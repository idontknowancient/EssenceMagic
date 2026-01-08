package com.idk.essence.commands.essence_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.messages.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get all command descriptions";
    }

    @Override
    public String getSyntax() {
        return "/es help";
    }

    @Override
    public void perform(Player p, String[] args) {
        List<Component> messages = new ArrayList<>();
        messages.add(Util.parseMessage(Message.getPrefix() + "&eHelp List"));
        messages.add(Util.parseMessage("&e----------------------------------------"));
        for(SubCommand sub : EssenceCommand.getSubCommands()) {
            messages.add(Util.parseMessage("&e/essence " + sub.getName() + "&f: " + sub.getDescription()));
            for(SubCommand detail : sub.getSubCommands()) {
                messages.add(Util.parseMessage("&6" + detail.getSyntax() + "&7: " + detail.getDescription()));
            }
        }
        messages.add(Util.parseMessage("&e----------------------------------------"));

        messages.forEach(p::sendMessage);
    }
}
