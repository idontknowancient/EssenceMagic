package com.idk.essencemagic.elements;

import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

public class ElementHandler {

    public static void initialize() {
        Element.elements.clear();
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
        //default element setting
        if(!Element.elements.containsKey("none"))
            Element.elements.put("none", new Element("none"));

        //set suppress and suppressed elements
        for(String s : elementSet) {
            if(!ce.isConfigurationSection(s+".suppress")) continue;
            ConfigurationSection suppressSection = ce.getConfigurationSection(s+".suppress");
            if(suppressSection != null) {
                Set<String> suppressElement = suppressSection.getKeys(false);
                for(String s2 : suppressElement) {
                    //suppress
                    Element.elements.get(s).getSuppressMap().put(
                            Element.elements.get(s2), ce.getDouble(s+".suppress."+s2+".damage_modifier"));
                    //suppressed
                    Element.elements.get(s2).getSuppressedMap().put(
                            Element.elements.get(s),
                            //*100d / 100d means rounding, decimal 2
                            (double) Math.round(1 / ce.getDouble(s+".suppress."+s2+".damage_modifier")*100d)/100d);
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
}
