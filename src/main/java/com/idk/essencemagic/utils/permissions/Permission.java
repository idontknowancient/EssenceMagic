package com.idk.essencemagic.utils.permissions;

public enum Permission {

    COMMAND_ELEMENT("command.element"),
    COMMAND_ELEMENT_MENU("command.element.menu"),
    COMMAND_GOD("command.god"),
    COMMAND_ITEM("command.item"),
    COMMAND_ITEM_GET("command.item.get"),
    COMMAND_ITEM_INFO("command.item.info"),
    COMMAND_ITEM_MENU("command.item.menu"),
    COMMAND_MANA("command.mana"),
    COMMAND_MANA_GET("command.mana.get"),
    COMMAND_MANA_SET("command.mana.set"),
    COMMAND_MOB("command.mob"),
    COMMAND_MOB_MENU("command.menu"),
    COMMAND_MOB_SPAWN("command.spawn"),
    COMMAND_RELOAD("command.reload"),
    ;

    public final String name;

    Permission(String name) {
        this.name = "essencemagic." + name;
    }
}
