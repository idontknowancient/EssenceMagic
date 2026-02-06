package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class AptitudeCommand extends SubCommand {

    public AptitudeCommand(String name) {
        super(name);
        getSubCommands().put("get", new GetCommand("get", this));
        getSubCommands().put("give", new GiveCommand("give", this));
        getSubCommands().put("remove", new RemoveCommand("remove", this));
        getSubCommands().put("reset", new ResetCommand("reset"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all magic aptitudes.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence magic aptitude", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MAGIC_APTITUDE;
    }

    @Override
    protected int getLeastArgs() {
        return 3;
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
