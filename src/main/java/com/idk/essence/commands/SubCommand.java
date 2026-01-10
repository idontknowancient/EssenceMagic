package com.idk.essence.commands;

import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public abstract class SubCommand {

    private final String name;

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public SubCommand(String name) {
        this.name = name;
    }

    public abstract String getDescription();

    /**
     * Get the syntax of this command. Automatically handle permission.
     * @return serialized string from component
     */
    public abstract String getSyntax(CommandSender sender);

    public abstract Permission getPermission();

    /**
     * For example, "/essence item " has least 2 argument(s).
     */
    protected abstract int getLeastArgs();

    protected abstract boolean isPlayerOnly();

    private List<String> getTabCompletionBySub(Player p, String[] args) {
        return getSubCommands().values().stream().filter(sub ->
                SystemPermission.checkPerm(p, sub.getPermission())).map(SubCommand::getName).toList();
    }

    /**
     * Dispatch to subcommands according to the length of args. Automatically handle permission.
     */
    @Nullable
    public List<String> getTabCompletion(Player p, String[] args) {
        if(args.length > getLeastArgs()) {
            String sub = args[getLeastArgs() - 1].toLowerCase();
            return Optional.ofNullable(subCommands.get(sub))
                    .filter(subCommand -> SystemPermission.checkPerm(p, subCommand.getPermission()))
                    .map(subCommand -> subCommand.getTabCompletion(p, args))
                    .orElse(null);
        }
        return getTabCompletionBySub(p, args);
    }

    private boolean checkPermission(CommandSender sender) {
        if(!(sender instanceof Player p)) return true;
        if(!SystemPermission.checkPerm(p, getPermission())) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return false;
        }
        return true;
    }

    private boolean checkArgs(CommandSender sender, String[] args) {
        if(args.length < getLeastArgs()) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(sender, getSyntax(sender));
            return false;
        }
        return true;
    }

    private boolean checkPlayerOnly(CommandSender sender) {
        if(isPlayerOnly() && !(sender instanceof Player)) {
            SystemMessage.PLAYER_ONLY.send(sender);
            return false;
        }
        return true;
    }

    /**
     * Check permission and arguments length.
     * @return whether the command can be executed
     */
    protected boolean preCheck(CommandSender sender, String[] args) {
        if(!checkPlayerOnly(sender)) return false;
        if(!checkPermission(sender)) return false;
        return checkArgs(sender, args);
    }

    /**
     * Dispatch to subcommands.
     */
    public void dispatch(CommandSender sender, String[] args) {
        Optional.ofNullable(subCommands.get(args[getLeastArgs() - 1])).ifPresentOrElse(
                subCommand -> subCommand.perform(sender, args),
                () -> SystemMessage.UNKNOWN_COMMAND.send(sender));
    }

    public abstract void perform(CommandSender sender, String[] args);
}
