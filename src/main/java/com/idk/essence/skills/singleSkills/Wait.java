package com.idk.essence.skills.singleSkills;

import com.idk.essence.skills.SingleSkill;
import com.idk.essence.skills.SkillType;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Getter
public class Wait extends SingleSkill {

    private int tick;

    public Wait(@NotNull String skillName) {
        super(skillName);
        skillDetailsSetting(skillName);
        setSkillType();
    }

    private Wait(String skillName, @NotNull String singleSkillName) {
        super(skillName, singleSkillName);
        skillDetailsSetting(skillName + ".skills." + singleSkillName);
        setSkillType();
    }

    @Override
    protected void skillDetailsSetting(String path) {
    }

    @Override
    protected void setSkillType() {
        skillType = SkillType.WAIT;
    }

    @Override
    protected void perform(LivingEntity target) {
    }
}
