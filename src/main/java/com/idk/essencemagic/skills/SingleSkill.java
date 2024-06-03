package com.idk.essencemagic.skills;

import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SingleSkill {

    @NotNull private final String name;

    @NotNull private final SkillType type;

    @Nullable private final List<ClickType> triggers = new ArrayList<>();

    @Nullable private final List<String> targets = new ArrayList<>();

    @Nullable private final List<String> requirements = new ArrayList<>();

    private final double cooldown;

    private final double probability;

    @Nullable private final List<String> costs = new ArrayList<>();

    /*
     * Use when a skill only has one single skill.
     */
    public SingleSkill(@NotNull String skillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path = skillName;
        name = skillName;
        type = SkillType.valueOf(cs.getString(path + ".type").toUpperCase());

        String triggersPath = path + ".triggers";
        //should check if a single string is counted in
        if(cs.isString(triggersPath)) {
            triggers.add(ClickType.valueOf(cs.getString(triggersPath).toUpperCase()));
        } else if(cs.isList(triggersPath)) {
            for(String s : cs.getStringList(triggersPath)) {
                triggers.add(ClickType.valueOf(s.toUpperCase()));
            }
        }

        String targetsPath = path + ".targets";
        //should check if a single string is counted in
        //unhandled
        if(cs.isString(targetsPath)) {
            targets.add(cs.getString(targetsPath));
        } else if(cs.isList(targetsPath)) {
            targets.addAll(cs.getStringList(targetsPath));
        }

        String requirementsPath = path + ".requirements";
        //should check if a single string is counted in
        //unhandled
        if(cs.isString(requirementsPath)) {
            requirements.add(cs.getString(requirementsPath));
        } else if(cs.isList(requirementsPath)) {
            requirements.addAll(cs.getStringList(requirementsPath));
        }

        String cooldownPath = path + ".cooldown";
        if(cs.isDouble(cooldownPath))
            cooldown = cs.getDouble(cooldownPath);
        else if(cs.isInteger(cooldownPath))
            cooldown = cs.getInteger(cooldownPath);
        else
            cooldown = 0;

        String probabilityPath = path + ".probability";
        if(cs.isDouble(probabilityPath))
            probability = cs.getDouble(probabilityPath);
        else if(cs.isInteger(probabilityPath))
            probability = cs.getInteger(probabilityPath);
        else
            probability = 0d;

        String costsPath = path + ".costs";
        //unhandled
        if(cs.isString(costsPath)) {
            costs.add(cs.getString(costsPath));
        } else if(cs.isList(costsPath)) {
            costs.addAll(cs.getStringList(costsPath));
        }
    }

    /*
     * Use when a skill has multiple single skills.
     */
    public SingleSkill(String skillName, @NotNull String singleSkillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path = skillName + ".skills." + singleSkillName;
        name = singleSkillName;
        type = SkillType.valueOf(cs.getString(path + ".type").toUpperCase());

        String triggersPath = path + ".triggers";
        //should check if a single string is counted in
        if(cs.isString(triggersPath)) {
            triggers.add(ClickType.valueOf(cs.getString(triggersPath).toUpperCase()));
        } else if(cs.isList(triggersPath)) {
            for(String s : cs.getStringList(triggersPath)) {
                triggers.add(ClickType.valueOf(s.toUpperCase()));
            }
        }

        String targetsPath = path + ".targets";
        //should check if a single string is counted in
        //unhandled
        if(cs.isString(targetsPath)) {
            targets.add(cs.getString(targetsPath));
        } else if(cs.isList(targetsPath)) {
            targets.addAll(cs.getStringList(targetsPath));
        }

        String requirementsPath = path + ".requirements";
        //should check if a single string is counted in
        //unhandled
        if(cs.isString(requirementsPath)) {
            requirements.add(cs.getString(requirementsPath));
        } else if(cs.isList(requirementsPath)) {
            requirements.addAll(cs.getStringList(requirementsPath));
        }

        String cooldownPath = path + ".cooldown";
        if(cs.isDouble(cooldownPath))
            cooldown = cs.getDouble(cooldownPath);
        else if(cs.isInteger(cooldownPath))
            cooldown = cs.getInteger(cooldownPath);
        else
            cooldown = 0;

        String probabilityPath = path + ".probability";
        if(cs.isDouble(probabilityPath))
            probability = cs.getDouble(probabilityPath);
        else if(cs.isInteger(probabilityPath))
            probability = cs.getInteger(probabilityPath);
        else
            probability = 0d;

        String costsPath = path + ".costs";
        //unhandled
        if(cs.isString(costsPath)) {
            costs.add(cs.getString(costsPath));
        } else if(cs.isList(costsPath)) {
            costs.addAll(cs.getStringList(costsPath));
        }
    }
}
