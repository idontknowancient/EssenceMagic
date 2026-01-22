package com.idk.essence.skills.types;

import com.idk.essence.Essence;
import com.idk.essence.skills.SkillManager;
import com.idk.essence.skills.SkillTemplate;
import com.idk.essence.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Optional;

public class ComboType implements SkillType {

    @Override
    public String name() {
        return "combo";
    }

    @Override
    public void execute(LivingEntity target, SkillTemplate template) {
        List<String> sequence = template.getSettings().getStringList("sequence");
        // Wait time
        long currentTickOffset = 0;

        for(String action : sequence) {
            action = action.trim().toLowerCase();

            // e.g. wait:10
            if(action.startsWith("wait:")) {
                try {
                    // e.g. 10
                    String delay = action.split(":")[1];
                    currentTickOffset += Long.parseLong(delay);
                } catch (NumberFormatException ignored) {
                }
                continue;
            }

            final String skillName = action;
            // Skill execution
            Bukkit.getScheduler().runTaskLater(Essence.getPlugin(), () -> {
                if(!target.isDead()) {
                    // No recursion allowed
                    // Conditions are based on combo skill, not respective skills
                    Optional.ofNullable(SkillManager.get(skillName)).filter(skill -> skill != template).ifPresent(
                            skill -> skill.execute(target, true));
                }
            }, currentTickOffset);
        }
    }
}
