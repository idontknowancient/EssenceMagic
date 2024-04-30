package com.idk.essencemagic.commands.essence_sub.item_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.entity.Player;

import java.util.List;

public class GetCommand extends SubCommand {

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get a custom item.";
    }

    @Override
    public String getSyntax() {
        return "/essence item get <item>";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
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
        SystemMessage.ITEM_NOT_FOUND.send(p);
    }
}
