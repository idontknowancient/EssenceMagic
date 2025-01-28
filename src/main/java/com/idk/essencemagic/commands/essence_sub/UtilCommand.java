package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.commands.essence_sub.util_sub.GodCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class UtilCommand extends SubCommand {

    public UtilCommand() {
        getSubCommands().add(new GodCommand());

        for(SubCommand subCommand : getSubCommands()) {
            getSubCommandsString().add(subCommand.getName());
        }
    }

    @Override
    public String getName() {
        return "util";
    }

    @Override
    public String getDescription() {
        return "A lot of practical features";
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
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_UTIL.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 1) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        for(SubCommand subCommand : getSubCommands()) {
            if(args[1].equalsIgnoreCase(subCommand.getName())) {
                subCommand.perform(p, args);
            }
        }
    }
}
