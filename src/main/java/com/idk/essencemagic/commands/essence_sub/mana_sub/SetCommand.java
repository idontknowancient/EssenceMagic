package com.idk.essencemagic.commands.essence_sub.mana_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.player.ManaHandler;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class SetCommand extends SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "set a player's mana";
    }

    @Override
    public String getSyntax() {
        return "/essence mana set <player> <amount>";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(args.length <= 3) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p);
            return;
        }
        Player player = Bukkit.getPlayer(args[2]);
        if(player == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(p);
            return;
        }
        double amount = Double.parseDouble(args[3]);
        ManaHandler.manaMap.put(player, amount);
    }
}
