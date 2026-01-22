package com.idk.essence.skills;

import com.idk.essence.skills.types.ComboType;
import com.idk.essence.skills.types.PotionType;
import com.idk.essence.skills.types.ProjectileType;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public interface SkillType {

    static void registerAll(Map<String, SkillType> skillTypes) {
        skillTypes.put("combo", new ComboType());
        skillTypes.put("potion", new PotionType());
        skillTypes.put("projectile", new ProjectileType());
    }

    String name();

    void execute(LivingEntity target, SkillTemplate template);
}
