package com.idk.essence.commands.essence_sub.wand_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.items.arcana.Wand;
import com.idk.essence.items.arcana.WandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetCommand extends SubCommand {

    public GetCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Get a wand.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence wand get", "wand", true);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_WAND_GET;
    }

    @Override
    protected int getLeastArgs() {
        return 3;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        return Wand.wands.keySet().stream().toList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
        String wandName = args[2];
        Wand wand = WandHandler.getWand(wandName);
        if(wand == null)  {
            SystemMessage.WAND_NOT_FOUND.send(p);
            return;
        }
        p.getInventory().addItem(wand.getItemStack());
        SystemMessage.WAND_GOT.send(p, wand);
    }
}
