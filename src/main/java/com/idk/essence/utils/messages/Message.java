package com.idk.essence.utils.messages;

import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigFile;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface Message {

    static void send(CommandSender sender, String content) {
        sender.sendMessage(getPrefix().append(Util.parseMessage(content)));
    }

    static void send(CommandSender sender, String path, Object info) {
        sender.sendMessage(getPrefix().append(Util.parseMessage(path, info)));
    }

    static Component out(String path) {
        return ConfigFile.ConfigName.MESSAGES.outString(path);
    }

    static Component out(String path, Object info) {
        return ConfigFile.ConfigName.MESSAGES.outString(path, info);
    }

    static Component getPrefix() {
        return SystemMessage.PREFIX.out();
    }
}
