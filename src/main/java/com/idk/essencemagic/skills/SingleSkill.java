package com.idk.essencemagic.skills;

import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@NotNull
public abstract class SingleSkill {

    private final String name;

    private double cooldown;

    private double probability;

    protected SkillType skillType;

    private final List<String> targets = new ArrayList<>();

    private final List<String> requirements = new ArrayList<>();

    private final List<String> costs = new ArrayList<>();

    private final List<String> info = new ArrayList<>();

    /*
     * Use when a skill only has one single skill.
     */
    public SingleSkill(@NotNull String skillName) {
        name = skillName;
        skillSetting(skillName + ".");
    }

    /*
     * Use when a skill has multiple single skills.
     */
    public SingleSkill(String skillName, @NotNull String singleSkillName) {
        name = singleSkillName;
        skillSetting(skillName + ".skills." + singleSkillName);
    }

    private void skillSetting(String path) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;

        // set skill cooldown (default to 0)
        if(!cs.isDouble(path + ".cooldown") || cs.getDouble(path + ".cooldown") < 0)
            if(!cs.isInteger(path + ".cooldown") || cs.getInteger(path + ".cooldown") < 0)
                cooldown = 0;
            else
                cooldown = cs.getInteger(path + ".cooldown");
        else
            cooldown = cs.getDouble(path + ".cooldown");
        info.add("  &7Cooldown: " + cooldown);

        // set skill probability (default to 1 / 100%)
        if(!cs.isDouble(path + ".probability") || cs.getDouble(path + ".probability") < 0)
            if(!cs.isInteger(path + ".probability") || cs.getInteger(path + ".probability") < 0)
                probability = 1;
            else
                probability = cs.getInteger(path + ".probability");
        else
            probability = cs.getDouble(path + ".probability");
        info.add("  &7Probability: " + probability);

        // set skill targets (default to self)
        if(cs.isList(path + ".targets"))
            targets.addAll(cs.getStringList(path + ".targets"));
        else if(cs.isString(path + ".targets"))
            targets.add(cs.getString(path + ".targets"));
        if(targets.isEmpty())
            targets.add("self");
        info.add("  &7Targets: " + targets);

        // set skill requirements (default to empty)
        if(cs.isList(path + ".requirements"))
            requirements.addAll(cs.getStringList(path + ".requirements"));
        else if(cs.isString(path + ".requirements"))
            requirements.add(cs.getString(path + ".requirements"));
        info.add("  &7Requirements: " + requirements);

        // set skill costs (default to empty)
        if(cs.isList(path + ".costs"))
            costs.addAll(cs.getStringList(path + ".costs"));
        else if(cs.isString(path + ".costs"))
            costs.add(cs.getString(path + ".costs"));
        info.add("  &7Costs: " + costs);
    }

    protected abstract void skillDetailsSetting(String path);

    protected abstract void setSkillType();

    protected abstract void perform(LivingEntity target);
}
