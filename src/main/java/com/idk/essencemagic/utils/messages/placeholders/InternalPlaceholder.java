package com.idk.essencemagic.utils.messages.placeholders;

public enum InternalPlaceholder {

    PLAYER("player"),
    USAGE("usage"),
    ITEM_NAME("item_name"),
    ITEM_DISPLAY_NAME("item_display_name"),
    ITEM_TYPE("item_type"),
    ITEM_ID("item_id"),
    ITEM_ELEMENT("item_element"),
    MANA_LEVEL("mana_level"),
    MANA("mana"),
    DEFAULT_MANA("default_mana"),
    MAX_MANA("max_mana"),
    MANA_RECOVERY_SPEED("mana_recovery_speed"),
    SKILL_DISPLAY_NAME("skill_display_name"),
    WAND_NAME("wand_name"),
    WAND_DISPLAY_NAME("wand_display_name"),
    WAND_MAGIC("wand_magic"),
    WAND_MANA("wand_mana"),
    WAND_SLOT("wand_slot"),
    ;

    public final String name;

    InternalPlaceholder(String name) {
        this.name = "%" + name + "%";
    }
}
