package com.idk.essencemagic.commands.essence_sub.util_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class GodCommand extends SubCommand {

    @Override
    public String getName() {
        return "god";
    }

    @Override
    public String getDescription() {
        return "Make someone invulnerable";
    }

    @Override
    public String getSyntax() {
        return "/essence util god [player]";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_UTIL_GOD.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            if(p.isInvulnerable()) {
                p.setInvulnerable(false);
                SystemMessage.GOD_MODE_DISABLED.send(p);
            } else {
                p.setInvulnerable(true);
                SystemMessage.GOD_MODE_ENABLED.send(p);
            }
            return;
        }

        // check if the player has the permission to set others' god mode
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_UTIL_GOD_OTHERS.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        Player target = Bukkit.getPlayer(args[2]);
        if(target == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(p);
            return;
        }
        if(target.isInvulnerable()) {
            target.setInvulnerable(false);
            SystemMessage.GOD_MODE_DISABLED.send(target);
        } else {
            target.setInvulnerable(true);
            SystemMessage.GOD_MODE_ENABLED.send(target);
        }
    }
}
