package com.idk.essence.utils.messages;

import com.idk.essence.utils.configs.ConfigFile;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum SystemMessage implements Message {

    PREFIX("prefix"),
    PLAYER_JOIN_MESSAGE("player-join-message"),
    PLAYER_QUIT_MESSAGE("player-quit-message"),
    UNKNOWN_COMMAND("unknown-command"),
    TOO_LITTLE_ARGUMENT("too-little-argument"),
    CLICK_TO_USE("click-to-use"),
    INADEQUATE_PERMISSION("inadequate-permission"),
    INADEQUATE_MANA("inadequate-mana"),
    NOT_NUMBER("not-number"),
    NOT_NEGATIVE_NUMBER("not-negative-number"),
    PLAYER_ONLY("player-only"),
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
    SKILL_FORCED("skill-forced"),
    SKILL_NOT_FOUND("skill-not-found"),
    SKILL_REQUIREMENT_NOT_SATISFIED("skill-requirement-not-satisfied"),
    SKILL_IN_COOLDOWN("skill-in-cooldown"),
    SKILL_ACTIVATION_FAILED("skill-activation-failed"),
    MAGIC_CASTED("magic-casted"),
    MAGIC_FORCED("magic-forced"),
    MAGIC_NOT_FOUND("magic-not-found"),
    WAND_GOT("wand-got"),
    WAND_NOT_FOUND("wand-not-found"),
    NO_WAND_IN_HAND("no-wand-in-hand"),
    NOT_WAND("not-wand"),
    ADD_WAND_MANA("add-wand-mana"),
    SET_WAND_MANA("set-wand-mana"),
    WAND_MANA_INJECTED("wand-mana-injected"),
    WAND_MAGIC_EMPTY("wand-magic-empty"),
    WAND_MAGIC_SWITCH("wand-magic-switch"),
    WAND_MAGIC_UPDATED("wand-magic-updated"),
    WAND_MAGIC_REMOVED("wand-magic-removed"),
    SLOT_NOT_EXIST("slot-not-exist"),
    WAND_UPDATED("wand-updated"),
    PLAYER_NOT_EXIST("player-not-exist"),
    SUCCESSFULLY_RELOADED("successfully-reloaded"),
    ;

    private final String path;

    SystemMessage(String path) {
        this.path = path;
    }

    public String get() {
        return Message.get(path);
    }

    public Component out() {
        return Message.out(path);
    }

    public Component out(Player p) {
        return Message.out(path, p);
    }

    public void send(CommandSender sender) {
        Message.send(sender, ConfigFile.ConfigName.MESSAGES.getString(path));
    }

    /**
     * With placeholders
     */
    public void send(CommandSender sender, Object info) {
        Message.send(sender, ConfigFile.ConfigName.MESSAGES.getString(path), info);
    }
}
