package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.players.PlayerData;
import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResetCommand extends SubCommand {

    public ResetCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Reset magic aptitudes of a player. Be careful!";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence magic aptitude reset", "player", true);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MAGIC_APTITUDE_RESET;
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
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        // es magic aptitude reset <player>
        OfflinePlayer player = Util.Tool.getPlayerByName(args[getLeastArgs() - 1]);
        PlayerData data = PlayerDataManager.get(player);
        if(data == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }

        // Clear aptitudes
        data.getGottenAptitudes().clear();
        // Regenerate aptitudes
        PlayerDataManager.create(player);
        SystemMessage.MAGIC_APTITUDE_RESET.send(sender, data);
    }
}
