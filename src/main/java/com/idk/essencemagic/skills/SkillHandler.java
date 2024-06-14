package com.idk.essencemagic.skills;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Set;

public class SkillHandler implements Listener {

    public static void initialize() {
        Skill.skills.clear();
        setSkills();
    }

    public static void setSkills() {
        Set<String> skillSet = ConfigFile.ConfigName.SKILLS.getConfig().getKeys(false);
        for(String s : skillSet) {
            Skill.skills.put(s, new Skill(s));
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack itemStack = e.getItem();
        if(!ItemHandler.isCustomItem(itemStack)) return;
        Item item = ItemHandler.getCorrespondingItem(itemStack);
        assert item != null;
        if(item.getSkillList().isEmpty()) return;
        for(Skill skill : item.getSkillList()) {
            ClickType trigger = skill.getTrigger();
            if(trigger.equals(ClickType.LEFT_CLICK))
                if(!((action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))
                        && !player.isSneaking()))
                    return;
            if(trigger.equals(ClickType.SHIFT_LEFT_CLICK))
                if(!((action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))
                        && player.isSneaking()))
                    return;
            if(trigger.equals(ClickType.RIGHT_CLICK))
                if(!((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))
                        && !player.isSneaking()))
                    return;
            if(trigger.equals(ClickType.SHIFT_RIGHT_CLICK))
                if(!((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))
                        && player.isSneaking()))
                    return;
            handleSkill(player, skill.getName());
        }
    }

    public static void handleSkill(LivingEntity caster, String skillName) {
        for(SingleSkill singleSkill : Skill.skills.get(skillName).getSingleSkills().values()) {
            handleSingleSkill(caster, singleSkill);
        }
    }

    public static void handleSingleSkill(LivingEntity caster, SingleSkill singleSkill) {
        SkillType type = singleSkill.getType();
        if(type.equals(SkillType.SHOOT)) {
            shoot(caster, singleSkill);
        } else if(type.equals(SkillType.CUSTOM)) {
            custom(caster, singleSkill);
        }
    }

    private static void shoot(LivingEntity caster, SingleSkill singleSkill) {
        String projectile = singleSkill.getProjectile();
        final Entity spawned;
        final Vector velocity = caster.getEyeLocation().getDirection().normalize().multiply(singleSkill.getVelocity());
        if(projectile.equalsIgnoreCase("snowball")) {
            spawned = caster.launchProjectile(Snowball.class);
            spawned.setVelocity(velocity);
        } else if(projectile.equalsIgnoreCase("fireball")) {
            //straight?
            spawned = caster.launchProjectile(Fireball.class);
            spawned.setVelocity(velocity);
            new BukkitRunnable() {
                int centiSec = 0;
                @Override
                public void run() {
                    if(spawned.isDead()) {
                        this.cancel();
                        return;
                    }
                    if(centiSec >= 30) {
                        this.cancel();
                        spawned.remove();
                    } else {
                        spawned.setVelocity(velocity);
                    }
                    centiSec++;
                }
            }.runTaskTimer(EssenceMagic.getPlugin(), 0, 1/2L);
        }
    }

    private static void custom(LivingEntity caster, SingleSkill singleSkill) {

    }
}
