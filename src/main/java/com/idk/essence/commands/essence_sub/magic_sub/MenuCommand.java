package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.menus.Menu;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    public MenuCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Show all available magics.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence magic", "menu", false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_MAGIC_MENU;
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
        p.openInventory(Menu.getMagicMenu());
    }
}
