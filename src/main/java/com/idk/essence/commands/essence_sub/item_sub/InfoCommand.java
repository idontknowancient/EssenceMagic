package com.idk.essence.commands.essence_sub.item_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.items.items.ItemFactory;
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
        return "Show the custom item info in main hand.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence item", "info", false);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_ITEM_INFO;
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
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) {
            SystemMessage.NO_ITEM_IN_HAND.send(p);
            return;
        }
        if(!ItemFactory.isHoldingCustom(p) && !WandHandler.isHoldingWand(p)) {
            SystemMessage.NOT_CUSTOM_ITEM.send(p);
            return;
        }
        List<Component> info = ConfigManager.DefaultFile.MESSAGES.outStringList("item-info", itemInMainHand);
        info.forEach(p::sendMessage);
    }
}
