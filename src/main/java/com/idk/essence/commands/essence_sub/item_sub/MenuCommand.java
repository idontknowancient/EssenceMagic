package com.idk.essence.commands.essence_sub.item_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.menus.Menu;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

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
