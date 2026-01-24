package com.idk.essence.commands.essence_sub.item_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetCommand extends SubCommand {

    public GetCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Get a custom item.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence item get", "item", true);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_ITEM_GET;
    }

    @Override
    public int getLeastArgs() {
        return 3;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        List<String> completions = new ArrayList<>();
        completions.addAll(ItemFactory.getAllKeys());
        completions.addAll(ArtifactFactory.getAllActivateKeys());
        return completions;
    }


    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
        String internalName = args[2];

        // Check artifact -> item
        ItemStack stack = ArtifactFactory.getArtifact(internalName);
        if(stack == null)
            stack = ItemFactory.get(internalName);
        if(stack == null) {
            SystemMessage.ITEM_NOT_FOUND.send(p);
            return;
        }
        p.getInventory().addItem(stack);
        SystemMessage.ITEM_GOT.send(p, stack);
    }
}
