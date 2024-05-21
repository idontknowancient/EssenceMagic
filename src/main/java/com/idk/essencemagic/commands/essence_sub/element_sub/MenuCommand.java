package com.idk.essencemagic.commands.essence_sub.element_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.menus.Menu;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
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
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_ELEMENT_MENU.name))
            SystemMessage.INADEQUATE_PERMISSION.send(p);
        p.openInventory(Menu.getElementMenu());
    }
}
