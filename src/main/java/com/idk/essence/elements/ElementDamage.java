package com.idk.essence.elements;

import com.idk.essence.items.ItemHandler;
import com.idk.essence.mobs.MobHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ElementDamage {

    //record what is the type of the element of an armor to attacker's weapon
    private static final Map<Element, Integer> armorElementMap = new HashMap<>();

    //itemInMainHand : item in attacker's main hand / entity : entity who gets damaged / magnification : the original magnification
    public static double getArmorElementMagnification(Element element, LivingEntity entity) {
        double magnification = 1;
        armorElementMap.clear();
        setSingularArmorElement(entity.getEquipment().getHelmet());
        setSingularArmorElement(entity.getEquipment().getChestplate());
        setSingularArmorElement(entity.getEquipment().getLeggings());
        setSingularArmorElement(entity.getEquipment().getBoots());
        for(Element e : armorElementMap.keySet()) {
            if (element.getSuppressMap().containsKey(e)) //suppress elements
                if (e.equals(Element.elements.get("none")))
                    magnification *= Math.round(Math.pow(element.getSuppressMap().get(e), armorElementMap.get(e))*10000d)/10000d;
                else {
                    double reciprocal_sum = 0;
                    for(int i = 1; i <= armorElementMap.get(e); i++) {
                        reciprocal_sum += (1d / i);
                    }
                    magnification *= Math.round(Math.pow(element.getSuppressMap().get(e), reciprocal_sum)*10000d)/10000d;
                }
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

    public static double getMobElementMagnification(Element element, LivingEntity entity) {
        double magnification = 1;
        if(MobHandler.isCustomMob(entity)) {
            Element mobElement = MobHandler.getCorrespondingMob(entity).getElement();
            if(element.getSuppressMap().containsKey(mobElement))
                magnification *= element.getSuppressMap().get(mobElement);
            if(element.getSuppressedMap().containsKey(mobElement))
                magnification *= element.getSuppressedMap().get(mobElement);
        } else {
            Element none = Element.elements.get("none");
            if(element.getSuppressMap().containsKey(none))
                magnification *= element.getSuppressMap().get(none);
            if(element.getSuppressedMap().containsKey(none))
                magnification *= element.getSuppressedMap().get(none);
        }
        return magnification;
    }
}
