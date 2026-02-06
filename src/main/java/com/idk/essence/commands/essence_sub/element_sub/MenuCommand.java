package com.idk.essence.commands.essence_sub.element_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.menus.MenuManager;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    public MenuCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Show all elements.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence element", "menu", false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_ELEMENT_MENU;
    }

    @Override
    public int getLeastArgs() {
        return 2;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
        MenuManager.openElementMenu(p);
    }
}
