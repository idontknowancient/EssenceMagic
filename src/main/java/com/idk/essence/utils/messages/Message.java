package com.idk.essence.utils.messages;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Message {

    /**
     * Format: &#xxxxxx
     */
    Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})", Pattern.CASE_INSENSITIVE);

    /**
     * Create map with respect to legacy code and minimessage tags.
     */
    Map<String, String> LEGACY_MAP = new LinkedHashMap<>();
    
    static void initialize() {
        LEGACY_MAP.put("&0", "<black>");     LEGACY_MAP.put("&1", "<dark_blue>");
        LEGACY_MAP.put("&2", "<dark_green>"); LEGACY_MAP.put("&3", "<dark_aqua>");
        LEGACY_MAP.put("&4", "<dark_red>");   LEGACY_MAP.put("&5", "<dark_purple>");
        LEGACY_MAP.put("&6", "<gold>");        LEGACY_MAP.put("&7", "<gray>");
        LEGACY_MAP.put("&8", "<dark_gray>");  LEGACY_MAP.put("&9", "<blue>");
        LEGACY_MAP.put("&a", "<green>");       LEGACY_MAP.put("&b", "<aqua>");
        LEGACY_MAP.put("&c", "<red>");         LEGACY_MAP.put("&d", "<light_purple>");
        LEGACY_MAP.put("&e", "<yellow>");      LEGACY_MAP.put("&f", "<white>");
        LEGACY_MAP.put("&l", "<bold>");        LEGACY_MAP.put("&m", "<strikethrough>");
        LEGACY_MAP.put("&n", "<underlined>");  LEGACY_MAP.put("&o", "<italic>");
        LEGACY_MAP.put("&k", "<obfuscated>");  LEGACY_MAP.put("&r", "<reset>");
    }

    static void send(CommandSender sender, String content) {
        sender.sendMessage(parse(getPrefix() + content));
    }

    static void send(CommandSender sender, String content, Object info) {
        sender.sendMessage(parse(getPrefix() + content, info));
    }

    static String get(String path) {
        return ConfigManager.DefaultFile.MESSAGES.getString(path);
    }

    static Component out(String path) {
        return ConfigManager.DefaultFile.MESSAGES.outString(path);
    }

    static Component out(String path, Object info) {
        return ConfigManager.DefaultFile.MESSAGES.outString(path, info);
    }

    static String getPrefix() {
        return SystemMessage.PREFIX.get();
    }

    /**
     * Convert placeholders, legacy color code (&), hex color code, and minimessage to component.
     */
    @NotNull
    static Component parse(String input, Object info) {
        return parse(PlaceholderManager.translate(input, info));
    }

    /**
     * Convert legacy color code (&), hex color code, and minimessage to component.
     */
    @NotNull
    static Component parse(String input) {
        if(input == null) return Component.empty();

        // Handle hex color code (e.g. #FFFFFF -> <#FFFFFF>)
        Matcher matcher = hexPattern.matcher(input);
        StringBuilder builder = new StringBuilder();
        while(matcher.find()) {
            matcher.appendReplacement(builder, "<#" + matcher.group(1) + ">");
        }
        matcher.appendTail(builder);
        input = builder.toString();

        // Handle legacy color code (e.g. &6 -> <gold>)
        for(Map.Entry<String, String> entry : LEGACY_MAP.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
            // Handle uppercase (&A)
            input = input.replace(entry.getKey().toUpperCase(), entry.getValue());
        }

        return deserialize(input).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    static String serialize(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    static Component deserialize(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }
}
