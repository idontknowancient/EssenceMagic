package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.commands.EssenceCommand;
import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.messages.Message;
import org.bukkit.entity.Player;

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
        p.sendMessage(Message.getPrefix() + Util.colorize("&eHelp List"));
        p.sendMessage(Util.colorize("&e----------------------------------------"));
        for(SubCommand feature : EssenceCommand.getSubCommands()) {
            p.sendMessage(Util.colorize("&e/essence " + feature.getName()
                    + "&f: " + feature.getDescription()));
            for(SubCommand detail : feature.getSubCommands()) {
                p.sendMessage(Util.colorize("&6" + detail.getSyntax()
                        + "&7: " + detail.getDescription()));
            }
        }
        p.sendMessage(Util.colorize("&e----------------------------------------"));
    }
}
