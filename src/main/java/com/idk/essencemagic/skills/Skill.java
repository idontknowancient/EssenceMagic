package com.idk.essencemagic.skills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.skills.singleSkills.Potion;
import com.idk.essencemagic.skills.singleSkills.Shoot;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Skill {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    private final String name;

    private final Trigger trigger;

    @Nullable private final String displayName;

    public static final Map<String, Skill> skills = new HashMap<>();

    private final Map<String, SingleSkill> singleSkills = new HashMap<>();

    public Skill(String skillName) {
        // config skills
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String singleSkillType;

        name = skillName;

        if(cs.isString(skillName + ".name"))
            displayName = cs.outString(skillName + ".name");
        else
            displayName = null;

        trigger = Trigger.valueOf(cs.getString(skillName + ".trigger").toUpperCase());

        if(cs.isConfigurationSection(skillName + ".skills")) {
            // multiple single skills
            for(String singleSkillName : cs.getConfigurationSection(skillName + ".skills").getKeys(false)) {
                if(cs.isString(skillName + ".skills." + singleSkillName + ".type"))
                    singleSkillType = cs.getString(skillName + ".skills." + singleSkillName + ".type");
                else
                    // default skill to shoot
                    singleSkillType = "shoot";

                SkillType skillType = SkillType.valueOf(singleSkillType.toUpperCase());
                if(skillType.equals(SkillType.SHOOT))
                    singleSkills.put(singleSkillName, new Shoot(skillName, singleSkillName));
                else if(skillType.equals(SkillType.POTION))
                    singleSkills.put(singleSkillName, new Potion(skillName, singleSkillName));
            }
        } else {
            // one single skill
            if(cs.isString(skillName + ".type"))
                singleSkillType = cs.getString(skillName + ".type");
            else
                singleSkillType = "shoot";

            SkillType skillType = SkillType.valueOf(singleSkillType.toUpperCase());
            if(skillType.equals(SkillType.SHOOT))
                singleSkills.put(skillName, new Shoot(skillName));
            else if(skillType.equals(SkillType.POTION))
                singleSkills.put(skillName, new Potion(skillName));
        }
    }
}
