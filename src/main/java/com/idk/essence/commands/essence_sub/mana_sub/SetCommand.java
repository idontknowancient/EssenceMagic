package com.idk.essence.commands.essence_sub.mana_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.players.PlayerData;
import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.Bukkit;
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
            return List.of("max", "infinite");
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player target = Bukkit.getPlayer(args[2]);
        if(!PlayerDataManager.has(target)) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }
        PlayerData data = PlayerDataManager.get(target);
        if(args[3].equalsIgnoreCase("max")) {
            data.setMana(-1);
        } else if(args[3].equalsIgnoreCase("infinite")) {
            data.setManaInfinite(true);
        } else {
            double amount;
            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                SystemMessage.NOT_NUMBER.send(sender);
                return;
            }
            data.setMana(amount);
        }
        SystemMessage.SET_MANA.send(sender, data);
    }
}
