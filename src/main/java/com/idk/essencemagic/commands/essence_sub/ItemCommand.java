package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.commands.essence_sub.item_sub.GetCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ItemCommand extends SubCommand {

    private static final List<SubCommand> subCommands = new ArrayList<>();

    public ItemCommand() {
        subCommands.add(new GetCommand());
    }

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public String getDescription() {
        return "Check and modify all items";
    }

    @Override
    public String getSyntax() {
        return "/essence item ";
    }

    @Override
    public List<String> getSubCommands() {
        List<String> subCommandsNames = new ArrayList<>();
        subCommands.forEach(s->subCommandsNames.add(s.getName()));

        return subCommandsNames;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(args.length <= 1) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p);
            return;
        }
        for (SubCommand subCommand : subCommands) {
            if (args[1].equalsIgnoreCase(subCommand.getName())) {
                subCommand.perform(p, args);
            }
        }
    }
}
