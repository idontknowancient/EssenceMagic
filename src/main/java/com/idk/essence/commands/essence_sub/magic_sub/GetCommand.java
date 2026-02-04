package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.magics.MagicManager;
import com.idk.essence.magics.individuals.MS;
import com.idk.essence.players.PlayerData;
import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class GetCommand extends SubCommand {

    private final SubCommand parent;

    public GetCommand(String name, SubCommand parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public String getDescription() {
        if(parent instanceof AptitudeCommand)
            return "Show all gotten magic aptitudes of a player";
        if(parent instanceof DomainCommand)
            return "Show all learned magic domains of a player";
        if(parent instanceof SignetCommand)
            return "Show all learned magic signets of a player.";
        return "";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        if(parent instanceof AptitudeCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic aptitude get", "player", true);
        if(parent instanceof DomainCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic domain get", "player", false);
        if(parent instanceof SignetCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic signet get", "player", false);
        return "";
    }

    @Override
    public Permission getPermission() {
        if(parent instanceof AptitudeCommand)
            return Permission.COMMAND_MAGIC_APTITUDE_GET;
        if(parent instanceof DomainCommand)
            return Permission.COMMAND_MAGIC_DOMAIN_GET;
        if(parent instanceof SignetCommand)
            return Permission.COMMAND_MAGIC_SIGNET_GET;
        return null;
    }

    @Override
    protected int getLeastArgs() {
        return 4;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        // es magic aptitude get <player>
        PlayerData data = PlayerDataManager.get(args[getLeastArgs() - 1]);
        if(data == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }

        if(parent instanceof AptitudeCommand) {
            SystemMessage.MAGIC_APTITUDE_GOT.send(sender, data);
            MS.getAptitudeDisplay(data).forEach(sender::sendMessage);
        } else if(parent instanceof DomainCommand) {
            SystemMessage.MAGIC_DOMAIN_GOT.send(sender, data);
            MS.getDomainDisplay(data).forEach(sender::sendMessage);
        } else if(parent instanceof SignetCommand) {
            SystemMessage.MAGIC_SIGNET_GOT.send(sender, data);
            MS.getSignetDisplay(data).forEach(sender::sendMessage);
        }
    }
}
