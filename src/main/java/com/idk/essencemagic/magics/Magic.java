package com.idk.essencemagic.magics;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.magics.modifiers.Damage;
import com.idk.essencemagic.magics.particles.Spiral;
import com.idk.essencemagic.skills.SingleSkill;
import com.idk.essencemagic.skills.SkillType;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class Magic {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    // record all child types
    public static final Map<String, Magic> magics = new HashMap<>();

    private final String name;

    private final String displayName;

    private final MagicType.Feature featureType;

    private final MagicType.Attribute attributeType;

    private final String lowerLimit;

    private final String upperLimit;

    private final List<Element> applyingElements = new ArrayList<>();

    @Nullable private final CustomParticle particle;

    private final List<Modifier> modifiers = new ArrayList<>();

    private final List<String> info = new ArrayList<>();

    public Magic(String magicName) {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MAGICS;
        name = magicName;

        // set magic name (default to "")
        if(cm.isString(magicName + ".name"))
            displayName = cm.outString(magicName+ ".name");
        else
            displayName = "";

        // set magic types (default to attack & element)
        if(cm.isString(magicName + ".type.feature"))
            featureType = MagicType.Feature.valueOf(cm.getString(magicName + ".type.feature").toUpperCase());
        else
            featureType = MagicType.Feature.ATTACK;
        if(cm.isString(magicName + ".type.attribute"))
            // e.g. attribute: element;flame
            attributeType = MagicType.Attribute.valueOf(cm.getString(magicName + ".type.attribute").split(";")[0].toUpperCase());
        else
            attributeType = MagicType.Attribute.ELEMENT;
        info.add("&7Type: " + featureType.name + " / " + attributeType.name);

        // set magic available range (lower limit & upper limit)(default to F;0)
        String prefix = magicName + ".available_range";
        if(cm.isString(prefix + ".lower_limit"))
            lowerLimit = cm.getString(prefix + ".lower_limit");
        else
            lowerLimit = "F;0";
        info.add("&7Lower Limit: " + lowerLimit);
        if(cm.isString(prefix + ".upper_limit"))
            upperLimit = cm.getString(prefix + ".upper_limit");
        else
            upperLimit = "F;0";
        info.add("&7Upper Limit: " + upperLimit);

        // set magic applying elements (default to none)
        if(cm.isList(magicName + ".applying_elements")) {
            for(String element : cm.getStringList(magicName + ".applying_elements")) {
                if(Element.elements.containsKey(element))
                    applyingElements.add(Element.elements.get(element));
            }
        } else if(cm.isString(magicName + ".applying_elements")) {
            String element = cm.getString(magicName + ".applying_elements");
            if(Element.elements.containsKey(element))
                applyingElements.add(Element.elements.get(element));
        }
        if(applyingElements.isEmpty())
            applyingElements.add(Element.elements.get("none"));
        List<String> elementNames = new ArrayList<>();
        for(Element element : applyingElements) {
            elementNames.add(element.getName());
        }
        info.add("&7Applying Elements: " + elementNames);

        // set magic particles (default to null)
        if(cm.isString(magicName + ".particles")) {
            String path = magicName + ".particles";
            if(cm.getString(path).equalsIgnoreCase("spiral")) {
                particle = new Spiral();
                info.add("&7Particles: Spiral");
            }
            else
                particle = null;
        } else
            particle = null;

        // set magic modifiers (default to empty)
        if(cm.isConfigurationSection(magicName + ".modifiers")){
            info.add("&7Modifiers:");
            for(String modifier : cm.getConfigurationSection(magicName + ".modifiers").getKeys(false)) {
                if(modifier.equalsIgnoreCase("damage"))
                    modifiers.add(new Damage(magicName));
            }
        }
        for(Modifier modifier : getModifiers()) {
            info.add("&f  " + modifier.getName() + ":");
            info.addAll(modifier.getInfo());
        }
        info.replaceAll(Util::colorize);
    }

    public abstract void perform();
}
