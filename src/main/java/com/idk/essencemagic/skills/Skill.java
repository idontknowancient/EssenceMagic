package com.idk.essencemagic.skills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Skill {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    private final String name;

    private final ClickType trigger;

    public static final Map<String, Skill> skills = new HashMap<>();

    @Nullable private final String displayName;

    private final Map<String, SingleSkill> singleSkills = new HashMap<>();

    public Skill(String skillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS; //config skills
        name = skillName;
        if(cs.isString(skillName + ".name"))
            displayName = cs.outString(skillName + ".name");
        else
            displayName = null;
        trigger = ClickType.valueOf(cs.getString(skillName + ".trigger").toUpperCase());
        if(cs.isConfigurationSection(skillName + ".skills")) {
            for(String singleSkillName : cs.getConfigurationSection(skillName + ".skills").getKeys(false)) {
                singleSkills.put(singleSkillName,new SingleSkill(skillName, singleSkillName));
            }
        } else {
            singleSkills.put(skillName, new SingleSkill(skillName));
        }
    }
}
