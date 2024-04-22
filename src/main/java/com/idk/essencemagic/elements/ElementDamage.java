package com.idk.essencemagic.elements;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ElementDamage implements Listener {

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof LivingEntity attacker)) return;
        if(!(e.getEntity() instanceof LivingEntity entity)) return;
        double magnification = 1L;
        if(ItemHandler.isHoldingCustomItem(attacker)) {
            Item itemInMainHand = ItemHandler.getCorrespondingItem(attacker);
            Element element = itemInMainHand.getElement();
            ItemStack helmet = entity.getEquipment().getHelmet();
            if(helmet != null && ItemHandler.isCustomItem(helmet)) {
                Element elementHelmet = ItemHandler.getCorrespondingItem(helmet).getElement();
                if(element.getSuppressMap().containsKey(elementHelmet))
                    magnification *= element.getSuppressMap().get(elementHelmet);
                if(element.getSuppressedMap().containsKey(elementHelmet))
                    magnification *= (1 / element.getSuppressedMap().get(elementHelmet));
            }
            if(element.getSuppressMap().containsKey(Element.elements.get("none")))
                magnification = element.getSuppressMap().get(Element.elements.get("none"));
            e.setDamage(e.getDamage() * magnification);
            attacker.sendMessage("attack damage x" + magnification + ", " + e.getDamage());
        } else {
            attacker.sendMessage(e.getDamage() * magnification + "");
        }
    }
}
