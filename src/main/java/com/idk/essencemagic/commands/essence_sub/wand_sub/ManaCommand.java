package com.idk.essencemagic.commands.essence_sub.wand_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import com.idk.essencemagic.wands.Wand;
import com.idk.essencemagic.wands.WandHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
        ItemMeta meta = itemInMainHand.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if(operation.equalsIgnoreCase("set")) {
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA_SET.name)){
                SystemMessage.INADEQUATE_PERMISSION.send(p);
                return;
            }
            double amount = 0;
            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                SystemMessage.NOT_NUMBER.send(p);
                return;
            }
            container.set(Wand.getManaKey(), PersistentDataType.DOUBLE, amount);
            itemInMainHand.setItemMeta(meta);
            WandHandler.updateWand(itemInMainHand);
            SystemMessage.SET_WAND_MANA.send(p, itemInMainHand);
        } else if(operation.equalsIgnoreCase("add")) {
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA_ADD.name)){
                SystemMessage.INADEQUATE_PERMISSION.send(p);
                return;
            }
            double amount = 0;
            try {
                amount = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                SystemMessage.NOT_NUMBER.send(p);
                return;
            }
            Double Mana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE);
            double mana = Mana != null ? Mana : 0;
            container.set(Wand.getManaKey(), PersistentDataType.DOUBLE, amount+mana);
            itemInMainHand.setItemMeta(meta);
            WandHandler.updateWand(itemInMainHand);
            SystemMessage.ADD_WAND_MANA.send(p, itemInMainHand);
        }
    }
}
