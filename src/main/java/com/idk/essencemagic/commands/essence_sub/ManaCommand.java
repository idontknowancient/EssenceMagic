package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.commands.essence_sub.mana_sub.SetCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ManaCommand extends SubCommand {

    private static final List<SubCommand> subCommands = new ArrayList<>();

    public ManaCommand() {
        subCommands.add(new SetCommand());
    }

    @Override
    public String getName() {
        return "mana";
    }

    @Override
    public String getDescription() {
        return "Adjust all player's mana";
    }

    @Override
    public String getSyntax() {
        return "/essence mana";
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
