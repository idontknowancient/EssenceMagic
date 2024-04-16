package com.idk.essencemagic.utils.messages;

import org.bukkit.command.CommandSender;

public enum SystemMessage implements Message{

    PLAYER_JOIN_MESSAGE("player-join-message"),
    PLAYER_QUIT_MESSAGE("player-quit-message"),
    TOO_LITTLE_ARGUMENT("too-little-argument"),
    TOO_MUCH_ARGUMENT("too-much-argument"),
    INADEQUATE_PERMISSION("inadequate-permission"),
    SUCCESSFULLY_RELOADED("successfully-reloaded"),
    UNSUCCESSFULLY_RELOADED("unsuccessfully-reloaded"),
    ;

    private final String path;

    SystemMessage(String path) {
        this.path = path;
    }

    public void send(CommandSender sender, String s) {
        Message.send(sender, s);
    }
}
