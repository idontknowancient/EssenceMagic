package com.idk.essence.utils.messages;

import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface Message {

    static void send(CommandSender sender, String content) {
        sender.sendMessage(Util.parseMessage(getPrefix() + content));
    }

    static void send(CommandSender sender, String content, Object info) {
        sender.sendMessage(Util.parseMessage(getPrefix() + content, info));
    }

    static String get(String path) {
        return ConfigManager.ConfigDefaultFile.MESSAGES.getString(path);
    }

    static Component out(String path) {
        return ConfigManager.ConfigDefaultFile.MESSAGES.outString(path);
    }

    static Component out(String path, Object info) {
        return ConfigManager.ConfigDefaultFile.MESSAGES.outString(path, info);
    }

    static String getPrefix() {
        return SystemMessage.PREFIX.get();
    }
}
