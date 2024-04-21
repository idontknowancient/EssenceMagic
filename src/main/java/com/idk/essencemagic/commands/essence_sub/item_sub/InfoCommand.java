package com.idk.essencemagic.commands.essence_sub.item_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends SubCommand {

    private final List<String> info = new ArrayList<>();

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "show the custom item info in main hand";
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
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(!itemInMainHand.hasItemMeta()) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        if(!itemInMainHand.getItemMeta().
                getPersistentDataContainer().has(Item.getItemKey())) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        Item item = null;
        for(Item i : Item.items.values()) {
            if(i.getItem().getItemMeta().getPersistentDataContainer()
                    .equals(itemInMainHand.getItemMeta().getPersistentDataContainer())) {
                item = i;
            }
        }
        if(item == null) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        addToInfo(
                "&f--------------------",
                "&fInfo of " + item.getDisplayName() + " &f:",
                "&fInterior Name: " + item.getName(), //interior name
                "&fDisplay Name: " + itemInMainHand.getItemMeta().getDisplayName(),
                "&fType: " + item.getType(),
                "&fId: " + item.getId(),
                "&fElement: " + item.getElement().getDisplayName(),
                "&f--------------------"
        );
        info.forEach(p::sendMessage);
    }

    private void addToInfo(String ... strings) {
        for(String string : strings) {
            info.add(Util.colorize(string));
        }
    }
}
