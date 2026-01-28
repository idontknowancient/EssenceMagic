package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.messages.Message;
import com.idk.essence.utils.permissions.Permission;
import com.idk.essence.utils.permissions.SystemPermission;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {

    public HelpCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Get all command descriptions.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence", "help", false);
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public int getLeastArgs() {
        return 1;
    }

    @Override
    protected boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        List<Component> messages = new ArrayList<>();
        messages.add(Message.parse(Message.getPrefix() + "&eHelp List"));
        messages.add(Message.parse("&e----------------------------------------"));
        for(SubCommand sub : EssenceCommand.getSubCommands().values()) {
            if(sender instanceof Player p && !SystemPermission.checkPerm(p, sub.getPermission())) continue;
            messages.add(Message.parse("&e/essence " + sub.getName() + "&f: " + sub.getDescription()));
            for(SubCommand detail : sub.getSubCommands().values()) {
                if(sender instanceof Player p && !SystemPermission.checkPerm(p, detail.getPermission())) continue;
                messages.add(Message.parse("&6" + detail.getSyntax(sender) + "&r&7: " + detail.getDescription()));
            }
        }
        messages.add(Message.parse("&e----------------------------------------"));

        messages.forEach(sender::sendMessage);
    }
}
