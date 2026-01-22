package com.idk.essence.commands.essence_sub.mob_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.mobs.MobManager;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpawnCommand extends SubCommand {

    public SpawnCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Spawn a custom mob.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence mob spawn", "name", true) ;
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MOB_SPAWN;
    }

    @Override
    protected int getLeastArgs() {
        return 3;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        return MobManager.getAllKeys().stream().toList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
        String internalName = args[2];
        if(!MobManager.spawn(internalName, p.getLocation())) {
            SystemMessage.MOB_NOT_FOUND.send(p);
        }
    }
}
