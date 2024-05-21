package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.commands.essence_sub.mana_sub.GetCommand;
import com.idk.essencemagic.commands.essence_sub.mana_sub.SetCommand;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ManaCommand extends SubCommand {

    private static final List<SubCommand> subCommands = new ArrayList<>();

    public ManaCommand() {
        subCommands.add(new SetCommand());
        subCommands.add(new GetCommand());
    }

    @Override
    public String getName() {
        return "mana";
    }

    @Override
    public String getDescription() {
        return "Check and modify all player's mana.";
    }

    @Override
    public String getSyntax() {
        StringBuilder subs = new StringBuilder();
        subs.append("/essence ").append(getName()).append(" [");
        for(SubCommand s : subCommands)
            subs.append(s.getName()).append(" | ");
        subs.delete(subs.length() - 3, subs.length());
        subs.append("]");
        return subs + "";
    }

    @Override
    public List<String> getSubCommands() {
        List<String> subCommandsNames = new ArrayList<>();
        for(SubCommand sub : subCommands)
            subCommandsNames.add(sub.getName());

        return subCommandsNames;
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_MANA.name))
            SystemMessage.INADEQUATE_PERMISSION.send(p);
        if(args.length <= 1) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        for (SubCommand subCommand : subCommands) {
            if (args[1].equalsIgnoreCase(subCommand.getName())) {
                subCommand.perform(p, args);
            }
        }
    }
}
