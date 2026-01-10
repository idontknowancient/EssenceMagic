package com.idk.essence.skills.singleSkills;

import com.idk.essence.skills.SingleSkill;
import com.idk.essence.skills.SkillType;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

@Getter
public class Potion extends SingleSkill {

    private String effect;

    // in ticks
    private int duration;

    // 0 is level 1
    private int level;

    private boolean particles;

    public Potion(@NotNull String skillName) {
        super(skillName);
        skillDetailsSetting(skillName);
        setSkillType();
    }

    public Potion(String skillName, @NotNull String singleSkillName) {
        super(skillName, singleSkillName);
        skillDetailsSetting(skillName + ".skills." + singleSkillName);
        setSkillType();
    }

    @Override
    protected void skillDetailsSetting(String path) {
        ConfigManager.ConfigDefaultFile cs = ConfigManager.ConfigDefaultFile.SKILLS;

        // set potion effect
        if(cs.isString(path + ".effect"))
            effect = cs.getString(path + ".effect");
        else
            effect = "absorption";
        getInfo().add("  &7Effect: " + effect);

        // set potion duration (default to 20 ticks)
        if(!cs.isInteger(path + ".duration") || cs.getInteger(path + ".duration") <= 0)
            duration = 20;
        else
            duration = cs.getInteger(path + ".duration");
        getInfo().add("  &7Duration: " + duration);

        // set potion level (default to 0 / level 1)
        if(!cs.isInteger(path + ".level") || cs.getInteger(path + ".level") < 0)
            level = 0;
        else
            level = cs.getInteger(path + ".level");
        getInfo().add("  &7Level: " + level);

        // set potion particles (default to true)
        if(cs.isBoolean(path + ".particles"))
            particles = cs.getBoolean(path + ".particles");
        else
            particles = true;
        getInfo().add("  &7Particles: " + particles);
    }

    @Override
    protected void setSkillType() {
        skillType = SkillType.POTION;
    }

    @Override
    protected void perform(LivingEntity target) {
        PotionEffectType type = Registry.EFFECT.get(NamespacedKey.minecraft(effect));
        if(type == null) return;

        PotionEffect potionEffect = new PotionEffect(type, getDuration(), getLevel(), false, isParticles());
        potionEffect.apply(target);
    }
}
