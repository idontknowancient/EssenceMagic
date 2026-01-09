package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.mana_sub.GetCommand;
import com.idk.essence.commands.essence_sub.mana_sub.SetCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class ManaCommand extends SubCommand {

    public ManaCommand(String name) {
        super(name);
        getSubCommands().put("get", new GetCommand("get"));
        getSubCommands().put("set", new SetCommand("set"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all player's mana.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence mana", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MANA;
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
