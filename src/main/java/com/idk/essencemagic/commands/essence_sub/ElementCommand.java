package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.commands.essence_sub.element_sub.MenuCommand;
import com.idk.essencemagic.utils.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ElementCommand extends SubCommand {

    private final Util cm = Util.getUtil("messages"); //config messages

    private static final List<SubCommand> subCommands = new ArrayList<>();

    public ElementCommand() {
        subCommands.add(new MenuCommand());
    }

    @Override
    public String getName() {
        return "element";
    }

    @Override
    public String getDescription() {
        return "Check and modify all elements";
    }

    @Override
    public String getSyntax() {
        return "/essence element ";
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
            p.sendMessage(cm.outs("too-little-argument"));
            return;
        }
        for (SubCommand subCommand : subCommands) {
            if (args[1].equalsIgnoreCase(subCommand.getName())) {
                subCommand.perform(p, args);
            }
        }
    }
}
