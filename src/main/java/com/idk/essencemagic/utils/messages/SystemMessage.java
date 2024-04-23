package com.idk.essencemagic.utils.messages;

import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.command.CommandSender;

public enum SystemMessage implements Message {

    PREFIX("prefix"),
    PLAYER_JOIN_MESSAGE("player-join-message"),
    PLAYER_QUIT_MESSAGE("player-quit-message"),
    TOO_LITTLE_ARGUMENT("too-little-argument"),
    TOO_MUCH_ARGUMENT("too-much-argument"),
    INADEQUATE_PERMISSION("inadequate-permission"),
    SUCCESSFULLY_RELOADED("successfully-reloaded"),
    UNSUCCESSFULLY_RELOADED("unsuccessfully-reloaded"),
    NO_ITEM_IN_HAND("no-item-in-hand"),
    NOT_CUSTOM_ITEM("not-custom-item"),
    ITEM_NOT_FOUND("item-not-found"),
    PLAYER_NOT_EXIST("player-not-exist"),
    GOD_MODE_ENABLED("god-mode-enabled"),
    GOD_MODE_DISABLED("god-mode-disabled"),
    ;

    private final String path;

    SystemMessage(String path) {
        this.path = path;
    }

    public String out() {
        return Message.out(path);
    }

    public void send(CommandSender sender) {
        Message.send(sender, ConfigFile.ConfigName.MESSAGES.getString(path));
    }
}
