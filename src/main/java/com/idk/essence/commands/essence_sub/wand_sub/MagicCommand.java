package com.idk.essence.commands.essence_sub.wand_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.magics_old.Magic;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import com.idk.essence.outdated.WandHandler;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicCommand extends SubCommand {

    public MagicCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Set available magic of a wand.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence wand magic", List.of("set/remove", "slot", "magic"),
                List.of(true, true, true), List.of(true, true, false));
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_WAND_MAGIC;
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
        return List.of("set", "remove");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // e.g. "/es wand magic set 1 fire_beam" & "/es wand magic remove 0"
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;

        // <set/remove> <slot> [magic]
        String operation = args[2];
        int targetSlot;
        try {
            targetSlot = Integer.parseInt(args[3]);
        } catch (NumberFormatException ignored) {
            SystemMessage.NOT_NUMBER.send(p);
            return;
        }
        if(targetSlot < 0) {
            SystemMessage.NOT_NEGATIVE_NUMBER.send(p);
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

        int totalSlot = WandHandler.getSlot(itemInMainHand);
        if(targetSlot >= totalSlot) {
            SystemMessage.SLOT_NOT_EXIST.send(p);
            return;
        }

        String WandMagic = WandHandler.getWandMagic(itemInMainHand);
        String[] wandMagic = WandMagic.split(";");
        StringBuilder newMagic = new StringBuilder();

        if(operation.equalsIgnoreCase("set")) {
            if(args.length == 4) {
                SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax(p));
                return;
            }
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MAGIC_SET)) {
                SystemMessage.INADEQUATE_PERMISSION.send(p);
                return;
            }

            String magic = args[4];
            if(!Magic.magics.containsKey(magic)) {
                SystemMessage.MAGIC_NOT_FOUND.send(p);
                return;
            }
            for(int i = 0; i < totalSlot; i++) {
                if(i == targetSlot) {
                    newMagic.append(magic).append(";");
                } else {
                    newMagic.append(wandMagic[i]).append(";");
                }
            }
            newMagic.append(wandMagic[wandMagic.length - 1]);
            SystemMessage.WAND_MAGIC_UPDATED.send(p);
        } else if(operation.equalsIgnoreCase("remove")) {
            if(!SystemPermission.checkPerm(p, Permission.COMMAND_WAND_MAGIC_REMOVE)) {
                SystemMessage.INADEQUATE_PERMISSION.send(p);
                return;
            }
            for(int i = 0; i < totalSlot; i++) {
                if(i == targetSlot) {
                    newMagic.append(";");
                } else {
                    newMagic.append(wandMagic[i]).append(";");
                }
            }
            newMagic.append(wandMagic[wandMagic.length - 1]);
            SystemMessage.WAND_MAGIC_REMOVED.send(p);
        }
        WandHandler.setWandMagic(itemInMainHand, newMagic.toString());

        WandHandler.updateWand(itemInMainHand);
    }
}
