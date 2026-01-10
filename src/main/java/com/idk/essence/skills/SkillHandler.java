package com.idk.essence.skills;

import com.idk.essence.Essence;
import com.idk.essence.items.ItemFactory;
import com.idk.essence.players.PlayerData;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
        Set<String> skillSet = ConfigManager.ConfigDefaultFile.SKILLS.getConfig().getKeys(false);
        for(String skillName : skillSet) {
            Skill.skills.put(skillName, new Skill(skillName));
        }
    }

    // handle if the trigger is clicking
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        boolean left_click = action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
        boolean right_click = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);

        ItemStack itemStack = event.getItem();
        if(!ItemFactory.isCustom(itemStack)) return;
//        if(item == null || item.getSkillList().isEmpty()) return;

//        for(Skill skill : item.getSkillList()) {
//            List<Trigger> trigger = skill.getTriggers();
//            // use array so that we can adjust variable in runnable
//            int[] count = {0};
//
//            // one entity
//            if(trigger.contains(Trigger.LEFT_CLICK))
//                if(left_click && !player.isSneaking())
//                    handleSkill(player, skill, count, false);
//            if(trigger.contains(Trigger.SHIFT_LEFT_CLICK))
//                if(left_click && player.isSneaking())
//                    handleSkill(player, skill, count, false);
//            if(trigger.contains(Trigger.RIGHT_CLICK))
//                if(right_click && !player.isSneaking())
//                    handleSkill(player, skill, count, false);
//            if(trigger.contains(Trigger.SHIFT_RIGHT_CLICK))
//                if(right_click && player.isSneaking())
//                    handleSkill(player, skill, count, false);
//        }
    }

    // handle if the trigger is attack
    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity caster)) return;
        if(!(event.getEntity() instanceof LivingEntity object)) return;

        if(caster.getEquipment() == null) return;
        ItemStack itemStack = caster.getEquipment().getItemInMainHand();
        if(!ItemFactory.isHoldingCustom(caster)) return;
//        if(item == null || item.getSkillList().isEmpty()) return;
//
//        for(Skill skill : item.getSkillList()) {
//            List<Trigger> triggers = skill.getTriggers();
//            // two entities
//            if(triggers.contains(Trigger.ATTACK))
//                handleSkill(caster, object, skill, new int[]{0}, false);
//        }
    }

    // use when event involves one entity
    // use int[] so that we can adjust variable in runnable, forced means casting a skill forcibly
    public static void handleSkill(LivingEntity caster, Skill skill, int[] count, boolean forced) {
        // check if the recursion times surpass designated orders
        if(count[0] >= skill.getOrders().size()) return;

        int waitTick;
        String order = skill.getOrders().get(count[0]);
        SingleSkill singleSkill;

        // get the single skill in orders
        if(order.startsWith("wait ")) {
            // extract the tick e.g. - wait 20
            waitTick = Integer.parseInt(order.split(" ")[1]);
            // get the only existed wait
            singleSkill = skill.getSingleSkills().get("wait");
        } else {
            waitTick = 0;
            singleSkill = skill.getSingleSkills().get(order);
        }

        // if forced, skipped the pre handle
        // move to the next single skill if the current one is not a wait and doesn't satisfy all conditions
        if(!forced && !(singleSkill.getSkillType().equals(SkillType.WAIT) ||
                        singleSkillPreHandle(caster, singleSkill))) {
            ++count[0];
            handleSkill(caster, skill, count, forced);
            return;
        }

        // add to cooldown map
        if(singleSkill.getCooldown() > 0 && caster instanceof Player player)
            cooldownMap.put(new Cooldown(player, singleSkill), System.currentTimeMillis());

        // handle the single skill
        ++count[0];
        Bukkit.getScheduler().runTaskLater(Essence.getPlugin(), ()->{
            // if is wait, wait for specific ticks and go to the next single skill
            if(singleSkill.getSkillType().equals(SkillType.WAIT))
                handleSkill(caster, skill, count, forced);
            // if is not wait, directly go to the next single skill (waitTick = 0)
            else {
                // with all targets performing a single skill
                for (LivingEntity target : getTargets(caster, singleSkill)) {
                    singleSkill.perform(target);
                }
                handleSkill(caster, skill, count, forced);
            }
        }, waitTick);
    }

    // use when event involves two entities
    // use int[] so that we can adjust variable in runnable, forced means casting a skill forcibly
    public static void handleSkill(LivingEntity caster, LivingEntity object, Skill skill, int[] count, boolean forced) {
        // check if the recursion times surpass designated orders
        if(count[0] >= skill.getOrders().size()) return;

        int waitTick;
        String order = skill.getOrders().get(count[0]);
        SingleSkill singleSkill;

        // get the single skill in orders
        if(order.startsWith("wait ")) {
            // extract the tick e.g. - wait 20
            waitTick = Integer.parseInt(order.split(" ")[1]);
            // get the only existed wait
            singleSkill = skill.getSingleSkills().get("wait");
        } else {
            waitTick = 0;
            singleSkill = skill.getSingleSkills().get(order);
        }

        // if forced, skipped the pre handle
        // move to the next single skill if the current one is not a wait and doesn't satisfy all conditions
        if(!forced && !(singleSkill.getSkillType().equals(SkillType.WAIT) ||
                singleSkillPreHandle(caster, singleSkill))) {
            ++count[0];
            handleSkill(caster, skill, count, forced);
            return;
        }

        // add to cooldown map
        if(singleSkill.getCooldown() > 0 && caster instanceof Player player)
            cooldownMap.put(new Cooldown(player, singleSkill), System.currentTimeMillis());

        // handle the single skill
        ++count[0];
        Bukkit.getScheduler().runTaskLater(Essence.getPlugin(), ()->{
            // if is wait, wait for specific ticks and go to the next single skill
            if(singleSkill.getSkillType().equals(SkillType.WAIT))
                handleSkill(caster, skill, count, forced);
            // if is not wait, directly go to the next single skill (waitTick = 0)
            else {
                // with all targets performing a single skill
                for (LivingEntity target : getTargets(caster, object, singleSkill)) {
                    singleSkill.perform(target);
                }
                handleSkill(caster, skill, count, forced);
            }
        }, waitTick);
    }

    private static boolean singleSkillPreHandle(LivingEntity caster, SingleSkill singleSkill) {
        if(!checkRequirements(caster, singleSkill)) {
            SystemMessage.SKILL_REQUIREMENT_NOT_SATISFIED.send(caster);
            return false;
        }

        if(checkInCooldown(caster, singleSkill)) {
            SystemMessage.SKILL_IN_COOLDOWN.send(caster);
            return false;
        }

        boolean success = true;
        // probability counts non-player entities
        if(!checkProbability(singleSkill)) {
            SystemMessage.SKILL_ACTIVATION_FAILED.send(caster);
            success = false;
        }

        executeCosts(caster, singleSkill, success);
        return success;
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
                    String argument_translated = PlaceholderManager.translate(argument_original, PlayerData.dataMap.get(player.getName()));

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
            // millisecond & tick
            if(timeElapsed > singleSkill.getCooldown() * 50) {
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
        if(singleSkill == null || singleSkill.getCosts() == null
                || singleSkill.getCosts().isEmpty() || !(caster instanceof Player player)) return;

        for(String cost : singleSkill.getCosts()) {
            String item = cost.substring(0, cost.indexOf(":")).trim();
            double amount = Double.parseDouble(cost.substring(cost.indexOf(":") + 1).trim());
            if(item.equalsIgnoreCase("mana"))
                // consume mana depending on the setting
                if(success || ConfigManager.ConfigDefaultFile.MANA.getBoolean("consume-while-skill-fail"))
                    PlayerData.dataMap.get(player.getName()).setMana(
                            PlayerData.dataMap.get(player.getName()).getMana() - amount);
        }
    }

    // use when event involves one entity
    private static List<LivingEntity> getTargets(LivingEntity caster, SingleSkill singleSkill) {
        List<LivingEntity> targets = new ArrayList<>();
        if(singleSkill == null) return targets;

        for(String target : singleSkill.getTargets()) {
            if(target.equalsIgnoreCase("self"))
                targets.add(caster);
        }

        return targets;
    }

    // use when event involves two entities
    private static List<LivingEntity> getTargets(LivingEntity caster, LivingEntity object, SingleSkill singleSkill) {
        List<LivingEntity> targets = new ArrayList<>();

        for(String target : singleSkill.getTargets()) {
            if(target.equalsIgnoreCase("self"))
                targets.add(caster);
            if(target.equalsIgnoreCase("player") && object instanceof Player)
                targets.add(object);
            if(target.equalsIgnoreCase("mob") && !(object instanceof Player))
                targets.add(object);
        }

        return targets;
    }
}
