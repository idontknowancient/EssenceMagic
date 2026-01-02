package com.idk.essence.commands.essence_sub.item_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.items.Item;
import com.idk.essence.items.SystemItem;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

public class GetCommand extends SubCommand {

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get a custom item";
    }

    @Override
    public String getSyntax() {
        return "/essence item get <item>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_ITEM_GET.name)){
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        for(String s : Item.items.keySet()) {
            if(args[2].equalsIgnoreCase(s)) {
                p.getInventory().addItem(Item.items.get(s).getItem());
                SystemMessage.ITEM_GOT.send(p, Item.items.get(s));
                return;
            }
        }
        for(String s : SystemItem.systemItems.keySet()) {
            if(args[2].equalsIgnoreCase(s)) {
                p.getInventory().addItem(SystemItem.systemItems.get(s).getItemStack());
                SystemMessage.ITEM_GOT.send(p, SystemItem.systemItems.get(s));
                return;
            }
        }
        SystemMessage.ITEM_NOT_FOUND.send(p);
    }
}
