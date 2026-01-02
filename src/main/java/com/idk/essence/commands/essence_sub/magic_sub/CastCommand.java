package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class CastCommand extends SubCommand {

    @Override
    public String getName() {
        return "cast";
    }

    @Override
    public String getDescription() {
        return "Cast a specific magic";
    }

    @Override
    public String getSyntax() {
        return "/essence magic cast <magic>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MAGIC_CAST.name)){
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        SystemMessage.MAGIC_NOT_FOUND.send(p);
    }
}
