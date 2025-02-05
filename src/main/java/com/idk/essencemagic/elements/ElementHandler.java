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

    private static void setElements() {
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
                            Element.elements.get(s2), ce.getDouble(s+".suppress."+s2+".damage-modifier"));
                    //suppressed
                    Element.elements.get(s2).getSuppressedMap().put(
                            Element.elements.get(s),
                            //*10000d / 10000d means rounding, decimal 4
                            (double) Math.round(1 / ce.getDouble(s+".suppress."+s2+".damage-modifier")*10000d)/10000d);
                }
            }
        }

        //set new lore if we want it to show suppress/suppressed elements
        for(Element e : Element.elements.values()) {
            List<String> newLore = e.getDescription();

            if(cm.getBoolean("element.show-suppress-elements")) {
                newLore.add("");
                newLore.add(cm.outString("element.suppress-elements-opening"));
                for(Element se : e.getSuppressMap().keySet())
                    newLore.add(se.getDisplayName() + Util.colorize(" &7x" + e.getSuppressMap().get(se)));
            }
            if(cm.getBoolean("element.show-suppressed-elements")) {
                newLore.add("");
                newLore.add(cm.outString("element.suppressed-elements-opening"));
                for(Element se : e.getSuppressedMap().keySet())
                    newLore.add(se.getDisplayName() + Util.colorize(" &7x" + e.getSuppressedMap().get(se)));
            }

            e.setDescription(newLore);
        }
    }
}
