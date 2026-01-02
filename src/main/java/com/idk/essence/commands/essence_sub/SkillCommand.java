package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.skill_sub.CastCommand;
import com.idk.essence.commands.essence_sub.skill_sub.ForceCommand;
import com.idk.essence.commands.essence_sub.skill_sub.MenuCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class SkillCommand extends SubCommand {

    public SkillCommand() {
        getSubCommands().add(new CastCommand());
        getSubCommands().add(new ForceCommand());
        getSubCommands().add(new MenuCommand());

        for(SubCommand subCommand : getSubCommands()) {
            getSubCommandsString().add(subCommand.getName());
        }
    }

    @Override
    public String getName() {
        return "skill";
    }

    @Override
    public String getDescription() {
        return "Check and modify all skills";
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
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_SKILL.name)) {
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
