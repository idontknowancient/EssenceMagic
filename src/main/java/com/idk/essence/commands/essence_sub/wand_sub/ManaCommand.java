package com.idk.essence.commands.essence_sub.wand_sub;

import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import com.idk.essence.items.wands.WandHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManaCommand extends SubCommand {

    @Override
    public String getName() {
        return "mana";
    }

    @Override
    public String getDescription() {
        return "Adjust the injected mana amount";
    }

    @Override
    public String getSyntax() {
        return "/essence wand mana <set/add> <amount>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA.name)){
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 3) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }

        // set or add
        String operation = args[2];
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if(itemInMainHand.getType().equals(Material.AIR)) {
            SystemMessage.NO_WAND_IN_HAND.send(p);
            return;
        }
        if(!WandHandler.isHoldingWand(p)) {
            SystemMessage.NOT_WAND.send(p);
            return;
        }

        if(operation.equalsIgnoreCase("set")) {
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA_SET.name)){
                SystemMessage.INADEQUATE_PERMISSION.send(p);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                SystemMessage.NOT_NUMBER.send(p);
                return;
            }
            WandHandler.setMana(itemInMainHand, amount);
            SystemMessage.SET_WAND_MANA.send(p, itemInMainHand);
            WandHandler.updateWand(itemInMainHand);
        } else if(operation.equalsIgnoreCase("add")) {
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA_ADD.name)){
                SystemMessage.INADEQUATE_PERMISSION.send(p);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                SystemMessage.NOT_NUMBER.send(p);
                return;
            }
            WandHandler.setMana(itemInMainHand, WandHandler.getMana(itemInMainHand) + amount);
            SystemMessage.ADD_WAND_MANA.send(p, itemInMainHand);
            WandHandler.updateWand(itemInMainHand);
        }
    }
}
