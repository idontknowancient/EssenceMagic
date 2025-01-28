package com.idk.essencemagic.commands.essence_sub.item_sub;

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
        return "Show all available custom items";
    }

    @Override
    public String getSyntax() {
        return "/essence item menu";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_ITEM_MENU.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        p.openInventory(Menu.getItemMenu());
    }
}
