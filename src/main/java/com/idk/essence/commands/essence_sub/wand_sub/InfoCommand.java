package com.idk.essence.commands.essence_sub.wand_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.items.arcana.WandHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InfoCommand extends SubCommand {

    public InfoCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Show the wand info in main hand.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence wand", "info", false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_WAND_INFO;
    }

    @Override
    protected int getLeastArgs() {
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
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) {
            SystemMessage.NO_WAND_IN_HAND.send(p);
            return;
        }
        if(!WandHandler.isHoldingWand(p)) {
            SystemMessage.NOT_WAND.send(p);
            return;
        }
        List<Component> info = ConfigManager.ConfigDefaultFile.MESSAGES.outStringList("wand-info", itemInMainHand);
        for(Component component : info) {
            p.sendMessage(component);
        }
    }
}
