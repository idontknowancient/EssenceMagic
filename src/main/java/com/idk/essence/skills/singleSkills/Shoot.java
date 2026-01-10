package com.idk.essence.skills.singleSkills;

import com.idk.essence.skills.SingleSkill;
import com.idk.essence.skills.SkillType;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

@Getter
public class Shoot extends SingleSkill {

    // universal
    private String projectile;

    private Component customName;

    private boolean customNameVisible;

    private boolean glowing;

    private boolean gravity;

    private double velocity;

    // for snowball
    private ItemStack itemStack;

    // for fireball
    private boolean incendiary;

    private double power;

    public Shoot(String skillName) {
        super(skillName);
        skillDetailsSetting(skillName);
        setSkillType();
    }

    public Shoot(String skillName, String singleSkillName) {
        super(skillName, singleSkillName);
        skillDetailsSetting(skillName + ".skills." + singleSkillName);
        setSkillType();
    }

    @Override
    protected void skillDetailsSetting(String path) {
        ConfigManager.ConfigDefaultFile cs = ConfigManager.ConfigDefaultFile.SKILLS;

        // universal
        // set projectile (default to snowball)
        if(cs.isString(path + ".projectile"))
            projectile = cs.getString(path + ".projectile");
        else
            projectile = "snowball";
        getInfo().add("  &7Projectile: " + projectile);

        // set custom name (default to "")
        if(cs.isString(path + ".name"))
            customName = cs.outString(path + ".name");
        else
            customName = Component.text("");
        getInfo().add("  &7Name: " + customName);

        // set custom name visible (default to false)
        if(cs.isBoolean(path + ".name-visible"))
            customNameVisible = cs.getBoolean(path + ".name-visible");
        else
            customNameVisible = false;
        getInfo().add("  &7Name Visible: " + customNameVisible);

        // set glowing (default to false)
        if(cs.isBoolean(path + ".glowing"))
            glowing = cs.getBoolean(path + ".glowing");
        else
            glowing = false;
        getInfo().add("  &7Glowing: " + glowing);

        // set gravity (default to true)
        if(cs.isBoolean(path + ".gravity"))
            gravity = cs.getBoolean(path + ".gravity");
        else
            gravity = true;
        getInfo().add("  &7Gravity: " + gravity);

        // set velocity (default to 1)
        if(!cs.isDouble(path + ".velocity") || cs.getDouble(path + ".velocity") < 0)
            if(!cs.isInteger(path + ".velocity") || cs.getInteger(path + ".velocity") < 0)
                velocity = 1;
            else
                velocity = cs.getInteger(path + ".velocity");
        else
            velocity = cs.getDouble(path + ".velocity");
        getInfo().add("  &7Velocity: " + velocity);

        // for snowball
        // set snowball item stack (default to snowball)
        if(cs.isString(path + ".item"))
            itemStack = new ItemStack(Material.valueOf(cs.getString(path + ".item").toUpperCase()));
        else
            itemStack = new ItemStack(Material.SNOWBALL);
        getInfo().add("  &7Item: " + itemStack.getType().name());

        // for fireball
        // set fireball incendiary (default to true)
        if(cs.isBoolean(path + ".incendiary"))
            incendiary = cs.getBoolean(path + ".incendiary");
        else
            incendiary = true;
        getInfo().add("  &7Incendiary: " + incendiary);

        // set fireball power (default to 1)
        if(!cs.isDouble(path + ".power") || cs.getDouble(path + ".power") < 0)
            if(!cs.isInteger(path + ".power") || cs.getInteger(path + ".power") < 0)
                power = 1;
            else
                power = cs.getInteger(path + ".power");
        else
            power = cs.getDouble(path + ".power");
        getInfo().add("  &7Power: " + power);
    }

    @Override
    protected void setSkillType() {
        skillType = SkillType.SHOOT;
    }

    @Override
    public void perform(LivingEntity target) {
         if(getProjectile().equalsIgnoreCase("snowball")) {
            Snowball spawned = target.launchProjectile(Snowball.class);
            setProjectile(target, spawned);
            spawned.setItem(getItemStack());
        } else if(getProjectile().equalsIgnoreCase("fireball")) {
            Fireball spawned = target.launchProjectile(Fireball.class);
            setProjectile(target, spawned);
            spawned.setIsIncendiary(isIncendiary());
            spawned.setYield((float) getPower());
         } else {
             // default to snowball
             Snowball spawned = target.launchProjectile(Snowball.class);
             setProjectile(target, spawned);
         }
    }

    private void setProjectile(LivingEntity target, Projectile spawned) {
        final Vector velocity = target.getEyeLocation().getDirection().normalize().multiply(getVelocity());

        spawned.customName(getCustomName());
        spawned.setCustomNameVisible(isCustomNameVisible());
        spawned.setGlowing(isGlowing());
        spawned.setGravity(isGravity());
        spawned.setVelocity(velocity);
    }
}
