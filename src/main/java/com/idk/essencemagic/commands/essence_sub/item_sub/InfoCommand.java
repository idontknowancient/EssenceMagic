package com.idk.essencemagic.commands.essence_sub.item_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.items.ItemHandler;
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
        if(!ItemHandler.isHoldingCustomItem(p)) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        Item item = ItemHandler.getCorrespondingItem(p);
        if(item == null) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        addToInfo(
                "&f&m                                 ",
                "&7Info of &f" + item.getDisplayName() + " &7:",
                "&7Interior Name: &f" + item.getName(), //interior name
                "&7Display Name: &f" + itemInMainHand.getItemMeta().getDisplayName(),
                "&7Type: &f" + item.getType(),
                "&7Id: &f" + item.getId(),
                "&7Element: &f" + item.getElement().getDisplayName(),
                "&f&m                                 "
        );
        info.forEach(p::sendMessage);
    }

    private void addToInfo(String ... strings) {
        for(String string : strings) {
            info.add(Util.colorize(string));
        }
    }
}
