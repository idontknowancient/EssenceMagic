package com.idk.essencemagic.commands;

import com.idk.essencemagic.commands.essence_sub.*;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.SystemMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EssenceCommand implements CommandExecutor, TabCompleter {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public EssenceCommand() {
        subCommands.add(new ElementCommand());
        subCommands.add(new ItemCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new GodCommand());
        subCommands.add(new MobCommand());
        subCommands.add(new ManaCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) {
            for (SubCommand subCommand : subCommands) {
                if(args.length == 0) {
                    SystemMessage.TOO_LITTLE_ARGUMENT.send(p);
                    return true;
                }
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    subCommand.perform(p, args);
                }
            }
        }

        return true;
    }

    @Nullable
    @Override //tab completion
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) { //when complete typing /essence <- a blank is here
            List<String> subCommandsNames = new ArrayList<>();
            subCommands.forEach(s->subCommandsNames.add(s.getName()));
            return subCommandsNames;
        }
        if(args.length == 2) { //when complete typing /essence <subcommand, such as element> <- a blank is here
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[0])) {
                    return subCommand.getSubCommands();
                }
            }
        }
        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("item") && args[1].equalsIgnoreCase("get")) {
                return new ArrayList<>(Item.items.keySet());
            }
            if(args[0].equalsIgnoreCase("mob") && args[1].equalsIgnoreCase("spawn")) {
                return new ArrayList<>(Mob.mobs.keySet());
            }
        }

        return null;
    }
}
