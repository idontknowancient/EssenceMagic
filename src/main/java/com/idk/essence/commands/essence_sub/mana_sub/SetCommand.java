package com.idk.essence.commands.essence_sub.mana_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.players.PlayerData;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetCommand extends SubCommand {

    public SetCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Set a specific player's mana.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence mana set",
                List.of("player", "<amount>/max"), List.of(true, true), List.of(true, true));
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MANA_SET;
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
        if(args.length == 4)
            return List.of("max");
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        String playerName = args[2];
        if(!PlayerData.dataMap.containsKey(playerName)) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }
        if(args[3].equalsIgnoreCase("max")) {
            PlayerData.dataMap.get(playerName).setMana(PlayerData.dataMap.get(playerName).getMaxMana());
        } else {
            double amount = 0;
            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                SystemMessage.NOT_NUMBER.send(sender);
                return;
            }
            PlayerData.dataMap.get(playerName).setMana(amount);
        }
        SystemMessage.SET_MANA.send(sender, PlayerData.dataMap.get(playerName));
    }
}
