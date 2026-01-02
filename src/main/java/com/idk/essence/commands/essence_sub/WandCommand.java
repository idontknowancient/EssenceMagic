package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.wand_sub.*;
import com.idk.essence.commands.essence_sub.wand_sub.MagicCommand;
import com.idk.essence.commands.essence_sub.wand_sub.ManaCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class WandCommand extends SubCommand {

    public WandCommand() {
        getSubCommands().add(new GetCommand());
        getSubCommands().add(new InfoCommand());
        getSubCommands().add(new MagicCommand());
        getSubCommands().add(new MenuCommand());
        getSubCommands().add(new ManaCommand());
        getSubCommands().add(new UpdateCommand());

        for(SubCommand subCommand : getSubCommands()) {
            getSubCommandsString().add(subCommand.getName());
        }
    }

    @Override
    public String getName() {
        return "wand";
    }

    @Override
    public String getDescription() {
        return "Check and modify all wands";
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
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND.name)) {
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
