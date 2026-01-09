package com.idk.essence.commands.essence_sub.mana_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.players.PlayerData;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetCommand extends SubCommand {

    public GetCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Get a specific players' mana.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence mana get", "player", true, false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MANA_GET;
    }

    @Override
    public int getLeastArgs() {
        return 2;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        String playerName;

        // Get sender's mana
        if(args.length == 2) {
            if(!(sender instanceof Player p))  {
                SystemMessage.PLAYER_ONLY.send(sender);
                return;
            }
            playerName = p.getName();
            SystemMessage.GET_MANA.send(p, PlayerData.dataMap.get(playerName));
            return;
        }

        // Get other player's mana
        if(sender instanceof Player p && !SystemPermission.checkPerm(p, Permission.COMMAND_MANA_GET_OTHERS)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        playerName = args[2];
        if(!PlayerData.dataMap.containsKey(playerName)) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }
        SystemMessage.GET_MANA.send(sender, PlayerData.dataMap.get(playerName));
    }
}
