package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.util_sub.GodCommand;
import com.idk.essence.commands.essence_sub.util_sub.HealCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class UtilCommand extends SubCommand {

    public UtilCommand(String name) {
        super(name);
        getSubCommands().put("god", new GodCommand("god"));
        getSubCommands().put("heal", new HealCommand("heal"));
    }

    @Override
    public String getDescription() {
        return "A lot of practical features.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence util", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_UTIL;
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
