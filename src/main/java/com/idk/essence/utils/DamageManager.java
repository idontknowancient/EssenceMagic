package com.idk.essence.utils;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DamageManager implements Listener {

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        event.setDamage(finalDamage(event));
    }

    public static double finalDamage(EntityDamageByEntityEvent event) {
        double original = event.getDamage();
        double multiplier = 1;
        LivingEntity attacker = getAttacker(event);
        LivingEntity target = getTarget(event);

        if(attacker == null || target == null) return original;
        multiplier *= elementMultiplier(attacker, target);
        attacker.sendMessage(Util.colorize("&7attack damage &bx" + multiplier + "&7, " + original * multiplier));

        return original * multiplier;
    }

    private static double elementMultiplier(LivingEntity attacker, LivingEntity target) {
        double multiplier = 1;
        Element element = ElementFactory.getOrDefault(
                Optional.ofNullable(attacker.getEquipment()).map(EntityEquipment::getItemInMainHand).orElse(null));
        // Count the appearance of each element in the armor
        Map<Element, Long> appearance = new HashMap<>();
        if(target.getEquipment() == null) {
            appearance.put(ElementFactory.getDefault(), 4L);
        } else {
            appearance = Arrays.stream(target.getEquipment().getArmorContents())
                    .map(ElementFactory::getOrDefault)
                    .collect(Collectors.groupingBy(targetElement -> targetElement, Collectors.counting()));
        }

        for(Element targetElement : appearance.keySet()) {
            double singleMultiplier = element.getDamageMultiplier(targetElement);
            int appearanceCount = appearance.get(targetElement).intValue();
            // If A counters B, damage will be more but the marginal damage is decreasing.
            if(singleMultiplier >= 1) {
                multiplier *= Math.pow(singleMultiplier, harmonic(appearanceCount));
            }
            // If A is countered by B, damage will be less.
            else {
                multiplier *= Math.pow(singleMultiplier, appearanceCount / 1.75);
            }
        }

        return multiplier;
    }

    /**
     * @return entity who attacked or shot
     */
    @Nullable
    public static LivingEntity getAttacker(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if(damager instanceof LivingEntity attacker)
            return attacker;
        else if(damager instanceof Projectile projectile && projectile.getShooter() instanceof LivingEntity attacker)
            return attacker;
        return null;
    }

    /**
     * @return entity who attacked or shot
     */
    @Nullable
    public static LivingEntity getTarget(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity target)
            return target;
        return null;
    }

    /**
     * @return Harmonic series sum of n (1/1+1/2+1/3+...+1/n)
     */
    public static double harmonic(int n) {
        double sum = 0.0;
        for(int i = 1; i <= n; i++) {
            sum += 1.0 / i;
        }
        return sum;
    }
}
