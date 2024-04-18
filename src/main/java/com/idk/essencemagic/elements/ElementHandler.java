package com.idk.essencemagic.elements;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.Set;

public class ElementHandler implements Listener {

    public static void initialize() {
        setElements();
    }

    public static void setElements() {
        //config instance
        ConfigFile.ConfigName ce = ConfigFile.ConfigName.ELEMENTS; //config elements
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS; //config menus

        //directly use getKeys, not getDefaultSection
        Set<String> elementSet = ce.getConfig().getKeys(false);

        //register elements
        for(String s : elementSet) {
            Element.elements.put(s, new Element(s));
        }

        //set suppress and suppressed elements
        for(String s : elementSet) {
            ConfigurationSection suppressSection = ce.getConfigurationSection(s+".suppress");
            if(suppressSection != null) {
                Set<String> suppressElement = suppressSection.getKeys(false);
                for(String s2 : suppressElement) {
                    Element.elements.get(s).getSuppressMap().put(
                            Element.elements.get(s2), ce.getDouble(s+".suppress."+s2+".damage_modifier"));
                }
            }

            ConfigurationSection suppressedSection = ce.getConfigurationSection(s+".suppressed");
            if(suppressedSection != null) {
                Set<String> suppressedElement = suppressedSection.getKeys(false);
                for(String s2 : suppressedElement) {
                    Element.elements.get(s).getSuppressedMap().put(
                            Element.elements.get(s2), ce.getDouble(s+".suppressed."+s2+".damage_modifier"));
                }
            }
        }

        //set new lore if we want it to show suppress/suppressed elements
        Element.elements.forEach((s, e)->{
            //s is String and e is Element
            List<String> newLore = e.getDescription();

            if(cm.getBoolean("element.show_suppress_elements")) {
                newLore.add("");
                newLore.add(cm.outString("element.suppress_elements_opening"));
                e.getSuppressMap().forEach((se,d)->{
                    //se is a SuppressElement and d is the scale
                    newLore.add(se.getDisplayName() + Util.colorize(" &7x" + d.toString()));
                });
            }
            if(cm.getBoolean("element.show_suppressed_elements")) {
                newLore.add("");
                newLore.add(cm.outString("element.suppressed_elements_opening"));
                e.getSuppressedMap().forEach((se,d)->{
                    //se is a SuppressedElement and d is the scale
                    newLore.add(se.getDisplayName() +Util.colorize(" &7x" + d.toString()));
                });
            }

            e.setDescription(newLore);
        });
    }

    @EventHandler
    public static void onAttack(EntityDamageByEntityEvent e) {
        LivingEntity entity = (LivingEntity) e.getEntity();
        LivingEntity damager = (LivingEntity) e.getDamager();
        Element entityElement = Element.elements.get("none");
        PersistentDataContainer damagerContainer = entity.getEquipment().getItemInMainHand().getItemMeta().getPersistentDataContainer();
        if(damagerContainer.has(Item.getItemKey())) {
            for(String s : Item.items.keySet()) {
                if(Item.items.get(s).getItem().equals(entity.getEquipment().getItemInMainHand())) {
                    entityElement = Item.items.get(s).getElement();
                }
            }
        }
    }
}
