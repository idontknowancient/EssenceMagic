package com.idk.essencemagic.skills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.skills.singleSkills.Potion;
import com.idk.essencemagic.skills.singleSkills.Shoot;
import com.idk.essencemagic.utils.Util;
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

    private final List<Trigger> triggers = new ArrayList<>();

    private final Map<String, SingleSkill> singleSkills = new HashMap<>();

    private final List<String> info = new ArrayList<>();

    public Skill(String skillName) {
        // config skills
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        String singleSkillType;

        name = skillName;

        // set skill name (default to null)
        if(cs.isString(skillName + ".name"))
            displayName = cs.outString(skillName + ".name");
        else
            displayName = null;

        // set skill triggers (default to right_click)
        if(cs.isList(skillName + ".trigger")) {
            for(String trigger : cs.getStringList(skillName + ".trigger")) {
                triggers.add(Trigger.valueOf(trigger.toUpperCase()));
            }
        } else if(cs.isString(skillName + ".trigger"))
            triggers.add(Trigger.valueOf(cs.getString(skillName + ".trigger").toUpperCase()));
        if(triggers.isEmpty())
            triggers.add(Trigger.RIGHT_CLICK);

        // add single skills
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

        // set info list
        getInfo().add("&7Trigger: " + getTriggers());

        if(getSingleSkills().isEmpty()) {
            getInfo().add(cm.outString("skill.no-skill"));
        } else {
            for(SingleSkill singleSkill : getSingleSkills().values()) {
                getInfo().add("&f" + singleSkill.getName() + ":");
                getInfo().addAll(singleSkill.getInfo());
            }
        }
        getInfo().replaceAll(Util::colorize);
    }
}
