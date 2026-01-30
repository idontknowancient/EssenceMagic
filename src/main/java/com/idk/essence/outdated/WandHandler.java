package com.idk.essence.outdated;

import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.messages.SystemMessage;
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
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WandHandler implements Listener {

    public static void initialize() {
        Wand.wands.clear();
        //Wand.specificWands.clear();
        setWands();
    }

    private static void setWands() {
        Set<String> wandSet = ConfigManager.DefaultFile.ARCANA.getConfig().getKeys(false);
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

    public static double getMana(ItemStack wand) {
        if(!isWand(wand)) return -1;
        assert wand.getItemMeta() != null;
        Double Mana = wand.getItemMeta().getPersistentDataContainer().get(Wand.getManaKey(), PersistentDataType.DOUBLE);
        return Mana != null ? Mana : 0;
    }

    public static void setMana(ItemStack wand, double mana) {
        if(!isWand(wand)) return;
        assert wand.getItemMeta() != null;
        ItemMeta meta = wand.getItemMeta();
        meta.getPersistentDataContainer().set(Wand.getManaKey(), PersistentDataType.DOUBLE, mana);
        wand.setItemMeta(meta);
    }

    public static double getInjection(ItemStack wand) {
        if(!isWand(wand)) return -1;
        assert wand.getItemMeta() != null;
        Double Injection = wand.getItemMeta().getPersistentDataContainer().get(Wand.getInjectionKey(), PersistentDataType.DOUBLE);
        return Injection != null ? Injection : 0;
    }

    public static void setInjection(ItemStack wand, double injection) {
        if(!isWand(wand)) return;
        assert wand.getItemMeta() != null;
        ItemMeta meta = wand.getItemMeta();
        meta.getPersistentDataContainer().set(Wand.getInjectionKey(), PersistentDataType.DOUBLE, injection);
        wand.setItemMeta(meta);
    }

    public static int getSlot(ItemStack wand) {
        if(!isWand(wand)) return -1;
        assert wand.getItemMeta() != null;
        Integer Slot = wand.getItemMeta().getPersistentDataContainer().get(Wand.getSlotKey(), PersistentDataType.INTEGER);
        return Slot != null ? Slot : 1;
    }

    public static void setSlot(ItemStack wand, int slot) {
        if(!isWand(wand)) return;
        assert wand.getItemMeta() != null;
        ItemMeta meta = wand.getItemMeta();
        meta.getPersistentDataContainer().set(Wand.getSlotKey(), PersistentDataType.INTEGER, slot);
        wand.setItemMeta(meta);
    }

    public static String getWandMagic(ItemStack wand) {
        if(!isWand(wand)) return "";
        assert wand.getItemMeta() != null;
        String WandMagic = wand.getItemMeta().getPersistentDataContainer().get(Wand.getWandMagicKey(), PersistentDataType.STRING);
        return WandMagic != null ? WandMagic : "";
    }

    public static void setWandMagic(ItemStack wand, String wandMagic) {
        if(!isWand(wand)) return;
        assert wand.getItemMeta() != null;
        ItemMeta meta = wand.getItemMeta();
        meta.getPersistentDataContainer().set(Wand.getWandMagicKey(), PersistentDataType.STRING, wandMagic);
        wand.setItemMeta(meta);
    }

    public static boolean isWand(ItemStack i) {
        return i != null && i.getItemMeta() != null &&
                i.getItemMeta().getPersistentDataContainer().has(Wand.getWandKey());
    }

    public static boolean isHoldingWand(LivingEntity entity) {
        if(entity.getEquipment() == null) return false;
        ItemStack itemInMainHand = entity.getEquipment().getItemInMainHand();
        return isWand(itemInMainHand);
    }

    public static void updateWand(ItemStack wand) {
        if(!isWand(wand)) return;
        ItemMeta meta = wand.getItemMeta();
        assert meta != null;

        String name = getWandName(wand);
        // create a new wand (internal name can't be change)
        ItemStack newWand = new Wand(name).getItemStack();
        if(newWand.getItemMeta() == null) return;

        // copy the old one's mana to the new one
        setMana(newWand, getMana(wand));

        // copy the old one's magic to the new one (adapting to the new slot)
        int oldSlot = getSlot(wand);
        int newSlot = getSlot(newWand);
        String WandMagic = getWandMagic(wand);
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
        setWandMagic(newWand, newMagic.toString());

        // update lore (based on old mana)
//        List<String> lore = ConfigManager.DefaultFile.WANDS.outStringList(name + ".lore", newWand);
//        // handle "\n" in lore
//        Util.setLore(newWand, Util.splitLore(lore));

        wand.setItemMeta(newWand.getItemMeta());
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
        if(wand.getItemMeta() == null) return;

        double playerMana = PlayerDataManager.get(player).getMana();
        double manaInjection = getInjection(wand);

        // the player's mana is less than the amount that will be consumed per right click
        if(playerMana < manaInjection) {
            SystemMessage.INADEQUATE_MANA.send(player);
            return;
        }
        setMana(wand, getMana(wand) + manaInjection);

        // update player's mana
        PlayerDataManager.get(player).setMana(playerMana - manaInjection);
        SystemMessage.WAND_MANA_INJECTED.send(player, wand);

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

        String WandMagic = getWandMagic(wand);
        // get the magic from "1;2;3;index"
        String[] wandMagic = WandMagic.split(";");
        int index = 0;
        try {
            // get the index from "1;2;3;index"
            index = Integer.parseInt(wandMagic[wandMagic.length - 1]);
        } catch (NumberFormatException ignored) {
        }
        int slot = getSlot(wand);

        // get array like [0, 1, 2, 3, 0, 1, 2, 3] to process repetition
        int[] order = generateRepeatedArray(slot);
        for(int i = index; i < index + slot - 1;) {
            if(!wandMagic[order[++i]].isEmpty()) {
                WandMagic = WandMagic.substring(0, WandMagic.lastIndexOf(";") + 1) + order[i];
                break;
            }
        }

        setWandMagic(wand, WandMagic);
        SystemMessage.WAND_MAGIC_SWITCH.send(player, wand);

        updateWand(wand);
    }

    // prevent block being broken while using wands
    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if(ClickHandler.shouldCancelLeft(e.getPlayer()))
            e.setCancelled(true);
    }

    private static int[] generateRepeatedArray(int x) {
        int[] arr = new int[2 * x];
        for(int i = 0; i < x; i++) {
            arr[i] = i;
            arr[i + x] = i;
        }
        return arr;
    }

}
