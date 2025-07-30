package com.idk.essencemagic.utils.damage;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.elements.ElementDamage;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageCalculator implements Listener {

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof LivingEntity attacker)) return;
        if(!(e.getEntity() instanceof LivingEntity entity)) return;

        double magnification = 1L;

        Item itemInMainHand = ItemHandler.getCorrespondingItem(attacker);
        if(itemInMainHand != null) {
            //armor - singular element damage
            magnification *= ElementDamage.getArmorElementMagnification(itemInMainHand.getElement(), entity);
            if(ConfigFile.ConfigName.CONFIG.getBoolean("extra-damage-by-mob-element") &&
                    !(entity instanceof Player))
                //mob - singular element damage
                magnification *= ElementDamage.getMobElementMagnification(itemInMainHand.getElement(), entity);
        } else {
            //armor - singular element damage
            magnification *= ElementDamage.getArmorElementMagnification(Element.elements.get("none"), entity);
            if(ConfigFile.ConfigName.CONFIG.getBoolean("extra-damage-by-mob-element") &&
                    !(entity instanceof Player))
                //mob - singular element damage
                magnification *= ElementDamage.getMobElementMagnification(Element.elements.get("none"), entity);
        }

        magnification = Math.round(magnification*10000d)/10000d;
        e.setDamage(e.getDamage() * magnification);
        attacker.sendMessage(Util.colorize("&7attack damage &bx" + magnification + "&7, " + e.getDamage()));
    }
}
