package com.idk.essence.outdated;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class ClickHandler {

    public static boolean isLeftClick(Player player, Action action) {
        return action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK);
    }

    public static boolean isShiftLeftClick(Player player, Action action) {
        return player.isSneaking() && isLeftClick(player, action);
    }

    public static boolean isRightClick(Player player, Action action) {
        return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
    }

    public static boolean isShiftRightClick(Player player, Action action) {
        return player.isSneaking() && isRightClick(player, action);
    }

    // does not check if the action is left
    public static boolean shouldCancelLeft(Player player) {
        return WandHandler.isHoldingWand(player);
    }

    // does not check if the action is right
    public static boolean shouldCancelRight(Player player) {
        return false;
    }

    // also checks if the action is a left click
    public static boolean isCancelLeftClick(Player player, Action action) {
        return isLeftClick(player, action) && shouldCancelLeft(player);
    }

    // also checks if the action is a shift left click
    public static boolean isCancelShiftLeftClick(Player player, Action action) {
        return isShiftLeftClick(player, action) && shouldCancelLeft(player);
    }

    // also checks if the action is a right click
    public static boolean isCancelRightClick(Player player, Action action) {
        return isRightClick(player, action) && shouldCancelRight(player);
    }

    // also checks if the action is a shift right click
    public static boolean isCancelShiftRightClick(Player player, Action action) {
        return isShiftRightClick(player, action) && shouldCancelRight(player);
    }

}
