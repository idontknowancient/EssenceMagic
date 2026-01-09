package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.item_sub.GetCommand;
import com.idk.essence.commands.essence_sub.item_sub.InfoCommand;
import com.idk.essence.commands.essence_sub.item_sub.MenuCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class ItemCommand extends SubCommand {

    public ItemCommand(String name) {
        super(name);
        getSubCommands().put("get", new GetCommand("get"));
        getSubCommands().put("info", new InfoCommand("info"));
        getSubCommands().put("menu", new MenuCommand("menu"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all items.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence item", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_ITEM;
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
