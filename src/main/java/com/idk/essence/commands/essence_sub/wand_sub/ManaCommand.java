package com.idk.essence.commands.essence_sub.wand_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import com.idk.essence.items.wands.WandHandler;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaCommand extends SubCommand {

    public ManaCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Adjust the injected mana amount.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence wand mana", List.of("set/add", "amount"),
                List.of(true, true), List.of(true, true));
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_WAND_MANA;
    }

    @Override
    protected int getLeastArgs() {
        return 4;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        return List.of("set", "add");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
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
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA_SET)){
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
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MANA_ADD)){
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
