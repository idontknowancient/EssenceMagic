package com.idk.essence.commands;

import com.idk.essence.commands.essence_sub.*;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.SystemPermission;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EssenceCommand implements CommandExecutor, TabCompleter {

    @Getter private static final Map<String, SubCommand> subCommands = new HashMap<>();

    public EssenceCommand() {
        subCommands.put("element", new ElementCommand("element"));
        subCommands.put("help", new HelpCommand("help"));
        subCommands.put("item", new ItemCommand("item"));
        subCommands.put("magic", new MagicCommand("magic"));
        subCommands.put("mana", new ManaCommand("mana"));
        subCommands.put("mob", new MobCommand("mob"));
        subCommands.put("reload", new ReloadCommand("reload"));
        subCommands.put("skill", new SkillCommand("skill"));
        subCommands.put("util", new UtilCommand("util"));
        subCommands.put("wand", new WandCommand("wand"));
    }

    private String getSyntax(CommandSender sender) {
        return getSyntaxFromSubCommands("/essence", subCommands, sender);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(sender, getSyntax(sender));
            return true;
        }
        Optional.ofNullable(subCommands.get(args[0].toLowerCase())).ifPresentOrElse(
                subCommand -> subCommand.perform(sender, args),
                () -> SystemMessage.UNKNOWN_COMMAND.send(sender));
        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return null;
        if(args.length == 0) return null;

        // When complete typing "/essence " <- a blank is here
        if(args.length == 1) {
            return getSubCommands().values().stream().filter(sub ->
                    SystemPermission.checkPerm(player, sub.getPermission())).map(SubCommand::getName).toList();
        }

        // First subcommand
        return Optional.ofNullable(subCommands.get(args[0])).map(sub ->
                sub.getTabCompletion(player, args)).orElse(null);
//        if(args.length == 2) { //when complete typing /essence <subcommand, such as element> <- a blank is here
//            for(SubCommand subCommand : subCommands) {
//                if(subCommand.getName().equalsIgnoreCase(args[0])) {
//                    return subCommand.getSubCommands().stream().map(SubCommand::getName).toList();
//                }
//            }
//        }
//        if(args.length == 3) {
//            if(args[0].equalsIgnoreCase("item") && args[1].equalsIgnoreCase("get")) {
//                List<String> index = new ArrayList<>(ItemFactory.getAllKeys());
//                for(Registry.SystemItem registry : Registry.SystemItem.values()) {
//                    index.add(registry.name().toLowerCase());
//                }
//                return index;
//            }
//            if(args[0].equalsIgnoreCase("magic") && args[1].equalsIgnoreCase("cast")) {
//                return new ArrayList<>(Magic.magics.keySet());
//            }
//            if(args[0].equalsIgnoreCase("magic") && args[1].equalsIgnoreCase("force")) {
//                return new ArrayList<>(Magic.magics.keySet());
//            }
//            if(args[0].equalsIgnoreCase("mana") &&
//                    (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("set"))) {
//                return new ArrayList<>(PlayerData.dataMap.keySet());
//            }
//            if(args[0].equalsIgnoreCase("mob") && args[1].equalsIgnoreCase("spawn")) {
//                return new ArrayList<>(MobManager.getAllKeys());
//            }
//            if(args[0].equalsIgnoreCase("skill") && args[1].equalsIgnoreCase("cast")) {
//                return new ArrayList<>(Skill.skills.keySet());
//            }
//            if(args[0].equalsIgnoreCase("skill") && args[1].equalsIgnoreCase("force")) {
//                return new ArrayList<>(Skill.skills.keySet());
//            }
//            if(args[0].equalsIgnoreCase("wand") && args[1].equalsIgnoreCase("get")) {
//                return new ArrayList<>(Wand.wands.keySet());
//            }
//            if(args[0].equalsIgnoreCase("wand") && args[1].equalsIgnoreCase("mana")) {
//                return List.of("set", "add");
//            }
//            if(args[0].equalsIgnoreCase("wand") && args[1].equalsIgnoreCase("magic")) {
//                return List.of("set", "remove");
//            }
//        }
//        if(args.length == 4) {
//            if(args[1].equalsIgnoreCase("magic") &&
//                    (args[2].equalsIgnoreCase("set") || args[2].equalsIgnoreCase("remove"))) {
//                if(!(sender instanceof Player player) ||
//                    player.getInventory().getItemInMainHand().getItemMeta() == null) return null;
//                List<String> slotList = new ArrayList<>();
//                for(int i = 0; i < WandHandler.getSlot(player.getInventory().getItemInMainHand()); i++)
//                    slotList.add(i+"");
//                return slotList;
//            }
//        }
//        if(args.length == 5) {
//            if(args[1].equalsIgnoreCase("magic") && args[2].equalsIgnoreCase("set")) {
//                return new ArrayList<>(Magic.magics.keySet());
//            }
//        }
    }

    /**
     * Get a serialized string from a map of subcommands. Automatically handle permission.
     * @param formerCommand for example, former command of "/essence <sub>" is "/essence"
     */
    public static String getSyntaxFromSubCommands(String formerCommand, Map<String, SubCommand> subCommands, CommandSender sender) {
        TextComponent.Builder builder = Component.text().content(formerCommand + " [");
        List<SubCommand> subWithPerms;
        if(sender instanceof Player player)
            subWithPerms = subCommands.values().stream().filter(sub ->
                SystemPermission.checkPerm(player, sub.getPermission())).toList();
        else
            subWithPerms = subCommands.values().stream().toList();

        for(int i = 0; i < subWithPerms.size(); i++) {
            String name = subWithPerms.get(i).getName();

            // Clickable subcommand
            Component subComponent = Component.text(name)
                    .decoration(TextDecoration.UNDERLINED, true)
                    .clickEvent(ClickEvent.suggestCommand(formerCommand + " " + name + " "))
                    .hoverEvent(HoverEvent.showText(SystemMessage.CLICK_TO_USE.out()));
            builder.append(subComponent);

            if(i < subWithPerms.size() - 1) {
                builder.append(Component.text(" | ").decoration(TextDecoration.UNDERLINED, false));
            }
        }
        builder.append(Component.text("]"));

        // Serialize : Component -> String
        // Deserialize : String -> Component
        return MiniMessage.miniMessage().serialize(builder.build());
    }

    /**
     * Get a serialized string from three strings. For example, "/essence item get <item>"
     * @param formerCommand command prefix, such as "/essence item get"
     * @param parameter clickable subcommand, such as the second "item"
     * @param needBrackets when clicked, parameter will not be appended if true, and will be appended if false
     * @param isNecessary necessary: <> / optional: []
     */
    public static String getSyntaxFromStrings(String formerCommand, String parameter, boolean needBrackets, boolean isNecessary) {
        TextComponent.Builder builder = Component.text().content(formerCommand + " ");
        if(needBrackets && isNecessary) builder.append(Component.text("<"));
        if(needBrackets && !isNecessary) builder.append(Component.text("["));

        // Clickable
        Component subComponent = Component.text(parameter)
                .decoration(TextDecoration.UNDERLINED, true)
                // Brackets are just hint, and should not be added to suggest
                .clickEvent(ClickEvent.suggestCommand(needBrackets ? formerCommand + " " : formerCommand + " " + parameter + " "))
                .hoverEvent(HoverEvent.showText(SystemMessage.CLICK_TO_USE.out()));
        builder.append(subComponent);
        if(needBrackets && isNecessary) builder.append(Component.text(">"));
        if(needBrackets && !isNecessary) builder.append(Component.text("]"));

        return MiniMessage.miniMessage().serialize(builder.build());
    }

    public static String getSyntaxFromStrings(String formerCommand, String parameter, boolean needBrackets) {
        return getSyntaxFromStrings(formerCommand, parameter, needBrackets, true);
    }

    /**
     * Get a serialized string from three strings. For example, "/essence wand magic <set/remove> <slot> [magic]"
     * @param formerCommand command prefix, such as "/essence wand magic"
     * @param parameter clickable subcommand, such as "set/remove", "slot", "magic"
     * @param needBrackets such as true, true, true
     * @param isNecessary such as true, true, false
     */
    public static String getSyntaxFromStrings(String formerCommand, List<String> parameter,
                                              List<Boolean> needBrackets, List<Boolean> isNecessary) {
        int length = parameter.size();
        TextComponent.Builder builder = Component.text().content(formerCommand + " ");
        for(int i = 0; i < length; i++) {
            boolean bracket = Optional.ofNullable(needBrackets.get(i)).orElse(true);
            boolean necessary = Optional.ofNullable(isNecessary.get(i)).orElse(true);
            if(bracket && necessary) builder.append(Component.text("<"));
            if(bracket && !necessary) builder.append(Component.text("["));

            // Clickable
            Component subComponent = Component.text(parameter.get(i))
                    .decoration(TextDecoration.UNDERLINED, true)
                    // Brackets are just hint, and should not be added to suggest
                    .clickEvent(ClickEvent.suggestCommand(formerCommand + " "))
                    .hoverEvent(HoverEvent.showText(SystemMessage.CLICK_TO_USE.out()));
            builder.append(subComponent);
            if(bracket && necessary) builder.append(Component.text(">"));
            if(bracket && !necessary) builder.append(Component.text("]"));

            if(i < length - 1) {
                builder.append(Component.text(" "));
            }
        }

        return MiniMessage.miniMessage().serialize(builder.build());
    }
}
