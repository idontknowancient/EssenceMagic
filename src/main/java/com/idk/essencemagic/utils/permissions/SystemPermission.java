package com.idk.essencemagic.utils.permissions;

import org.bukkit.entity.Player;

public class SystemPermission {

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
