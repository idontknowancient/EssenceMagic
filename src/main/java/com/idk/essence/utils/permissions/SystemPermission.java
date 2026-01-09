package com.idk.essence.utils.permissions;

import org.bukkit.entity.Player;

import java.util.Optional;

public class SystemPermission {

    public static boolean checkPerm(Player p, Permission perm) {
        return Optional.ofNullable(perm).map(Permission::getName).map(p::hasPermission).orElse(true);
    }

    public static boolean checkPerm(Player p, String perm) {
        return p.hasPermission(perm);
    }

    public static boolean checkPerms(Player p, String ... perms) {
        for(String perm : perms)
            if(!p.hasPermission(perm))
                return false;
        return true;
    }
}
