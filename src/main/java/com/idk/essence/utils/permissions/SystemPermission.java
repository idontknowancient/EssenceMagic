package com.idk.essence.utils.permissions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SystemPermission {

    public static boolean checkPerm(Player p, @Nullable Permission permission) {
        return Optional.ofNullable(permission).map(perm -> p.hasPermission(perm.getName())).orElse(true);
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
