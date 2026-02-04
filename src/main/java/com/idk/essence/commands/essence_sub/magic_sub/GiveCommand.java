package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.magics.MagicDomain;
import com.idk.essence.magics.MagicManager;
import com.idk.essence.magics.MagicSignet;
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

public class GiveCommand extends SubCommand {

    private final SubCommand parent;

    public GiveCommand(String name, SubCommand parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public String getDescription() {
        if(parent instanceof AptitudeCommand)
            return "Give a player magic aptitude";
        if(parent instanceof DomainCommand)
            return "Give a player magic domain";
        if(parent instanceof SignetCommand)
            return "Give a player magic signet";
        return "";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        if(parent instanceof AptitudeCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic aptitude give",
                    List.of("player", "aptitude"), List.of(true, true), List.of(true, true));
        if(parent instanceof DomainCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic domain give",
                    List.of("player", "domain"), List.of(true, true), List.of(true, true));
        if(parent instanceof SignetCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic signet give",
                    List.of("player", "signet"), List.of(true, true), List.of(true, true));
        return "";
    }

    @Override
    public Permission getPermission() {
        if(parent instanceof AptitudeCommand)
            return Permission.COMMAND_MAGIC_APTITUDE_GIVE;
        if(parent instanceof DomainCommand)
            return Permission.COMMAND_MAGIC_DOMAIN_GIVE;
        if(parent instanceof SignetCommand)
            return Permission.COMMAND_MAGIC_SIGNET_GIVE;
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
            if(parent instanceof AptitudeCommand || parent instanceof DomainCommand)
                return MagicManager.getAllDomains().stream().map(MagicDomain::getInternalName).toList();
            if(parent instanceof SignetCommand)
                return MagicManager.getAllSignets().stream().map(MagicSignet::getInternalName).toList();
        }
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        // es magic aptitude give <player> <aptitude>
        PlayerData data = PlayerDataManager.get(args[getLeastArgs() - 2]);
        if(data == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }

        if(parent instanceof AptitudeCommand) {
            if(MS.addAptitude(data, args[getLeastArgs() - 1]))
                SystemMessage.MAGIC_APTITUDE_GAVE.send(sender, data);
            else
                SystemMessage.MAGIC_APTITUDE_NOT_FOUND.send(sender);
        } else if(parent instanceof DomainCommand) {
            if(MS.addDomain(data, args[getLeastArgs() - 1]))
                SystemMessage.MAGIC_DOMAIN_GAVE.send(sender, data);
            else
                SystemMessage.MAGIC_DOMAIN_NOT_FOUND.send(sender);
        } else if(parent instanceof SignetCommand) {
            if(MS.addSignet(data, args[getLeastArgs() - 1]))
                SystemMessage.MAGIC_SIGNET_GAVE.send(sender, data);
            else
                SystemMessage.MAGIC_SIGNET_NOT_FOUND.send(sender);
        }
    }
}
