package com.idk.essencemagic.skills;

import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.Message;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.messages.placeholders.InternalPlaceholderHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Set;

public class SkillHandler implements Listener {

    public static void initialize() {
        Skill.skills.clear();
        setSkills();
    }

    private static void setSkills() {
        Set<String> skillSet = ConfigFile.ConfigName.SKILLS.getConfig().getKeys(false);
        for(String s : skillSet) {
            Skill.skills.put(s, new Skill(s));
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        boolean left_click = action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
        boolean right_click = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
        ItemStack itemStack = e.getItem();
        if(!ItemHandler.isCustomItem(itemStack)) return;
        Item item = ItemHandler.getCorrespondingItem(itemStack);
        assert item != null;
        if(item.getSkillList().isEmpty()) return;
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
            handleSkill(player, skill.getName());
        }
    }

    public static void handleSkill(LivingEntity caster, String skillName) {
        for(SingleSkill singleSkill : Skill.skills.get(skillName).getSingleSkills().values()) {
            if(checkRequirements(caster, singleSkill)) {
                handleSingleSkill(caster, singleSkill);
                executeCosts(caster, singleSkill);
            } else SystemMessage.SKILL_REQUIREMENT_NOT_SATISFIED.send(caster);
        }
    }

    private static boolean checkRequirements(LivingEntity caster, SingleSkill singleSkill) {
        assert singleSkill.getRequirements() != null;
        if(singleSkill.getRequirements().isEmpty() || !(caster instanceof Player player))
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

    private static void executeCosts(LivingEntity caster, SingleSkill singleSkill) {
        assert singleSkill.getCosts() != null;
        if(singleSkill.getCosts().isEmpty() || !(caster instanceof Player player)) return;

        for(String cost : singleSkill.getCosts()) {
            String item = cost.substring(0, cost.indexOf(":")).trim();
            double amount = Double.parseDouble(cost.substring(cost.indexOf(":") + 1).trim());
            if(item.equalsIgnoreCase("mana"))
                PlayerData.dataMap.get(player.getName()).setMana(
                        PlayerData.dataMap.get(player.getName()).getMana() - amount);
        }
    }

    private static void handleSingleSkill(LivingEntity caster, SingleSkill singleSkill) {
        SkillType type = singleSkill.getType();
        if(type.equals(SkillType.SHOOT)) {
            shoot(caster, singleSkill);
        } else if(type.equals(SkillType.CUSTOM)) {
            custom(caster, singleSkill);
        }
    }

    private static void shoot(LivingEntity caster, SingleSkill singleSkill) {
        String projectile = singleSkill.getProjectile();
        final Vector velocity = caster.getEyeLocation().getDirection().normalize().multiply(singleSkill.getVelocity());
        if(projectile.equalsIgnoreCase("snowball")) {
            Snowball spawned = caster.launchProjectile(Snowball.class);
            spawned.setVelocity(velocity);
        } else if(projectile.equalsIgnoreCase("fireball")) {
            Fireball spawned = caster.getWorld().spawn(caster.getEyeLocation(), Fireball.class);
            spawned.setVelocity(velocity);
            spawned.setYield((float) singleSkill.getPower());
            spawned.setIsIncendiary(singleSkill.isIncendiary());
        }
    }

    private static void custom(LivingEntity caster, SingleSkill singleSkill) {

    }
}
