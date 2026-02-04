package com.idk.essence.commands.essence_sub.magic_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.menus.Menu;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    private final SubCommand parent;

    public MenuCommand(String name, SubCommand parent) {
        super(name);
        this.parent = parent;
    }

    @Override
    public String getDescription() {
        if(parent instanceof DomainCommand)
            return "Show all available magic domains.";
        if(parent instanceof SignetCommand)
            return "Show all available magic signets.";
        return "Show all available magic domains and signets.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        if(parent instanceof DomainCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic domain", "menu", false);
        if(parent instanceof SignetCommand)
            return EssenceCommand.getSyntaxFromStrings("/essence magic signet", "menu", false);
        return EssenceCommand.getSyntaxFromStrings("/essence magic", "menu", false);
    }

    @Override
    public Permission getPermission() {
        if(parent instanceof DomainCommand)
            return Permission.COMMAND_MAGIC_DOMAIN_MENU;
        if(parent instanceof SignetCommand)
            return Permission.COMMAND_MAGIC_SIGNET_MENU;
        return Permission.COMMAND_MAGIC_MENU;
    }

    @Override
    public int getLeastArgs() {
        if(parent instanceof DomainCommand || parent instanceof  SignetCommand)
            return 3;
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
        if(parent instanceof DomainCommand) {
            p.openInventory(Menu.getMagicDomainMenu());
            return;
        }
        if(parent instanceof SignetCommand) {
            p.openInventory(Menu.getMagicSignetMenu());
            return;
        }
        p.openInventory(Menu.getMagicMenu());
    }
}
