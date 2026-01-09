package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.mob_sub.MenuCommand;
import com.idk.essence.commands.essence_sub.mob_sub.SpawnCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class MobCommand extends SubCommand {

    public MobCommand(String name) {
        super(name);
        getSubCommands().put("menu", new MenuCommand("menu"));
        getSubCommands().put("spawn", new SpawnCommand("spawn"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all mobs.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence mob", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MOB;
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
