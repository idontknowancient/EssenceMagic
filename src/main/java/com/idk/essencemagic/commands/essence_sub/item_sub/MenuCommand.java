package com.idk.essencemagic.commands.essence_sub.item_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.menus.Menu;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand extends SubCommand {
    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "show all available custom items";
    }

    @Override
    public String getSyntax() {
        return "/essence item menu";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        p.openInventory(Menu.getItemMenu());
    }
}
