package com.idk.essencemagic.commands.essence_sub.wand_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import com.idk.essencemagic.items.wands.WandHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InfoCommand extends SubCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Show the wand info in main hand";
    }

    @Override
    public String getSyntax() {
        return "/essence wand info";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_INFO.name)) {
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) {
            SystemMessage.NO_WAND_IN_HAND.send(p);
            return;
        }
        if(!WandHandler.isHoldingWand(p)) {
            SystemMessage.NOT_WAND.send(p);
            return;
        }
        List<String> info = ConfigFile.ConfigName.MESSAGES.outStringList("wand-info", itemInMainHand);
        for(String string : info) {
            p.sendMessage(string);
        }
    }
}
