package com.idk.essence.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum Trigger {

    LEFT_CLICK("left_click", (p, a) -> a.isLeftClick()),
    SHIFT_LEFT_CLICK("shift_left_click", (p, a) -> p.isSneaking() && a.isLeftClick()),
    RIGHT_CLICK("right_click", (p, a) -> a.isRightClick()),
    SHIFT_RIGHT_CLICK("shift_right_click", (p, a) -> p.isSneaking() && a.isRightClick()),
    ATTACK("attack", a -> true),
    ;

    public final String name;
    private final BiPredicate<Player, Action> actionPredicate;
    private final Predicate<EntityEvent> interactPredicate;

    Trigger(String name, Predicate<EntityEvent> interactPredicate) {
        this.name = name;
        this.actionPredicate = null;
        this.interactPredicate = interactPredicate;
    }

    Trigger(String name, BiPredicate<Player, Action> actionPredicate) {
        this.name = name;
        this.actionPredicate = actionPredicate;
        this.interactPredicate = null;
    }

    /**
     * @return corresponding trigger, default is right_click
     */
    public static Trigger get(@Nullable String name) {
        if(name == null) return RIGHT_CLICK;
        try {
            return Trigger.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RIGHT_CLICK;
        }
    }

    /**
     * Check if the trigger is satisfied.
     */
    public boolean matches(Event event) {
        switch(event) {
            case PlayerInteractEvent e -> {
                Player player = e.getPlayer();
                Action action = e.getAction();
                if(actionPredicate != null)
                    return actionPredicate.test(player, action);
            }

            case EntityDamageByEntityEvent e -> {
                if(interactPredicate != null)
                    return interactPredicate.test(e);
            }

            case null, default -> {
                return false;
            }
        }
        return false;
    }
}
