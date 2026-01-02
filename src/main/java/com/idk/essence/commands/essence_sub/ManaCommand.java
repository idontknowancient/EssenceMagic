package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.mana_sub.GetCommand;
import com.idk.essence.commands.essence_sub.mana_sub.SetCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class ManaCommand extends SubCommand {

    public ManaCommand() {
        getSubCommands().add(new GetCommand());
        getSubCommands().add(new SetCommand());

        for(SubCommand subCommand : getSubCommands()) {
            getSubCommandsString().add(subCommand.getName());
        }
    }

    @Override
    public String getName() {
        return "mana";
    }

    @Override
    public String getDescription() {
        return "Check and modify all player's mana";
    }

    @Override
    public String getSyntax() {
        StringBuilder subs = new StringBuilder();
        subs.append("/essence ").append(getName()).append(" [");
        for(String s : getSubCommandsString())
            subs.append(s).append(" | ");
        subs.delete(subs.length() - 3, subs.length());
        subs.append("]");
        return subs + "";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MANA.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 1) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        for (SubCommand subCommand : getSubCommands()) {
            if (args[1].equalsIgnoreCase(subCommand.getName())) {
                subCommand.perform(p, args);
            }
        }
    }
}
