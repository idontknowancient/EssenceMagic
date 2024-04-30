package com.idk.essencemagic.commands.essence_sub.element_sub;

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
        return "Show all elements.";
    }

    @Override
    public String getSyntax() {
        return "/essence element menu";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        p.openInventory(Menu.getElementMenu());
    }
}
