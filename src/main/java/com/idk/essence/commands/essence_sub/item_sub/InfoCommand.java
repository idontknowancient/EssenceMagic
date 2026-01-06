package com.idk.essence.commands.essence_sub.item_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.items.Item;
import com.idk.essence.items.ItemHandler;
import com.idk.essence.utils.configs.ConfigFile;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import com.idk.essence.items.wands.WandHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InfoCommand extends SubCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show the custom item info in main hand";
    }

    @Override
    public String getSyntax() {
        return "/essence item info";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_ITEM_INFO.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) {
            SystemMessage.NO_ITEM_IN_HAND.send(p);
            return;
        }
        if(!ItemHandler.isHoldingCustomItem(p) && !WandHandler.isHoldingWand(p)) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
//        Item item = ItemHandler.getCorrespondingItem(p);
//        if(item != null) {
//            List<String> info = ConfigFile.ConfigName.MESSAGES.outStringList("item-info", item);
//            for(String string : info) {
//                p.sendMessage(string);
//            }
//            return;
//        }
        List<String> info = ConfigFile.ConfigName.MESSAGES.outStringList("item-info", itemInMainHand);
        for (String string : info) {
            p.sendMessage(string);
        }
    }
}
