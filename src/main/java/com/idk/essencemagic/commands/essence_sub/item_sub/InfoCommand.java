package com.idk.essencemagic.commands.essence_sub.item_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends SubCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show the custom item info in main hand.";
    }

    @Override
    public String getSyntax() {
        return "/essence item info";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_ITEM_INFO.name))
            SystemMessage.INADEQUATE_PERMISSION.send(p);
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) {
            SystemMessage.NO_ITEM_IN_HAND.send(p);
            return;
        }
        if(!ItemHandler.isHoldingCustomItem(p)) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        Item item = ItemHandler.getCorrespondingItem(p);
        if(item == null) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        List<String> info = ConfigFile.ConfigName.MESSAGES.outStringList("item-info", item);
        for(String string : info)
            p.sendMessage(string);
    }
}
