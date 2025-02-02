package com.idk.essencemagic.skills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.skills.singleSkills.Potion;
import com.idk.essencemagic.skills.singleSkills.Shoot;
import com.idk.essencemagic.skills.singleSkills.Wait;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.*;

@Getter
public class Skill {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();

    public static final Map<String, Skill> skills = new HashMap<>();

    private final String name;

    private final String displayName;

    private final List<Trigger> triggers = new ArrayList<>();

    private final Map<String, SingleSkill> singleSkills = new HashMap<>();

    private final List<String> orders = new ArrayList<>();

    private final List<String> info = new ArrayList<>();

    public Skill(String skillName) {
        // config skills
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        String singleSkillType;

        name = skillName;

        // set skill name (default to "")
        if(cs.isString(skillName + ".name"))
            displayName = cs.outString(skillName + ".name");
        else
            displayName = "";

        // set skill triggers (default to right_click)
        if(cs.isList(skillName + ".triggers")) {
            for(String trigger : cs.getStringList(skillName + ".triggers")) {
                triggers.add(Trigger.valueOf(trigger.toUpperCase()));
            }
        } else if(cs.isString(skillName + ".triggers"))
            triggers.add(Trigger.valueOf(cs.getString(skillName + ".triggers").toUpperCase()));
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

        // set activation orders and wait (default to original orders)
        if(cs.isList(skillName + ".orders")) {
            for(String order : cs.getStringList(skillName + ".orders")) {
                if(singleSkills.containsKey(order))
                    orders.add(order);
                else if(order.startsWith("wait")) {
                    orders.add(order);
                    // e.g. - wait 100 (5s) -> put only one wait to notify there is wait in orders
                    singleSkills.putIfAbsent("wait", new Wait("wait"));
                }
            }
        } else if(cs.isString(skillName + ".orders"))
            if(singleSkills.containsKey(cs.getString(skillName + ".orders")))
                orders.add(cs.getString(skillName + ".orders"));
        if(orders.isEmpty())
            orders.addAll(singleSkills.keySet());
        info.add("&7Orders: " + orders);

        // set info list
        info.add("&7Triggers: " + getTriggers());

        if(getSingleSkills().isEmpty()) {
            info.add(cm.outString("skill.no-skill"));
        } else {
            for(SingleSkill singleSkill : getSingleSkills().values()) {
                // ignore wait info
                if(singleSkill.getSkillType().equals(SkillType.WAIT)) continue;
                info.add("&f" + singleSkill.getName() + ":");
                info.addAll(singleSkill.getInfo());
            }
        }
        info.replaceAll(Util::colorize);
    }
}
