package com.idk.essence.commands.essence_sub.mana_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.players.PlayerData;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class SetCommand extends SubCommand {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set a specific player's mana";
    }

    @Override
    public String getSyntax() {
        return "/essence mana set <player> <<amount>/max>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MANA_SET.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 3) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }

        // check if the player has the permission to set others' mana
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MANA_SET_OTHERS.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
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
