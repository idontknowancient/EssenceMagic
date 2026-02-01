package com.idk.essence.skills;

import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.utils.ConditionManager;
import com.idk.essence.utils.configs.EssenceConfig;
import com.idk.essence.utils.messages.Message;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.placeholders.Placeholder;
import com.idk.essence.utils.placeholders.PlaceholderProvider;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class SkillTemplate implements PlaceholderProvider {

    private final String internalName;
    @Setter private Component displayName;
    private final ItemBuilder itemBuilder;
    private SkillType type;
    /**
     * DO not allow repetition
     */
    private final Set<Trigger> triggers;
    @Setter private int cooldown;
    @Setter private double probability;
    private final Set<Target> targets;
    private final List<String> requirements;
    private final List<String> costs;
    @Setter private ConfigurationSection settings;

    public SkillTemplate(String internalName) {
        this.internalName = internalName;
        itemBuilder = new ItemBuilder(Material.STONE);

        triggers = new LinkedHashSet<>();
        targets = new LinkedHashSet<>();
        requirements = new ArrayList<>();
        costs = new ArrayList<>();
    }

    public SkillTemplate displayName(Component displayName) {
        this.displayName = displayName;
        itemBuilder.displayName(displayName);
        return this;
    }

    public SkillTemplate displayName(String displayName) {
        this.displayName = Message.parse(displayName);
        itemBuilder.displayName(displayName);
        return this;
    }

    public SkillTemplate symbolItem(ConfigurationSection symbolSection) {
        if(symbolSection == null) return this;
        ItemFactory.setSymbolItemBuilder(internalName, symbolSection, itemBuilder);
        return this;
    }

    public SkillTemplate type(SkillType type) {
        this.type = type;
        return this;
    }

    public SkillTemplate type(String typeString) {
        type = SkillManager.getType(typeString);
        return this;
    }

    public SkillTemplate triggers(Trigger... triggers) {
        this.triggers.addAll(Arrays.stream(triggers).toList());
        return this;
    }

    /**
     * Automatically handle repetition.
     */
    public SkillTemplate triggers(EssenceConfig config, String path) {
        if(config == null || path == null) return this;
        if(config.isList(path)) {
            triggers.addAll(config.getStringList(path).stream().map(Trigger::get).toList());
        } else if(config.isString(path)) {
            triggers.add(Trigger.get(config.getString(path)));
        } else
            triggers.add(Trigger.RIGHT_CLICK);
        return this;
    }

    public SkillTemplate cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public SkillTemplate probability(double probability) {
        this.probability = probability;
        return this;
    }

    public SkillTemplate targets(Target... targets) {
        this.targets.addAll(Arrays.stream(targets).toList());
        return this;
    }

    /**
     * Automatically handle repetition.
     */
    public SkillTemplate targets(EssenceConfig config, String path) {
        if(config == null || path == null) return this;
        if(config.isList(path)) {
            targets.addAll(config.getStringList(path).stream().map(Target::get).toList());
        } else if(config.isString(path)) {
            targets.add(Target.get(config.getString(path)));
        } else
            targets.add(Target.SELF);
        return this;
    }

    private SkillTemplate applyStringList(List<String> stringList, EssenceConfig config, String path) {
        if(config == null || path == null) return this;
        if(config.isList(path)) {
            stringList.addAll(config.getStringList(path));
        }  else if(config.isString(path)) {
            stringList.add(config.getString(path));
        }
        return this;
    }

    public SkillTemplate requirements(String... requirements) {
        this.requirements.addAll(Arrays.stream(requirements).toList());
        return this;
    }

    public SkillTemplate requirements(EssenceConfig config, String path) {
        return applyStringList(requirements, config, path);
    }

    public SkillTemplate costs(String... costs) {
        this.costs.addAll(Arrays.stream(costs).toList());
        return this;
    }

    public SkillTemplate costs(EssenceConfig config, String path) {
        return applyStringList(costs, config, path);
    }

    public SkillTemplate settings(ConfigurationSection settings) {
        this.settings = settings;
        return this;
    }

    public void applyLore() {
        itemBuilder.addLore("&fType: " + type.name());
        itemBuilder.addLore("&fTriggers:");
        itemBuilder.addLore("&7" + triggers.stream().map(Trigger::toString).toList());
        itemBuilder.addLore("&fCooldown: " +  cooldown);
        itemBuilder.addLore("&fProbability: " +  probability);
        itemBuilder.addLore("&fTargets:");
        itemBuilder.addLore("&7" + targets.stream().map(Target::toString).toList());
        itemBuilder.addLore("&fRequirements:");
        itemBuilder.addLore("&7" + requirements);
        itemBuilder.addLore("&fCosts:");
        itemBuilder.addLore("&7" + costs);
        itemBuilder.addLore("&fSettings:");
        for(String key : settings.getKeys(false)) {
            if(settings.isString(key))
                itemBuilder.addLore("&7" + key + ": " + settings.getString(key));
            else if(settings.isBoolean(key))
                itemBuilder.addLore("&7" + key + ": " + settings.getBoolean(key));
            else if(settings.isDouble(key))
                itemBuilder.addLore("&7" + key + ": " + settings.getDouble(key));
            else if(settings.isInt(key))
                itemBuilder.addLore("&7" + key + ": " + settings.getInt(key));
            else if(settings.isList(key))
                itemBuilder.addLore("&7" + key + ": " + settings.getStringList(key));
        }
    }

    public static SkillTemplate create(String internalName, EssenceConfig config) {
        SkillTemplate template = new SkillTemplate(internalName);
        template.displayName(config.outString(internalName + ".display-name"))
                .symbolItem(config.getConfigurationSection(internalName + ".symbol-item"))
                .type(config.getString(internalName + ".type"))
                .triggers(config, internalName + ".triggers")
                .cooldown(Math.max(config.getInteger(internalName + ".cooldown", 0), 0))
                .probability(Math.clamp(config.getDouble(internalName + ".probability", 1d), 0d, 1d))
                .targets(config, internalName + ".targets")
                .requirements(config, internalName + ".requirements")
                .costs(config, internalName + ".costs")
                .settings(config.getConfigurationSection(internalName + ".settings"))
                .applyLore();

        return template;
    }

    /**
     * Use when event involves only the caster.
     * @param caster skill caster
     * @param forced means casting a skill forcibly
     * @return whether the execution is successful
     */
    public boolean execute(LivingEntity caster, boolean forced) {
        return execute(caster, forced, null);
    }


    /**
     * Use when event involves an event.
     * @param caster skill caster
     * @param forced means casting a skill forcibly
     * @return whether the execution is successful
     */
    public boolean execute(LivingEntity caster, boolean forced, Event event) {
        if(forced || preCheck(caster)) {
            Set<LivingEntity> allTargets = targets.stream()
                    // Flat multiple sets into a stream
                    .flatMap(target -> target.getTargets(caster, event).stream())
                    .filter(target -> !target.isDead())
                    // Collect to a set and remove repetition
                    .collect(Collectors.toSet());
            allTargets.forEach(target -> type.execute(target, this));
            SkillManager.updateCooldown(caster.getUniqueId(), this);
            return true;
        }
        return false;
    }

    /**
     * Check cooldown, requirements, probability, and costs.
     * @return whether check success
     */
    private boolean preCheck(LivingEntity caster) {
        if(checkInCooldown(caster)) {
            SystemMessage.SKILL_IN_COOLDOWN.send(caster);
            return false;
        }

        if(!checkRequirements(caster)) {
            SystemMessage.SKILL_REQUIREMENT_NOT_SATISFIED.send(caster);
            return false;
        }

        boolean success = true;
        // Probability counts non-player entities
        if(!checkProbability()) {
            SystemMessage.SKILL_ACTIVATION_FAILED.send(caster);
            success = false;
        }

        applyCosts(caster, success);

        return success;
    }

    private boolean checkInCooldown(LivingEntity caster) {
        if(cooldown == 0) return false;
        return SkillManager.inCooldown(caster.getUniqueId(), this);
    }

    private boolean checkRequirements(LivingEntity caster) {
        if(requirements.isEmpty() || !(caster instanceof Player player))
            return true;
        for(String requirement : requirements) {
            if(!ConditionManager.checkRequirement(requirement, player))
                return false;
        }
        return true;
    }

    private boolean checkProbability() {
        if(this.probability == 1d)
            return true;
        if(this.probability == 0d)
            return false;
        Random random = new Random();
        // Between 0 and 1
        double probability = random.nextDouble();

        return probability <= this.probability;
    }

    private void applyCosts(LivingEntity caster, boolean success) {
        if(costs.isEmpty() || !(caster instanceof Player player)) return;
        for(String cost : costs) {
            ConditionManager.applyCost(cost, player, success);
        }
    }

    @Override
    public Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new LinkedHashMap<>();
        placeholders.put(Placeholder.SKILL_NAME.name, internalName);
        placeholders.put(Placeholder.SKILL_DISPLAY_NAME.name, Message.serialize(displayName));
        return placeholders;
    }
}
