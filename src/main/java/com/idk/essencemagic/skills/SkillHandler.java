package com.idk.essencemagic.skills;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.messages.placeholders.InternalPlaceholderHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class SkillHandler implements Listener {

    // player singleSkill timeMillis
    private record Cooldown(Player player, SingleSkill singleSkill) {};
    private static final Map<Cooldown, Long> cooldownMap = new HashMap<>();

    public static void initialize() {
        Skill.skills.clear();
        cooldownMap.clear();
        setSkills();
    }

    private static void setSkills() {
        Set<String> skillSet = ConfigFile.ConfigName.SKILLS.getConfig().getKeys(false);
        for(String skillName : skillSet) {
            Skill.skills.put(skillName, new Skill(skillName));
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        boolean left_click = action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
        boolean right_click = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);

        ItemStack itemStack = e.getItem();
        if(itemStack == null || !ItemHandler.isCustomItem(itemStack)) return;
        Item item = ItemHandler.getCorrespondingItem(itemStack);
        if(item == null || item.getSkillList().isEmpty()) return;

        for(Skill skill : item.getSkillList()) {
            ClickType trigger = skill.getTrigger();
            if(trigger.equals(ClickType.LEFT_CLICK))
                if(!(left_click && !player.isSneaking()))
                    return;
            if(trigger.equals(ClickType.SHIFT_LEFT_CLICK))
                if(!(left_click && player.isSneaking()))
                    return;
            if(trigger.equals(ClickType.RIGHT_CLICK))
                if(!(right_click && !player.isSneaking()))
                    return;
            if(trigger.equals(ClickType.SHIFT_RIGHT_CLICK))
                if(!(right_click && player.isSneaking()))
                    return;
            handleSkill(player, skill);
        }
    }

    public static void handleSkill(LivingEntity caster, Skill skill) {
        boolean success = true;

        for(SingleSkill singleSkill : skill.getSingleSkills().values()) {
            if(!checkRequirements(caster, singleSkill)) {
                SystemMessage.SKILL_REQUIREMENT_NOT_SATISFIED.send(caster);
                return;
            }

            if(checkInCooldown(caster, singleSkill)) {
                SystemMessage.SKILL_IN_COOLDOWN.send(caster);
                return;
            }

            // probability counts non-player entities
            if(!checkProbability(singleSkill)) {
                SystemMessage.SKILL_ACTIVATION_FAILED.send(caster);
                success = false;
            }

            executeCosts(caster, singleSkill, success);
            if(!success) return;
            handleSingleSkill(caster, singleSkill);
        }
    }

    private static boolean checkRequirements(LivingEntity caster, SingleSkill singleSkill) {
        if(singleSkill == null || singleSkill.getRequirements().isEmpty() || !(caster instanceof Player player))
            return true;

        String[] operators = {">=", ">", "<=", "<", "==", "!="};

        /* satisfy all requirements */
        for(String requirement : singleSkill.getRequirements()) {
            for(int i = 0; i <= 5; i++) {
                if(requirement.contains(operators[i])) {
                    /* argument operator condition  e.g. %mana% >= 10 */
                    String argument_original = requirement.substring(0, requirement.indexOf(operators[i])).trim();
                    String condition = requirement.substring(requirement.indexOf(operators[i]) + 2).trim();
                    String argument_translated = InternalPlaceholderHandler.translatePlaceholders(argument_original, PlayerData.dataMap.get(player.getName()));

                    /* handle if the placeholder is not a custom one */
                    if(argument_translated.contains("%"))
                        argument_translated = PlaceholderAPI.setPlaceholders(player, argument_translated);

                    if(i == 0)
                        if(Double.parseDouble(argument_translated) >= Double.parseDouble(condition))
                            break;
                        else return false;
                    else if(i == 1)
                        if(Double.parseDouble(argument_translated) > Double.parseDouble(condition))
                            break;
                        else return false;
                    else if(i == 2)
                        if(Double.parseDouble(argument_translated) <= Double.parseDouble(condition))
                            break;
                        else return false;
                    else if(i == 3)
                        if(Double.parseDouble(argument_translated) < Double.parseDouble(condition))
                            break;
                        else return false;
                    else if(i == 4)
                        if(argument_translated.equalsIgnoreCase(condition))
                            break;
                        else return false;
                    else
                        if(!argument_translated.equalsIgnoreCase(condition))
                            break;
                        else return false;

                }
            }
        }
        return true;
    }

    private static boolean checkInCooldown(LivingEntity caster, SingleSkill singleSkill) {
        if(singleSkill == null || singleSkill.getCooldown() == 0 || !(caster instanceof Player player))
            return false;

        Cooldown check = new Cooldown(player, singleSkill);
        if(cooldownMap.containsKey(check)) {
            long timeElapsed = System.currentTimeMillis() - cooldownMap.get(check);
            // millisecond & second
            if(timeElapsed > singleSkill.getCooldown() * 1000) {
                cooldownMap.remove(check);
                return false;
            } else return true;
        }

        return false;
    }

    private static boolean checkProbability(SingleSkill singleSkill) {
        if(singleSkill == null || singleSkill.getProbability() == 1)
            return true;
        Random random = new Random();
        // between 0 and 1
        double probability = random.nextDouble();

        return probability < singleSkill.getProbability();
    }

    private static void executeCosts(LivingEntity caster, SingleSkill singleSkill, boolean success) {
        if(singleSkill.getCosts() == null ||singleSkill.getCosts().isEmpty() || !(caster instanceof Player player)) return;

        for(String cost : singleSkill.getCosts()) {
            String item = cost.substring(0, cost.indexOf(":")).trim();
            double amount = Double.parseDouble(cost.substring(cost.indexOf(":") + 1).trim());
            if(item.equalsIgnoreCase("mana"))
                // consume mana depending on the setting
                if(success || ConfigFile.ConfigName.MANA.getBoolean("consume-while-skill-fail"))
                    PlayerData.dataMap.get(player.getName()).setMana(
                            PlayerData.dataMap.get(player.getName()).getMana() - amount);
        }
    }

    private static void handleSingleSkill(LivingEntity caster, SingleSkill singleSkill) {
        singleSkill.perform(caster);

        //add to cooldown map
        if(singleSkill.getCooldown() > 0 && caster instanceof Player player) {
            cooldownMap.put(new Cooldown(player, singleSkill), System.currentTimeMillis());
        }
    }
}
