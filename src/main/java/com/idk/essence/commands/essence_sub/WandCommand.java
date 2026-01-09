package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.wand_sub.*;
import com.idk.essence.commands.essence_sub.wand_sub.MagicCommand;
import com.idk.essence.commands.essence_sub.wand_sub.ManaCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class WandCommand extends SubCommand {

    public WandCommand(String name) {
        super(name);
        getSubCommands().put("get", new GetCommand("get"));
        getSubCommands().put("info", new InfoCommand("info"));
        getSubCommands().put("magic", new MagicCommand("magic"));
        getSubCommands().put("mana", new ManaCommand("mana"));
        getSubCommands().put("menu", new MenuCommand("menu"));
        getSubCommands().put("update", new UpdateCommand("update"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all wands.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence wand", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_WAND;
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
