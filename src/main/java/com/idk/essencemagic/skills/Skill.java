package com.idk.essencemagic.skills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Skill {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Skill> skills = new HashMap<>();

    private final String name;

    @Nullable private final String displayName;

    private final List<SingleSkill> singleSkills = new ArrayList<>();

    public Skill(String skillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS; //config skills
        name = skillName;
        if(cs.isString(skillName + ".name"))
            displayName = cs.outString(skillName + ".name");
        else
            displayName = null;
        if(cs.isConfigurationSection(skillName + ".skills")) {
            for(String skill : cs.getStringList(skillName + ".skills")) {
                singleSkills.add(new SingleSkill(skillName, skill));
            }
        } else {
            singleSkills.add(new SingleSkill(skillName));
        }
    }
}
