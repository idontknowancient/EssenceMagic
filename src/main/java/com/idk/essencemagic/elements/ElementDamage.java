package com.idk.essencemagic.elements;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ElementDamage implements Listener {

    //record what is the type of the element of an armor to attacker's weapon
    private static final Map<Element, Integer> armorElementMap = new HashMap<>();

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof LivingEntity attacker)) return;
        if(!(e.getEntity() instanceof LivingEntity entity)) return;
        armorElementMap.clear();

        double magnification = 1L;

        if(ItemHandler.isHoldingCustomItem(attacker)) {
            Item itemInMainHand = ItemHandler.getCorrespondingItem(attacker);
            magnification = getElementMagnification(itemInMainHand.getElement(), entity, magnification);
        } else {
            magnification = getElementMagnification(Element.elements.get("none"), entity, magnification);
        }
        e.setDamage(e.getDamage() * magnification);
        attacker.sendMessage("attack damage x" + magnification + ", " + e.getDamage());
    }

    //itemInMainHand : item in attacker's main hand / entity : entity who gets damaged / magnification : the original magnification
    public static double getElementMagnification(Element element, LivingEntity entity, double magnification) {
        setSingularArmorElement(entity.getEquipment().getHelmet());
        setSingularArmorElement(entity.getEquipment().getChestplate());
        setSingularArmorElement(entity.getEquipment().getLeggings());
        setSingularArmorElement(entity.getEquipment().getBoots());
        for(Element e : armorElementMap.keySet()) {
            if (element.getSuppressMap().containsKey(e)) //suppress elements
                if (e.equals(Element.elements.get("none")))
                    magnification *= Math.round(Math.pow(element.getSuppressMap().get(e), armorElementMap.get(e))*10000d)/10000d;
                else
                    magnification *= Math.round(Math.pow(element.getSuppressMap().get(e), 1d / armorElementMap.get(e))*10000d)/10000d;
            else if (element.getSuppressedMap().containsKey(e)) //suppressed elements
                magnification *= Math.round(Math.pow(element.getSuppressedMap().get(e), armorElementMap.get(e) / 1.75d)*10000d)/10000d;
            else //nothing
                magnification *= 1;
        }
        return magnification;
    }

    public static void setSingularArmorElement(ItemStack armor) {
        if(armor != null && ItemHandler.isCustomItem(armor)) {
            Element armorElement = ItemHandler.getCorrespondingItem(armor).getElement();
            if (!armorElementMap.containsKey(armorElement))
                armorElementMap.put(armorElement, 1);
            else
                armorElementMap.put(armorElement, armorElementMap.get(armorElement) + 1);
        } else { //if not wearing a custom armor or wearing nothing
            Element none = Element.elements.get("none");
            if(!armorElementMap.containsKey(none))
                armorElementMap.put(none, 1);
            else
                armorElementMap.put(none, armorElementMap.get(none) + 1);
        }
    }
}
