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
        skillSetting(skillName, "");
    }

    /*
     * Use when a skill has multiple single skills.
     */
    public SingleSkill(String skillName, @NotNull String singleSkillName) {
        name = singleSkillName;
        skillSetting(skillName, singleSkillName);
    }

    private void skillSetting(String skillName, String singleSkillName) {
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;
        String path;
        if(singleSkillName.isEmpty())
            path = skillName;
        else
            path = skillName + ".skills." + singleSkillName;

        // set skill cooldown (default to 0)
        // check if specific cooldown is given
        if(cs.isDouble(path + ".cooldown") && cs.getDouble(path + ".cooldown") >= 0)
            cooldown = cs.getDouble(path + ".cooldown");
        else if(cs.isInteger(path + ".cooldown") && cs.getInteger(path + ".cooldown") >= 0)
            cooldown = cs.getInteger(path + ".cooldown");
        // check if overall cooldown is given (for multiple single skills)
        else if(cs.isDouble(skillName + ".cooldown") && cs.getDouble(skillName + ".cooldown") >= 0)
            cooldown = cs.getDouble(skillName + ".cooldown");
        else if(cs.isInteger(skillName + ".cooldown") && cs.getInteger(skillName + ".cooldown") >= 0)
            cooldown = cs.getInteger(skillName + ".cooldown");
        else
            cooldown = 0;
        info.add("  &7Cooldown: " + cooldown);

        // set skill probability (default to 1 / 100%)
        // check if specific probability is given
        if(cs.isDouble(path + ".probability") && cs.getDouble(path + ".probability") >= 0)
            probability = cs.getDouble(path + ".probability");
        else if(cs.isInteger(path + ".probability") && cs.getInteger(path + ".probability") >= 0)
            probability = cs.getInteger(path + ".probability");
        // check if overall cooldown is given (for multiple single skills)
        else if(cs.isDouble(skillName + ".probability") && cs.getDouble(skillName + ".probability") >= 0)
            probability = cs.getDouble(skillName + ".probability");
        else if(cs.isInteger(skillName + ".probability") && cs.getInteger(skillName + ".probability") >= 0)
            probability = cs.getInteger(skillName + ".probability");
        else
            probability = 0;
        info.add("  &7Probability: " + probability);

        // set skill targets (default to self)
        // check if specific targets are given
        if(cs.isList(path + ".targets"))
            targets.addAll(cs.getStringList(path + ".targets"));
        else if(cs.isString(path + ".targets"))
            targets.add(cs.getString(path + ".targets"));
        // check if overall targets are given (for multiple single skills)
        else if(cs.isList(skillName + ".targets"))
            targets.addAll(cs.getStringList(skillName + ".targets"));
        else if(cs.isString(skillName + ".targets"))
            targets.add(cs.getString(skillName + ".targets"));
        if(targets.isEmpty())
            targets.add("self");
        info.add("  &7Targets: " + targets);

        // set skill requirements (default to empty)
        // check if specific requirements are given
        if(cs.isList(path + ".requirements"))
            requirements.addAll(cs.getStringList(path + ".requirements"));
        else if(cs.isString(path + ".requirements"))
            requirements.add(cs.getString(path + ".requirements"));
        // check if overall requirements are given (for multiple single skills)
        else if(cs.isList(skillName + ".requirements"))
            requirements.addAll(cs.getStringList(skillName + ".requirements"));
        else if(cs.isString(skillName + ".requirements"))
            requirements.add(cs.getString(skillName + ".requirements"));
        info.add("  &7Requirements: " + requirements);

        // set skill costs (default to empty)
        // check if specific costs are given
        if(cs.isList(path + ".costs"))
            costs.addAll(cs.getStringList(path + ".costs"));
        else if(cs.isString(path + ".costs"))
            costs.add(cs.getString(path + ".costs"));
        // check if overall costs are given (for multiple single skills)
        else if(cs.isList(skillName + ".costs"))
            costs.addAll(cs.getStringList(skillName + ".costs"));
        else if(cs.isString(skillName + ".costs"))
            costs.add(cs.getString(skillName + ".costs"));
        info.add("  &7Costs: " + costs);
    }

    protected abstract void skillDetailsSetting(String path);

    protected abstract void setSkillType();

    protected abstract void perform(LivingEntity target);
}
