package com.idk.essence.commands.essence_sub.mob_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.mobs.Mob;
import com.idk.essence.mobs.MobFactory;
import com.idk.essence.mobs.MobHandler;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class SpawnCommand extends SubCommand {

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getDescription() {
        return "Spawn a custom mob";
    }

    @Override
    public String getSyntax() {
        return "/essence mob spawn <name>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MOB_SPAWN.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        String internalName = args[2];
        if(!MobFactory.spawn(internalName, p.getLocation())) {
            SystemMessage.MOB_NOT_FOUND.send(p);
        }
    }
}
