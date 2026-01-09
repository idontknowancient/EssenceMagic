package com.idk.essence.commands.essence_sub.util_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommand extends SubCommand {

    public GodCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Make someone invulnerable.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence util god", "player", true, false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_UTIL_GOD;
    }

    @Override
    protected int getLeastArgs() {
        return 2;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        if(args.length == 2) {
            if(!(sender instanceof Player p))  {
                SystemMessage.PLAYER_ONLY.send(sender);
                return;
            }
            switchGodMode(p);
            return;
        }

        // Set other player's god mode
        if(sender instanceof Player p && !SystemPermission.checkPerm(p, Permission.COMMAND_UTIL_GOD_OTHERS)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        Player target = Bukkit.getPlayer(args[2]);
        if(target == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }
        switchGodMode(target);
    }

    private void switchGodMode(Player p) {
        if(p.isInvulnerable()) {
            p.setInvulnerable(false);
            SystemMessage.GOD_MODE_DISABLED.send(p);
        } else {
            p.setInvulnerable(true);
            SystemMessage.GOD_MODE_ENABLED.send(p);
        }
    }
}
