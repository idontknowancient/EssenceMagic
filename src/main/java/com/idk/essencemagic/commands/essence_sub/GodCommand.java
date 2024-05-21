package com.idk.essencemagic.commands.essence_sub;

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
        return "make someone invulnerable";
    }

    @Override
    public String getSyntax() {
        return "/essence god [player]";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_GOD.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 1) {
            if(p.isInvulnerable()) {
                p.setInvulnerable(false);
                SystemMessage.GOD_MODE_DISABLED.send(p);
            } else {
                p.setInvulnerable(true);
                SystemMessage.GOD_MODE_ENABLED.send(p);
            }
        } else {
            Player target = Bukkit.getPlayer(args[1]);
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
}
