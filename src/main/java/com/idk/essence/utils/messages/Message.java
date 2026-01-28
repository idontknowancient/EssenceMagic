package com.idk.essence.utils.messages;

import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface Message {

    static void send(CommandSender sender, String content) {
        sender.sendMessage(Util.System.parseMessage(getPrefix() + content));
    }

    static void send(CommandSender sender, String content, Object info) {
        sender.sendMessage(Util.System.parseMessage(getPrefix() + content, info));
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
}
