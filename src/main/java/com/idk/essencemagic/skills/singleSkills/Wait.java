package com.idk.essencemagic.skills.singleSkills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.skills.SingleSkill;
import com.idk.essencemagic.skills.SkillType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

@Getter
public class Wait extends SingleSkill {

    private int tick;

    public Wait(@NotNull String skillName) {
        super(skillName);
        skillDetailsSetting(skillName);
        setSkillType();
    }

    public Wait(String skillName, @NotNull String tick) {
        super(skillName, tick);
        skillDetailsSetting(tick);
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
