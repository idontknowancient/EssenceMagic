package com.idk.essencemagic.commands.essence_sub.mana_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.players.PlayerData;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class GetCommand extends SubCommand {

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get specific or all players' mana";
    }

    @Override
    public String getSyntax() {
        return "/essence mana get [player]";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MANA_GET.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            for(String playerName : PlayerData.dataMap.keySet()) {
                SystemMessage.GET_MANA.send(p, PlayerData.dataMap.get(playerName));
            }
            return;
        }

        // check if the player has the permission to get others' mana
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MANA_GET_OTHERS.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        String playerName = args[2];
        if(!PlayerData.dataMap.containsKey(playerName)) {
            SystemMessage.PLAYER_NOT_EXIST.send(p);
            return;
        }
        SystemMessage.GET_MANA.send(p, PlayerData.dataMap.get(playerName));
    }
}
