package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class DomainCommand extends SubCommand {

    public DomainCommand(String name) {
        super(name);
        getSubCommands().put("get", new GetCommand("get", this));
        getSubCommands().put("give", new GiveCommand("give", this));
        getSubCommands().put("menu", new MenuCommand("menu", this));
        getSubCommands().put("remove", new RemoveCommand("remove", this));
    }

    @Override
    public String getDescription() {
        return "Check and modify all magic domains.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence magic domain", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MAGIC_DOMAIN;
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
