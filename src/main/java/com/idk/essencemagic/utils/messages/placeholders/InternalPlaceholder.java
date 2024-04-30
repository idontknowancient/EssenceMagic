package com.idk.essencemagic.utils.messages.placeholders;

public enum InternalPlaceholder {

    PLAYER_NAME("player_name"),
    ITEM_NAME("item_name"),
    ITEM_DISPLAY_NAME("item_display_name"),
    MANA_LEVEL("mana_level"),
    MANA("mana"),
    DEFAULT_MANA("default_mana"),
    MAX_MANA("max_mana"),
    MANA_RECOVERY_SPEED("mana_recovery_speed"),
    ;

    public final String name;

    InternalPlaceholder(String name) {
        this.name = "%" + name + "%";
    }
}
