package com.idk.essence.menus;

import com.idk.essence.menus.providers.*;
import org.bukkit.entity.Player;

public class MenuManager {

    public static void openElementMenu(Player player) {
        new ElementMenuProvider().createMenu().open(player);
    }

    public static void openMobMenu(Player player) {
        new MobMenuProvider().createMenu().open(player);
    }

    public static void openSkillMenu(Player player) {
        new SkillMenuProvider().createMenu().open(player);
    }

    public static void openItemMenu(Player player) {
        new ItemMenuProvider().createMenu().open(player);
    }

    public static void openDomainMenu(Player player) {
        new DomainMenuProvider().createMenu().open(player);
    }

    public static void openSignetMenu(Player player) {
        new SignetMenuProvider().createMenu().open(player);
    }

    public static void openMagicMenu(Player player) {
        new MagicMenuProvider().createMenu().open(player);
    }
}
