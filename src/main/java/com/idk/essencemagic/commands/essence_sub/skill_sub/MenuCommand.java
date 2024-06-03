package com.idk.essencemagic.commands.essence_sub.skill_sub;

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
        return "Show all available skills.";
    }

    @Override
    public String getSyntax() {
        return "/essence skill menu";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_SKILL_MENU.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        p.openInventory(Menu.getSkillMenu());
    }
}
