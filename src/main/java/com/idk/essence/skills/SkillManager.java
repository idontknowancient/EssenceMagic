package com.idk.essence.skills;

import com.idk.essence.items.ItemFactory;
import com.idk.essence.utils.CustomKey;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SkillManager implements Listener {

    /**
     * Used to register listener
     */
    @Getter private static final SkillManager instance = new SkillManager();

    private static final Map<String, SkillTemplate> skillTemplates = new HashMap<>();

    private static final Map<String, SkillType> skillTypes = new HashMap<>();

    /**
     * UUID of caster, internal name of skill, system time millis
     */
    private static final Map<UUID, Map<String, Long>> skillCooldowns = new HashMap<>();

    private SkillManager() {}

    public static void initialize() {
        skillTemplates.clear();
        skillTypes.clear();
        skillCooldowns.clear();
        SkillType.registerAll(skillTypes);
        ConfigManager.ConfigFolder.SKILLS.load(SkillManager::register);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player caster = event.getPlayer();
        List<SkillTemplate> templates = get(event.getItem());
        if(templates == null) return;

        for(SkillTemplate template : templates) {
            // At least one trigger is satisfied
            boolean isMatched = template.getTriggers().stream()
                    .anyMatch(trigger -> trigger.matches(event));
            if(isMatched) {
                template.execute(caster, false, event);
            }
        }
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity caster)) return;
        List<SkillTemplate> templates = get(caster);
        if(templates == null) return;

        for(SkillTemplate template : templates) {
            // At least one trigger is satisfied
            boolean isMatched = template.getTriggers().stream()
                    .anyMatch(trigger -> trigger.matches(event));
            if(isMatched) {
                template.execute(caster, false, event);
            }
        }
    }

    public static boolean has(String internalName) {
        return skillTemplates.containsKey(internalName);
    }

    /**
     * Get a skill template from a string.
     * @param internalName the internal name of the item
     * @return the corresponding skill
     */
    @Nullable
    public static SkillTemplate get(String internalName) {
        return skillTemplates.get(internalName);
    }

    /**
     * Get all skill skillTemplates from an item.
     */
    @Nullable
    public static List<SkillTemplate> get(ItemStack item) {
        if(!ItemFactory.isCustom(item)) return null;
        String skill = item.getItemMeta().getPersistentDataContainer().get(CustomKey.getSkillKey(), PersistentDataType.STRING);
        if(skill == null) return null;
        // Ignore suffix ";"
        // e.g. a;b;c; -> [a, b, c]
        return Arrays.stream(skill.split(";")).map(SkillManager::get).filter(Objects::nonNull).toList();
    }

    /**
     * Get all skill skillTemplates from an entity's main hand.
     */
    @Nullable
    public static List<SkillTemplate> get(LivingEntity caster) {
        ItemStack item = Optional.ofNullable(caster.getEquipment()).map(EntityEquipment::getItemInMainHand).orElse(null);
        return get(item);
    }

    /**
     * Get skill type from a string, and the default is projectile.
     */
    public static SkillType getType(String typeName) {
        return Optional.ofNullable(skillTypes.get(typeName)).orElse(skillTypes.get("projectile"));
    }

    public static Collection<String> getAllKeys() {
        return skillTemplates.keySet();
    }

    public static Collection<SkillTemplate> getAll() {
        return skillTemplates.values();
    }

    /**
     * Check if a specific skill is in cooldown.
     */
    public static boolean inCooldown(UUID casterUUID, SkillTemplate template) {
        skillCooldowns.putIfAbsent(casterUUID, new HashMap<>());
        if(!skillCooldowns.get(casterUUID).containsKey(template.getInternalName())) return false;

        long timeElapsed = System.currentTimeMillis() - skillCooldowns.get(casterUUID).get(template.getInternalName());
        // Millisecond & tick
        if(timeElapsed > template.getCooldown() * 50L) {
            skillCooldowns.get(casterUUID).remove(template.getInternalName());
            return false;
        } else
            return true;
    }

    /**
     * Put currentTimeMillis to cooldown map.
     */
    public static void updateCooldown(UUID casterUUID, SkillTemplate template) {
        skillCooldowns.putIfAbsent(casterUUID, new HashMap<>());
        if(template.getCooldown() == 0) return;
        skillCooldowns.get(casterUUID).put(template.getInternalName(), System.currentTimeMillis());
    }

    /**
     * Register a skill.
     * @param internalName the internal name of the skill
     */
    public static void register(String internalName, EssenceConfig config) {
        if(!config.isConfigurationSection(internalName) || skillTemplates.containsKey(internalName)) return;

        SkillTemplate template = SkillTemplate.create(internalName, config);
        skillTemplates.put(internalName, template);
    }
}
