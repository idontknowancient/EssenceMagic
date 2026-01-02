package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.mob_sub.MenuCommand;
import com.idk.essence.commands.essence_sub.mob_sub.SpawnCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class MobCommand extends SubCommand {

    public MobCommand() {
        getSubCommands().add(new MenuCommand());
        getSubCommands().add(new SpawnCommand());

        for(SubCommand subCommand : getSubCommands()) {
            getSubCommandsString().add(subCommand.getName());
        }
    }

    @Override
    public String getName() {
        return "mob";
    }

    @Override
    public String getDescription() {
        return "Check and modify all mobs";
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
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MOB.name)) {
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
