package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.commands.essence_sub.magic_sub.AptitudeCommand;
import com.idk.essence.commands.essence_sub.magic_sub.DomainCommand;
import com.idk.essence.commands.essence_sub.magic_sub.MenuCommand;
import com.idk.essence.commands.essence_sub.magic_sub.SignetCommand;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class MagicCommand extends SubCommand {

    public MagicCommand(String name) {
        super(name);
        getSubCommands().put("aptitude", new AptitudeCommand("aptitude"));
        getSubCommands().put("domain", new DomainCommand("domain"));
        getSubCommands().put("menu", new MenuCommand("menu", this));
        getSubCommands().put("signet", new SignetCommand("signet"));
    }

    @Override
    public String getDescription() {
        return "Check and modify all magics.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromSubCommands("/essence magic", getSubCommands(), sender);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MAGIC;
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
