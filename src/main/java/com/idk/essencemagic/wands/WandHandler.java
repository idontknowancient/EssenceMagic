package com.idk.essencemagic.wands;

import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.placeholders.InternalPlaceholderHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

    // return wand and handle the initial registration (specific wand key)
    // use when getting from a string
    @Nullable
    public static Wand getSpecificWand(Player player, String wandName) {
        Wand wand = new Wand(wandName);
//        NamespacedKey specificWandKey = new NamespacedKey(EssenceMagic.getPlugin(),
//                // specific wand key
//                player.getName() + wandName + System.currentTimeMillis());
        ItemMeta itemMeta = wand.getItemStack().getItemMeta();
        if(itemMeta == null) return null;
//        itemMeta.getPersistentDataContainer().set(specificWandKey, PersistentDataType.STRING, wandName);
//        wand.setSpecificWandKey(specificWandKey);
//        wand.getItemStack().setItemMeta(itemMeta);
        //Wand.specificWands.put(itemMeta.getPersistentDataContainer(), wand);

        return wand;
    }

    // return wand and handle the initial registration (specific wand key)
    // use when getting from an item stack (compare using persistent data container)
    @Nullable
    public static Wand getSpecificWand(Player player, ItemStack itemStack) {
        if(itemStack.getItemMeta() == null) return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        for(Wand wand : Wand.wands.values()) {
            ItemMeta wandMeta = wand.getItemStack().getItemMeta();
            if(wandMeta == null) return null;
            if(itemMeta.getPersistentDataContainer().equals(wandMeta.getPersistentDataContainer()))
                return getSpecificWand(player, wand.getName());
        }
        return null;
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

    // return the corresponding wand
    @Nullable
    public static Wand getCorrespondingWand(ItemStack itemStack) {
        if(itemStack.getItemMeta() == null) return null;
        for(Wand w : Wand.wands.values()) {
            if(w.getItemStack().getItemMeta() == null) continue;
            if(w.getItemStack().getItemMeta().getPersistentDataContainer()
                    .equals(itemStack.getItemMeta().getPersistentDataContainer())) {
                return w;
            }
        }
        return null;
    }

    // return the corresponding wand in an entity's main hand
    @Nullable
    public static Wand getCorrespondingWand(LivingEntity entity) {
        if(entity.getEquipment() == null ||
                entity.getEquipment().getItemInMainHand().getItemMeta() == null) return null;
        PersistentDataContainer container = entity.getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer();
//        if(Wand.specificWands.containsKey(container))
//            return Wand.specificWands.get(container);
        return null;
        /*for(Wand w : Wand.wands.values()) {
            if(w.getItemStack().getItemMeta() == null) continue;
            if(w.getItemStack().getItemMeta().getPersistentDataContainer()
                    .equals(entity.getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer())) {
                return w;
            }
        }
        return null;*/
    }

    public static void updateWand(ItemStack wand) {
        ItemMeta meta = wand.getItemMeta();
        if(meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        String name = container.get(Wand.getWandKey(), PersistentDataType.STRING);
        List<String> lore = ConfigFile.ConfigName.WANDS.outStringList(name + ".lore", wand);
        meta.setLore(lore);
        wand.setItemMeta(meta);
    }

    // handle mana injection
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        // prevent the event being called twice
        if(e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        Action action = e.getAction();
        boolean right = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
        if(!right) return;

        if(!WandHandler.isHoldingWand(player)) return;
//        Wand wand = WandHandler.getCorrespondingWand(player);
        ItemStack wand = player.getInventory().getItemInMainHand();
        ItemMeta wandMeta = wand.getItemMeta();
        if(wandMeta == null) return;
        PersistentDataContainer container = wandMeta.getPersistentDataContainer();

        double playerMana = PlayerData.dataMap.get(player.getName()).getMana();
        double manaInjection = container.get(Wand.getInjectionKey(), PersistentDataType.DOUBLE);
        // player's mana is less than the amount that will be consumed per right click
        if(playerMana < manaInjection) return;
        double storageMana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE) + manaInjection;
        container.set(Wand.getManaKey(), PersistentDataType.DOUBLE, storageMana);
        wand.setItemMeta(wandMeta);
        PlayerData.dataMap.get(player.getName()).setMana(playerMana - manaInjection);
        player.sendMessage("injecting to wand: " + manaInjection + ", now: " + storageMana);
        updateWand(wand);
    }
}
