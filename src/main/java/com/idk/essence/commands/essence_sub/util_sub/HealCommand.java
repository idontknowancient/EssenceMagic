package com.idk.essence.commands.essence_sub.util_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HealCommand extends SubCommand {

    public HealCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Totally Heal a player.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence util heal", "player", true, false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_UTIL_HEAL;
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
            heal(p);
            return;
        }

        // Heal other player
        if(sender instanceof Player p && !SystemPermission.checkPerm(p, Permission.COMMAND_UTIL_HEAL_OTHERS)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        Player target = Bukkit.getPlayer(args[2]);
        if(target == null) {
            SystemMessage.PLAYER_NOT_EXIST.send(sender);
            return;
        }
        heal(target);
    }

    private void heal(Player player) {
        player.setHealth(Optional.ofNullable(player.getAttribute(Attribute.MAX_HEALTH))
                .map(AttributeInstance::getValue).orElse(20d));
        player.setFoodLevel(20);
        player.setSaturation(20);
    }
}
