package com.idk.essencemagic.commands.essence_sub.mana_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class SetCommand extends SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set a specific player's mana.";
    }

    @Override
    public String getSyntax() {
        return "/essence mana set <player> <<amount>/max>";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(args.length <= 3) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        String playerName = args[2];
        if(!PlayerData.dataMap.containsKey(playerName)) {
            SystemMessage.PLAYER_NOT_EXIST.send(p);
            return;
        }
        if(args[3].equalsIgnoreCase("max")) {
            PlayerData.dataMap.get(playerName).setMana(PlayerData.dataMap.get(playerName).getMaxMana());
        } else {
            double amount = Double.parseDouble(args[3]);
            PlayerData.dataMap.get(playerName).setMana(amount);
        }
        SystemMessage.SET_MANA.send(p, PlayerData.dataMap.get(playerName));
    }
}
