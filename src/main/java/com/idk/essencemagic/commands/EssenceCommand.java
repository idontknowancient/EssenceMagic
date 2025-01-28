package com.idk.essencemagic.commands;

import com.idk.essencemagic.commands.essence_sub.*;
import com.idk.essencemagic.commands.essence_sub.util_sub.GodCommand;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.player.PlayerData;
import com.idk.essencemagic.skills.Skill;
import com.idk.essencemagic.utils.messages.SystemMessage;
import lombok.Getter;
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

    @Getter private static final List<SubCommand> subCommands = new ArrayList<>();

    public EssenceCommand() {
        subCommands.add(new ElementCommand());
        subCommands.add(new HelpCommand());
        subCommands.add(new ItemCommand());
        subCommands.add(new ManaCommand());
        subCommands.add(new MobCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new SkillCommand());
        subCommands.add(new UtilCommand());
    }

    private String getSyntax() {
        StringBuilder subs = new StringBuilder();
        subs.append("/essence [");
        for(SubCommand s : subCommands)
            subs.append(s.getName()).append(" | ");
        subs.delete(subs.length() - 3, subs.length());
        subs.append("]");
        return subs + "";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p) {
            for (SubCommand subCommand : subCommands) {
                if(args.length == 0) {
                    SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
                    return true;
                }
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    subCommand.perform(p, args);
                    return true;
                }
            }
            SystemMessage.UNKNOWN_COMMAND.send(p);
        }

        return true;
    }

    @Nullable
    @Override //tab completion
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) { //when complete typing /essence <- a blank is here
            List<String> subCommandsNames = new ArrayList<>();
            for(SubCommand sub : subCommands)
                subCommandsNames.add(sub.getName());
            return subCommandsNames;
        }
        if(args.length == 2) { //when complete typing /essence <subcommand, such as element> <- a blank is here
            for(SubCommand subCommand : subCommands) {
                if(subCommand.getName().equalsIgnoreCase(args[0])) {
                    return subCommand.getSubCommandsString();
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
            if(args[0].equalsIgnoreCase("skill") && args[1].equalsIgnoreCase("cast")) {
                return new ArrayList<>(Skill.skills.keySet());
            }
            if(args[0].equalsIgnoreCase("skill") && args[1].equalsIgnoreCase("force")) {
                return new ArrayList<>(Skill.skills.keySet());
            }
            if(args[0].equalsIgnoreCase("mana") &&
                    (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("set"))) {
                return new ArrayList<>(PlayerData.dataMap.keySet());
            }
        }
        if(args.length == 4) {
            if(args[0].equalsIgnoreCase("mana") && args[1].equalsIgnoreCase("set")) {
                return List.of("max");
            }
        }

        return null;
    }
}
