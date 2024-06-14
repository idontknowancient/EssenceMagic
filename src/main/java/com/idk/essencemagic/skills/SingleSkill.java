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

    @Nullable private final List<String> targets = new ArrayList<>();

    @Nullable private final List<String> requirements = new ArrayList<>();

    private final double cooldown;

    private final double probability;

    @Nullable private final List<String> costs = new ArrayList<>();

    /*for only shoot type*/
    @NotNull private final String projectile;

    private final double velocity;

    private final double power;

    private final double duration;

    /*
     * Use when a skill only has one single skill.
     */
    public SingleSkill(@NotNull String skillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path = skillName;
        name = skillName;
        type = SkillType.valueOf(cs.getString(path + ".type").toUpperCase());

        String targetsPath = path + ".targets";
        if(cs.isString(targetsPath)) {
            targets.add(cs.getString(targetsPath));
        } else if(cs.isList(targetsPath)) {
            targets.addAll(cs.getStringList(targetsPath));
        }

        String requirementsPath = path + ".requirements";
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
        if(cs.isString(costsPath)) {
            costs.add(cs.getString(costsPath));
        } else if(cs.isList(costsPath)) {
            costs.addAll(cs.getStringList(costsPath));
        }

        /*only for shoot type*/
        String projectilePath = path + ".projectile";
        if(cs.isString(projectilePath))
            projectile = cs.getString(projectilePath);
        else
            projectile = "snowball";

        String velocityPath = path + ".velocity";
        if(cs.isDouble(velocityPath))
            velocity = cs.getDouble(velocityPath);
        else if(cs.isInteger(velocityPath))
            velocity = cs.getInteger(velocityPath);
        else
            velocity = 1;

        String powerPath = path + ".power";
        if(cs.isDouble(powerPath))
            power = cs.getDouble(powerPath);
        else if(cs.isInteger(powerPath))
            power = cs.getInteger(powerPath);
        else
            power = 1;

        String durationPath = path + ".duration";
        if(cs.isDouble(durationPath))
            duration = cs.getDouble(durationPath);
        else if(cs.isInteger(durationPath))
            duration = cs.getInteger(durationPath);
        else
            duration = 3;
    }

    /*
     * Use when a skill has multiple single skills.
     */
    public SingleSkill(String skillName, @NotNull String singleSkillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path = skillName + ".skills." + singleSkillName;
        name = singleSkillName;
        type = SkillType.valueOf(cs.getString(path + ".type").toUpperCase());

        String targetsPath = path + ".targets";
        if(cs.isString(targetsPath)) {
            targets.add(cs.getString(targetsPath));
        } else if(cs.isList(targetsPath)) {
            targets.addAll(cs.getStringList(targetsPath));
        }

        String requirementsPath = path + ".requirements";
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
        if(cs.isString(costsPath)) {
            costs.add(cs.getString(costsPath));
        } else if(cs.isList(costsPath)) {
            costs.addAll(cs.getStringList(costsPath));
        }

        /*only for shoot type*/
        String projectilePath = path + ".projectile";
        if(cs.isString(projectilePath))
            projectile = cs.getString(projectilePath);
        else
           projectile = "snowball";

        String velocityPath = path + ".velocity";
        if(cs.isDouble(velocityPath))
            velocity = cs.getDouble(velocityPath);
        else if(cs.isInteger(velocityPath))
            velocity = cs.getInteger(velocityPath);
        else
            velocity = 1;

        String powerPath = path + ".power";
        if(cs.isDouble(powerPath))
            power = cs.getDouble(powerPath);
        else if(cs.isInteger(powerPath))
            power = cs.getInteger(powerPath);
        else
            power = 1;

        String durationPath = path + ".duration";
        if(cs.isDouble(durationPath))
            duration = cs.getDouble(durationPath);
        else if(cs.isInteger(durationPath))
            duration = cs.getInteger(durationPath);
        else
            duration = 3;
    }
}
