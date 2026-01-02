package com.idk.essence.commands.essence_sub.wand_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import com.idk.essence.items.wands.Wand;
import com.idk.essence.items.wands.WandHandler;
import org.bukkit.entity.Player;

public class GetCommand extends SubCommand {

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get a wand";
    }

    @Override
    public String getSyntax() {
        return "/essence wand get <wand>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_GET.name)){
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        String wandName = args[2];
        Wand wand = WandHandler.getWand(wandName);
        if(wand == null)  {
            SystemMessage.WAND_NOT_FOUND.send(p);
            return;
        }
        p.getInventory().addItem(wand.getItemStack());
        SystemMessage.WAND_GOT.send(p, wand);
    }
}
