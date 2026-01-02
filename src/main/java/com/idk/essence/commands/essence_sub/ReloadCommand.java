package com.idk.essence.commands.essence_sub;

import com.idk.essence.Essence;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin config";
    }

    @Override
    public String getSyntax() {
        return "/essence reload";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_RELOAD.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        Essence.initialize();
        SystemMessage.SUCCESSFULLY_RELOADED.send(p);
    }
}
