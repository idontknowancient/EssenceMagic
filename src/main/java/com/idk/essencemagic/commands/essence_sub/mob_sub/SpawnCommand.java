package com.idk.essencemagic.commands.essence_sub.mob_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.mobs.MobHandler;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

import java.util.List;

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
        Mob mob = Mob.mobs.get(args[2]);
        if(mob == null) {
            SystemMessage.MOB_NOT_FOUND.send(p);
            return;
        }
        MobHandler.spawnMob(p.getLocation(), mob);
    }
}
