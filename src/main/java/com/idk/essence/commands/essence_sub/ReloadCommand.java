package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.Register;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Reload the plugin config.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence", "reload", false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_RELOAD;
    }

    @Override
    protected int getLeastArgs() {
        return 1;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Register.initialize();
        SystemMessage.SUCCESSFULLY_RELOADED.send(sender);
    }
}
