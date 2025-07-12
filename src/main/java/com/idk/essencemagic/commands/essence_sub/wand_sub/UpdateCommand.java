package com.idk.essencemagic.commands.essence_sub.wand_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import com.idk.essencemagic.wands.WandHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UpdateCommand extends SubCommand {

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Update the wand player holding";
    }

    @Override
    public String getSyntax() {
        return "/es wand update";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_UPDATE.name)) {
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
        WandHandler.updateWand(itemInMainHand);
        SystemMessage.WAND_UPDATED.send(p);
    }
}
