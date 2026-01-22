package com.idk.essence.skills.types;

import com.idk.essence.skills.SkillTemplate;
import com.idk.essence.skills.SkillType;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class PotionType implements SkillType {

    @Override
    public String name() {
        return "potion";
    }

    @Override
    public void execute(LivingEntity target, SkillTemplate template) {
        ConfigurationSection settings = template.getSettings();
        String effect = settings.getString("effect", "speed");
        PotionEffectType type = Optional.ofNullable(Registry.EFFECT.get(NamespacedKey.minecraft(effect)))
                .orElse(PotionEffectType.SPEED);
        // In ticks
        int duration = Math.max(settings.getInt("duration", 20), 20);
        int level = Math.max(settings.getInt("level", 0), 0);
        boolean particles = settings.getBoolean("particles", true);

        PotionEffect potionEffect = new PotionEffect(type, duration, level, false, particles);
        potionEffect.apply(target);
    }
}
