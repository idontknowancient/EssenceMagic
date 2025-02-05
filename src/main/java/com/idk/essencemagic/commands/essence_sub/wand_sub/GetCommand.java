package com.idk.essencemagic.commands.essence_sub.wand_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import com.idk.essencemagic.wands.Wand;
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
        for(String s : Wand.wands.keySet()) {
            if(args[2].equalsIgnoreCase(s)) {
                p.getInventory().addItem(Wand.wands.get(s).getItemStack());
                SystemMessage.WAND_GOT.send(p, Wand.wands.get(s));
                return;
            }
        }
        SystemMessage.WAND_NOT_FOUND.send(p);
    }
}
