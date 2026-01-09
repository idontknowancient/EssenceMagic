package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.magics.Magic;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForceCommand extends SubCommand {

    public ForceCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Force a specific magic.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence magic force", "magic", true) ;
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MAGIC_FORCE;
    }

    @Override
    public int getLeastArgs() {
        return 3;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        return Magic.magics.keySet().stream().toList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
        SystemMessage.MAGIC_NOT_FOUND.send(p);
    }
}
