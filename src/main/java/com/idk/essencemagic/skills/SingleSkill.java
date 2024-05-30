package com.idk.essencemagic.skills;

import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SingleSkill {

    @NotNull private final String name;

    @NotNull private final SkillType type;

    @Nullable private final List<ClickType> triggers = new ArrayList<>();

    @Nullable private final List<String> targets = new ArrayList<>();

    @Nullable private final List<String> requirements = new ArrayList<>();

    @Nullable private final int cooldown;

    @Nullable private final double probability;

    @Nullable private final Map<String, Object> costs = new HashMap<>();

    /*
     * Use when a skill only has one single skill.
     */
    public SingleSkill(@NotNull String skillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path = skillName;
        name = skillName;
        type = SkillType.valueOf(cs.getString(cs + ".type"));

        String triggersPath = path + ".triggers";
        //should check if a single string is counted in
        if(cs.isList(triggersPath)) {
            for(String s : cs.getStringList(triggersPath)) {
                triggers.add(ClickType.valueOf(s));
            }
        }

        String targetsPath = path + ".targets";
        //should check if a single string is counted in
        //unhandled
        if(cs.isList(targetsPath)) {
            targets.addAll(cs.getStringList(targetsPath));
        }

        String requirementsPath = path + ".requirements";
        //should check if a single string is counted in
        //unhandled
        if(cs.isList(requirementsPath)) {
            targets.addAll(cs.getStringList(requirementsPath));
        }

        String cooldownPath = path + ".cooldown";
        if(cs.isInteger(cooldownPath))
            cooldown = cs.getInteger(cooldownPath);
        else
            cooldown = 0;

        String probabilityPath = path + ".probability";
        if(cs.isDouble(probabilityPath))
            probability = cs.getDouble(probabilityPath);
        else
            probability = 0d;

        String costsPath = path + ".costs";
        //unhandled
        if(cs.isConfigurationSection(costsPath)) {
            for(String s : cs.getConfigurationSection(costsPath).getKeys(false)) {
                String  costPath = costsPath + s;
                if(cs.isString(costPath))
                    costs.put(s, cs.getString(costPath));
                else if(cs.isInteger(costPath))
                    costs.put(s, cs.getInteger(costPath));
                else if(cs.isDouble(costPath))
                    costs.put(s, cs.getDouble(costPath));
            }
        }
    }

    /*
     * Use when a skill has multiple single skills.
     */
    public SingleSkill(String skillName, @NotNull String singleSkillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path = skillName + ".skills." + singleSkillName;
        name = singleSkillName;
        type = SkillType.valueOf(cs.getString(cs + ".type"));

        String triggersPath = path + ".triggers";
        //should check if a single string is counted in
        if(cs.isList(triggersPath)) {
            for(String s : cs.getStringList(triggersPath)) {
                triggers.add(ClickType.valueOf(s));
            }
        }

        String targetsPath = path + ".targets";
        //should check if a single string is counted in
        //unhandled
        if(cs.isList(targetsPath)) {
            targets.addAll(cs.getStringList(targetsPath));
        }

        String requirementsPath = path + ".requirements";
        //should check if a single string is counted in
        //unhandled
        if(cs.isList(requirementsPath)) {
            targets.addAll(cs.getStringList(requirementsPath));
        }

        String cooldownPath = path + ".cooldown";
        if(cs.isInteger(cooldownPath))
            cooldown = cs.getInteger(cooldownPath);
        else
            cooldown = 0;

        String probabilityPath = path + ".probability";
        if(cs.isDouble(probabilityPath))
            probability = cs.getDouble(probabilityPath);
        else
            probability = 0d;

        String costsPath = path + ".costs";
        //unhandled
        if(cs.isConfigurationSection(costsPath)) {
            for(String s : cs.getConfigurationSection(costsPath).getKeys(false)) {
                String  costPath = costsPath + s;
                if(cs.isString(costPath))
                    costs.put(s, cs.getString(costPath));
                else if(cs.isInteger(costPath))
                    costs.put(s, cs.getInteger(costPath));
                else if(cs.isDouble(costPath))
                    costs.put(s, cs.getDouble(costPath));
            }
        }
    }
}
