package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.skill_sub.CastCommand;
import com.idk.essence.commands.essence_sub.skill_sub.ForceCommand;
import com.idk.essence.commands.essence_sub.skill_sub.MenuCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class SkillCommand extends SubCommand {

    public SkillCommand(String name) {
        super(name);
        getSubCommands().put("cast", new CastCommand("cast"));
        getSubCommands().put("force", new ForceCommand("force"));
        getSubCommands().put("menu", new MenuCommand("menu"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all skills.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence skill", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_SKILL;
    }

    @Override
    protected int getLeastArgs() {
        return 2;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        dispatch(sender, args);
    }
}
