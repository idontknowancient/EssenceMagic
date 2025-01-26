package com.idk.essencemagic.skills.singleSkills;

import com.idk.essencemagic.skills.SingleSkill;
import com.idk.essencemagic.skills.SkillType;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

@Getter
public class Shoot extends SingleSkill {

    private String projectile;

    private double velocity;

    private double power;

    private boolean incendiary;

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
        ConfigFile.ConfigName cs = ConfigFile.ConfigName.SKILLS;

        // set shoot projectile (default to snowball)
        if(cs.isString(path + ".projectile"))
            projectile = cs.getString(path + ".projectile");
        else
            projectile = "snowball";
        getInfo().add("  &7Projectile: " + projectile);

        // set shoot velocity (default to 1)
        if(!cs.isDouble(path + ".velocity") || cs.getDouble(path + ".velocity") < 0)
            if(!cs.isInteger(path + ".velocity") || cs.getInteger(path + ".velocity") < 0)
                velocity = 1;
            else
                velocity = cs.getInteger(path + ".velocity");
        else
            velocity = cs.getDouble(path + ".velocity");
        getInfo().add("  &7Velocity: " + velocity);

        // set shoot power (default to 1)
        if(!cs.isDouble(path + ".power") || cs.getDouble(path + ".power") < 0)
            if(!cs.isInteger(path + ".power") || cs.getInteger(path + ".power") < 0)
                power = 1;
            else
                power = cs.getInteger(path + ".power");
        else
            power = cs.getDouble(path + ".power");
        getInfo().add("  &7Power: " + power);

        // set shoot incendiary (default to true)
        if(cs.isBoolean(path + ".incendiary"))
            incendiary = cs.getBoolean(path + ".incendiary");
        else
            incendiary = true;
        getInfo().add("  &7Incendiary: " + incendiary);
    }

    @Override
    protected void setSkillType() {
        skillType = SkillType.SHOOT;
    }

    @Override
    public void perform(LivingEntity target) {
        final Vector velocity = target.getEyeLocation().getDirection().normalize().multiply(getVelocity());

         if(getProjectile().equalsIgnoreCase("snowball")) {
            Snowball spawned = target.launchProjectile(Snowball.class);
            spawned.setVelocity(velocity);
        } else if(getProjectile().equalsIgnoreCase("fireball")) {
            Fireball spawned = target.getWorld().spawn(target.getEyeLocation(), Fireball.class);
            spawned.setVelocity(velocity);
            spawned.setYield((float) getPower());
            spawned.setIsIncendiary(isIncendiary());
         }
    }
}
