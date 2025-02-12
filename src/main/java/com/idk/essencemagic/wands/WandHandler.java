package com.idk.essencemagic.wands;

import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WandHandler implements Listener {

    public static void initialize() {
        Wand.wands.clear();
        setWands();
    }

    private static void setWands() {
        Set<String> wandSet = ConfigFile.ConfigName.WANDS.getConfig().getKeys(false);
        for(String wand : wandSet) {
            Wand.wands.put(wand, new Wand(wand));
        }
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

    //return the corresponding wand
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

    //return the corresponding wand in an entity's main hand
    @Nullable
    public static Wand getCorrespondingWand(LivingEntity entity) {
        if(entity.getEquipment() == null ||
                entity.getEquipment().getItemInMainHand().getItemMeta() == null) return null;
        for(Wand w : Wand.wands.values()) {
            if(w.getItemStack().getItemMeta() == null) continue;
            if(w.getItemStack().getItemMeta().getPersistentDataContainer()
                    .equals(entity.getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer())) {
                return w;
            }
        }
        return null;
    }

    // handle mana injection
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        boolean right_click = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);

        if(!WandHandler.isHoldingWand(player)) return;
        Wand wand = WandHandler.getCorrespondingWand(player);
        if(wand == null) return;

        double playerMana = PlayerData.dataMap.get(player.getName()).getMana();
        double manaInjection = wand.getManaInjection();
        // player's mana is less than the amount that will be consumed per right click
        if(playerMana < manaInjection) return;
        wand.setStorageMana(wand.getStorageMana() + manaInjection);
        PlayerData.dataMap.get(player.getName()).setMana(playerMana - manaInjection);
        player.sendMessage("injecting to wand: " + manaInjection + ", now: " + wand.getStorageMana());
    }
}
