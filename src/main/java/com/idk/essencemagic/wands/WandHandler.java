package com.idk.essencemagic.wands;

import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class WandHandler implements Listener {

    public static void initialize() {
        Wand.wands.clear();
        //Wand.specificWands.clear();
        setWands();
    }

    private static void setWands() {
        Set<String> wandSet = ConfigFile.ConfigName.WANDS.getConfig().getKeys(false);
        for(String wand : wandSet) {
            Wand.wands.put(wand, new Wand(wand));
        }
    }

    // return specified wand
    // use when getting from a string
    @Nullable
    public static Wand getWand(String wandName) {
        if(Wand.wands.containsKey(wandName)) return new Wand(wandName);
        else return null;
    }

    // return specified wand
    // use when getting from an item
    @Nullable
    public static Wand getWand(ItemStack wand) {
        String name = getWandName(wand);
        if(name == null) return null;
        return getWand(name);
    }

    @Nullable
    public static String getWandName(ItemStack wand) {
        if(!isWand(wand)) return null;
        assert wand.getItemMeta() != null;
        return wand.getItemMeta().getPersistentDataContainer().get(Wand.getWandKey(), PersistentDataType.STRING);
    }

    public static boolean isWand(ItemStack i) {
        return i.getItemMeta() != null &&
                i.getItemMeta().getPersistentDataContainer().has(Wand.getWandKey());
    }

    public static boolean isHoldingWand(LivingEntity entity) {
        if(entity.getEquipment() == null) return false;
        ItemStack itemInMainHand = entity.getEquipment().getItemInMainHand();
        return isWand(itemInMainHand);
    }

    public static void updateWand(ItemStack wand) {
        ItemMeta meta = wand.getItemMeta();
        if(meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        String name = container.get(Wand.getWandKey(), PersistentDataType.STRING);
        // create new wand (internal name can't be change)
        ItemStack newWand = new Wand(name).getItemStack();
        ItemMeta newMeta = newWand.getItemMeta();
        if(newMeta == null) return;
        PersistentDataContainer newContainer = newMeta.getPersistentDataContainer();

        // copy the old one's mana to the new one
        Double Mana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE);
        double mana = Mana != null ? Mana : 0;
        newContainer.set(Wand.getManaKey(), PersistentDataType.DOUBLE, mana);

        // copy the old one's magic to the new one (adapting to new slot)
        Integer OldSlot = container.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
        int oldSlot = OldSlot != null ? OldSlot : 1;
        Integer NewSlot = newContainer.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
        int newSlot = NewSlot != null ? NewSlot : 1;
        String WandMagic = container.get(Wand.getWandMagicKey(), PersistentDataType.STRING);
        if(WandMagic == null) return;
        String[] wandMagic = WandMagic.split(";");
        StringBuilder newMagic;
        int index = 0;
        try {
            index = Integer.parseInt(wandMagic[wandMagic.length - 1]);
        } catch (NumberFormatException ignored) {
        }
        if(oldSlot > newSlot) { // e.g. "a;b;c;d;3" -> "a;b;c;0"
            newMagic = new StringBuilder();
            for(int i = 0; i < newSlot; i++) {
                newMagic.append(wandMagic[i]).append(";");
            }
            // avoid overflow
            if(index >= newSlot) index = 0;
            newMagic.append(index);
        } else if(oldSlot < newSlot) { // e.g. "a;b;c;d;3" -> "a;b;c;d;;3"
            newMagic = new StringBuilder(WandMagic.substring(0, WandMagic.lastIndexOf(";") + 1));
            newMagic.append(";".repeat(Math.max(0, newSlot - oldSlot)));
            newMagic.append(index);
        } else
            newMagic = new StringBuilder(WandMagic);
        newContainer.set(Wand.getWandMagicKey(), PersistentDataType.STRING, newMagic.toString());

        // update lore (based on old mana)
        newWand.setItemMeta(newMeta);
        List<String> lore = ConfigFile.ConfigName.WANDS.outStringList(name + ".lore", newWand);
        // handle "\n" in lore
        lore = Util.splitLore(lore);
        newMeta.setLore(lore);

        wand.setItemMeta(newMeta);
    }

    // handle mana injection (shift right)
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        // prevent the event being called twice
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        Action action = e.getAction();
        boolean right = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
        if(!right || !player.isSneaking()) return;

        if(!WandHandler.isHoldingWand(player)) return;
        ItemStack wand = player.getInventory().getItemInMainHand();
        ItemMeta wandMeta = wand.getItemMeta();
        if(wandMeta == null) return;
        PersistentDataContainer container = wandMeta.getPersistentDataContainer();

        double playerMana = PlayerData.dataMap.get(player.getName()).getMana();
        // prevent null pointer exception
        Double ManaInjection = container.get(Wand.getInjectionKey(), PersistentDataType.DOUBLE);
        double manaInjection = ManaInjection != null ? ManaInjection : 0;

        // player's mana is less than the amount that will be consumed per right click
        if(playerMana < manaInjection) {
            SystemMessage.INADEQUATE_MANA.send(player);
            return;
        }
        Double StorageMana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE);
        double storageMana = StorageMana != null ? StorageMana + manaInjection : 0;
        container.set(Wand.getManaKey(), PersistentDataType.DOUBLE, storageMana);
        wand.setItemMeta(wandMeta);

        // update player's mana
        PlayerData.dataMap.get(player.getName()).setMana(playerMana - manaInjection);
        player.sendMessage("injecting to wand: " + manaInjection + ", now: " + storageMana);
        updateWand(wand);
    }

    // handle magic switch (shift left)
    @EventHandler
    public void onPlayerLeftClick(PlayerInteractEvent e) {
        // prevent the event being called twice
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        Action action = e.getAction();
        boolean left = action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
        if(!left || !player.isSneaking()) return;

        if(!WandHandler.isHoldingWand(player)) return;
        ItemStack wand = player.getInventory().getItemInMainHand();
        ItemMeta wandMeta = wand.getItemMeta();
        if(wandMeta == null) return;
        PersistentDataContainer container = wandMeta.getPersistentDataContainer();

        String WandMagic = container.get(Wand.getWandMagicKey(), PersistentDataType.STRING);
        if(WandMagic == null) return;
        // get the magic from "1;2;3;index"
        String[] wandMagic = WandMagic.split(";");
//        String wandMagic = WandMagic.substring(0, WandMagic.lastIndexOf(";") + 1);
        int index = 0;
        try {
            // get the index from "1;2;3;index"
            index = Integer.parseInt(wandMagic[wandMagic.length - 1]);
        } catch (NumberFormatException ignored) {
        }
        Integer Slot = container.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
        int slot = Slot != null ? Slot : 1;

        // skip empty slot
        for(int i = 0; i < slot; i++) {
            if(++index < slot && !wandMagic[index].isEmpty()) {
                WandMagic = WandMagic.substring(0, WandMagic.lastIndexOf(";") + 1) + index;
                break;
            } else if(!wandMagic[0].isEmpty()) {
                WandMagic = WandMagic.substring(0, WandMagic.lastIndexOf(";") + 1) + 0;
                break;
            }
        }
        container.set(Wand.getWandMagicKey(), PersistentDataType.STRING, WandMagic);
        wand.setItemMeta(wandMeta);
        updateWand(wand);
        SystemMessage.WAND_MAGIC_SWITCH.send(player, wand);
    }

    // prevent block being broken while using wands
    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if(meta == null) return;
        if(meta.getPersistentDataContainer().has(Wand.getWandKey()))
            e.setCancelled(true);
    }
}
