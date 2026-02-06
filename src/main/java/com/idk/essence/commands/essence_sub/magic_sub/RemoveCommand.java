package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.magics.individuals.MS;
import com.idk.essence.players.PlayerData;
import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RemoveCommand extends SubCommand {

    private final SubCommand parent;

    public RemoveCommand(String name, SubCommand parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public String getDescription() {
        if(parent instanceof AptitudeCommand)
            return "Remove a magic aptitude from a player";
        if(parent instanceof DomainCommand)
            return "Remove a magic domain from a player";
        if(parent instanceof SignetCommand)
            return "Remove a magic signet from a player";
        return "";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        if(parent instanceof AptitudeCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic aptitude remove",
                    List.of("player", "aptitude"), List.of(true, true), List.of(true, true));
        if(parent instanceof DomainCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic domain remove",
                    List.of("player", "domain"), List.of(true, true), List.of(true, true));
        if(parent instanceof SignetCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic signet remove",
                    List.of("player", "signet"), List.of(true, true), List.of(true, true));
        return "";
    }

    @Override
    public Permission getPermission() {
        if(parent instanceof AptitudeCommand)
            return Permission.COMMAND_MAGIC_APTITUDE_REMOVE;
        if(parent instanceof DomainCommand)
            return Permission.COMMAND_MAGIC_DOMAIN_REMOVE;
        if(parent instanceof SignetCommand)
            return Permission.COMMAND_MAGIC_SIGNET_REMOVE;
        return null;
    }

    @Override
    protected int getLeastArgs() {
        return 5;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        if(args.length == getLeastArgs()) {
            PlayerData data = PlayerDataManager.get(args[getLeastArgs() - 2]);
            if(data == null) return null;

            if(parent instanceof AptitudeCommand) {
                return data.getGottenAptitudes();
            } else if(parent instanceof DomainCommand) {
                return data.getLearnedDomains();
            } else if(parent instanceof SignetCommand) {
                return data.getLearnedSignets();
            }
        }

        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        // es magic aptitude remove <player> <aptitude>
        PlayerData data = PlayerDataManager.get(args[getLeastArgs() - 2]);
        if(data == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }

        if(parent instanceof AptitudeCommand) {
            if(MS.removeAptitude(data, args[getLeastArgs() - 1]))
                SystemMessage.MAGIC_APTITUDE_REMOVED.send(sender, data);
            else
                SystemMessage.MAGIC_APTITUDE_NOT_FOUND.send(sender);
        } else if(parent instanceof DomainCommand) {
            if(MS.removeDomain(data, args[getLeastArgs() - 1]))
                SystemMessage.MAGIC_DOMAIN_REMOVED.send(sender, data);
            else
                SystemMessage.MAGIC_DOMAIN_NOT_FOUND.send(sender);
        } else if(parent instanceof SignetCommand) {
            if(MS.removeSignet(data, args[getLeastArgs() - 1]))
                SystemMessage.MAGIC_SIGNET_REMOVED.send(sender, data);
            else
                SystemMessage.MAGIC_SIGNET_NOT_FOUND.send(sender);
        }
    }
}
