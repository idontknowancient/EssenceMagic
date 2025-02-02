package com.idk.essencemagic.commands.essence_sub.magic_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class ForceCommand extends SubCommand {

    @Override
    public String getName() {
        return "force";
    }

    @Override
    public String getDescription() {
        return "Force a specific magic";
    }

    @Override
    public String getSyntax() {
        return "/essence magic force <magic>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MAGIC_FORCE.name)){
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
