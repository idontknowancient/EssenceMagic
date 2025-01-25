package com.idk.essencemagic.utils.messages;

import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum SystemMessage implements Message {

    PREFIX("prefix"),
    PLAYER_JOIN_MESSAGE("player-join-message"),
    PLAYER_QUIT_MESSAGE("player-quit-message"),
    UNKNOWN_COMMAND("unknown-command"),
    TOO_LITTLE_ARGUMENT("too-little-argument"),
    INADEQUATE_PERMISSION("inadequate-permission"),
    GOD_MODE_ENABLED("god-mode-enabled"),
    GOD_MODE_DISABLED("god-mode-disabled"),
    ITEM_GOT("item-got"),
    ITEM_NOT_FOUND("item-not-found"),
    NO_ITEM_IN_HAND("no-item-in-hand"),
    NOT_CUSTOM_ITEM("not-custom-item"),
    MOB_NOT_FOUND("mob-not-found"),
    GET_MANA("get-mana"),
    SET_MANA("set-mana"),
    SKILL_CASTED("skill-casted"),
    SKILL_NOT_FOUND("skill-not-found"),
    SKILL_REQUIREMENT_NOT_SATISFIED("skill-requirement-not-satisfied"),
    PLAYER_NOT_EXIST("player-not-exist"),
    SUCCESSFULLY_RELOADED("successfully-reloaded"),
    ;

    private final String path;

    SystemMessage(String path) {
        this.path = path;
    }

    public String out() {
        return Message.out(path);
    }

    public String out(Player p) {
        return Message.out(path, p);
    }

    public void send(CommandSender sender) {
        Message.send(sender, ConfigFile.ConfigName.MESSAGES.getString(path));
    }

    //use for placeholders
    public void send(CommandSender sender, Object info) {
        Message.send(sender, ConfigFile.ConfigName.MESSAGES.getString(path), info);
    }
}
