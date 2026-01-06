package com.idk.essence.commands;

import com.idk.essence.commands.essence_sub.*;
import com.idk.essence.items.Item;
import com.idk.essence.items.ItemFactory;
import com.idk.essence.magics.Magic;
import com.idk.essence.mobs.Mob;
import com.idk.essence.mobs.MobFactory;
import com.idk.essence.players.PlayerData;
import com.idk.essence.skills.Skill;
import com.idk.essence.utils.Registry;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.items.wands.Wand;
import com.idk.essence.items.wands.WandHandler;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EssenceCommand implements CommandExecutor, TabCompleter {

    @Getter private static final List<SubCommand> subCommands = new ArrayList<>();

    public EssenceCommand() {
        subCommands.add(new ElementCommand());
        subCommands.add(new HelpCommand());
        subCommands.add(new ItemCommand());
        subCommands.add(new MagicCommand());
        subCommands.add(new ManaCommand());
        subCommands.add(new MobCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new SkillCommand());
        subCommands.add(new UtilCommand());
        subCommands.add(new WandCommand());
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
                List<String> index = new ArrayList<>(ItemFactory.getAllKeys());
                for(Registry.SystemItem registry : Registry.SystemItem.values()) {
                    index.add(registry.name().toLowerCase());
                }
                return index;
            }
            if(args[0].equalsIgnoreCase("magic") && args[1].equalsIgnoreCase("cast")) {
                return new ArrayList<>(Magic.magics.keySet());
            }
            if(args[0].equalsIgnoreCase("magic") && args[1].equalsIgnoreCase("force")) {
                return new ArrayList<>(Magic.magics.keySet());
            }
            if(args[0].equalsIgnoreCase("mana") &&
                    (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("set"))) {
                return new ArrayList<>(PlayerData.dataMap.keySet());
            }
            if(args[0].equalsIgnoreCase("mob") && args[1].equalsIgnoreCase("spawn")) {
                return new ArrayList<>(MobFactory.getAllKeys());
            }
            if(args[0].equalsIgnoreCase("skill") && args[1].equalsIgnoreCase("cast")) {
                return new ArrayList<>(Skill.skills.keySet());
            }
            if(args[0].equalsIgnoreCase("skill") && args[1].equalsIgnoreCase("force")) {
                return new ArrayList<>(Skill.skills.keySet());
            }
            if(args[0].equalsIgnoreCase("wand") && args[1].equalsIgnoreCase("get")) {
                return new ArrayList<>(Wand.wands.keySet());
            }
            if(args[0].equalsIgnoreCase("wand") && args[1].equalsIgnoreCase("mana")) {
                return List.of("set", "add");
            }
            if(args[0].equalsIgnoreCase("wand") && args[1].equalsIgnoreCase("magic")) {
                return List.of("set", "remove");
            }
        }
        if(args.length == 4) {
            if(args[1].equalsIgnoreCase("magic") &&
                    (args[2].equalsIgnoreCase("set") || args[2].equalsIgnoreCase("remove"))) {
                if(!(sender instanceof Player player) ||
                    player.getInventory().getItemInMainHand().getItemMeta() == null) return null;
                List<String> slotList = new ArrayList<>();
                for(int i = 0; i < WandHandler.getSlot(player.getInventory().getItemInMainHand()); i++)
                    slotList.add(i+"");
                return slotList;
            }
        }
        if(args.length == 5) {
            if(args[1].equalsIgnoreCase("magic") && args[2].equalsIgnoreCase("set")) {
                return new ArrayList<>(Magic.magics.keySet());
            }
        }

        return null;
    }
}
