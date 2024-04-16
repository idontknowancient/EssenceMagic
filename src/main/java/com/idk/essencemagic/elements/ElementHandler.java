package com.idk.essencemagic.elements;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.Set;

public class ElementHandler implements Listener {

    private static final Util ce = Util.getUtil("elements"); //config elements, no .yml needed
    private static final Util cm = Util.getUtil("menus"); //config menus

    private static final FileConfiguration config = ce.getConfig();

    public static void setElements() {
        //directly use getKeys, not getDefaultSection
        Set<String> elementSet = config.getKeys(false);

        // set necessary element (none)
        Element.elements.put("none", new Element("none"));

        //register elements
        for(String s : elementSet) {
            Element.elements.put(s, new Element(s));
        }

        //set suppress and suppressed elements
        for(String s : elementSet) {
            ConfigurationSection suppressSection = config.getConfigurationSection(s+".suppress");
            if(suppressSection != null) {
                Set<String> suppressElement = suppressSection.getKeys(false);
                for(String s2 : suppressElement) {
                    Element.elements.get(s).getSuppressMap().put(
                            Element.elements.get(s2), ce.getd(s+".suppress."+s2+".damage_modifier"));
                }
            }

            ConfigurationSection suppressedSection = config.getConfigurationSection(s+".suppressed");
            if(suppressedSection != null) {
                Set<String> suppressedElement = suppressedSection.getKeys(false);
                for(String s2 : suppressedElement) {
                    Element.elements.get(s).getSuppressedMap().put(
                            Element.elements.get(s2), ce.getd(s+".suppressed."+s2+".damage_modifier"));
                }
            }
        }

        Element.elements.forEach((s, e)->{
            //s is String and e is Element
            List<String> newLore = e.getDescription();

            if(cm.getb("element.show_suppress_elements")) {
                newLore.add("");
                newLore.add(cm.outs("element.suppress_elements_opening"));
                e.getSuppressMap().forEach((se,d)->{
                    //se is a SuppressElement and d is the scale
                    newLore.add(se.getDisplayName() + cm.colorize(" &7x" + d.toString()));
                });
            }
            if(cm.getb("element.show_suppressed_elements")) {
                newLore.add("");
                newLore.add(cm.outs("element.suppressed_elements_opening"));
                e.getSuppressedMap().forEach((se,d)->{
                    //se is a SuppressedElement and d is the scale
                    newLore.add(se.getDisplayName() + cm.colorize(" &7x" + d.toString()));
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
