package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.element_sub.MenuCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class ElementCommand extends SubCommand {

    public ElementCommand(String name) {
        super(name);
        getSubCommands().put("menu", new MenuCommand("menu"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all elements.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence element", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_ELEMENT;
    }

    @Override
    public int getLeastArgs() {
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
