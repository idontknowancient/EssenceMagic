package com.idk.essencemagic.skills;

import com.idk.essencemagic.utils.configs.ConfigFile;

import java.util.Set;

public class SkillHandler {

    public static void initialize() {
        Skill.skills.clear();
        setSkills();
    }

    public static void setSkills() {
        Set<String> skillSet = ConfigFile.ConfigName.SKILLS.getConfig().getKeys(false);
        for(String s : skillSet) {
            Skill.skills.put(s, new Skill(s));
        }
    }

    public static void handleSkill() {

    }
}
