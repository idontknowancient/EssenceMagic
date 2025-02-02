package com.idk.essencemagic.utils.permissions;

public enum Permission {

    COMMAND_ELEMENT("command.element"),
    COMMAND_ELEMENT_MENU("command.element.menu"),
    COMMAND_ITEM("command.item"),
    COMMAND_ITEM_GET("command.item.get"),
    COMMAND_ITEM_INFO("command.item.info"),
    COMMAND_ITEM_MENU("command.item.menu"),
    COMMAND_ITEM_MENU_GET("command.item.menu.get"),
    COMMAND_MAGIC("command.magic"),
    COMMAND_MAGIC_MENU("command.menu"),
    COMMAND_MAGIC_CAST("command.cast"),
    COMMAND_MAGIC_FORCE("command.force"),
    COMMAND_MANA("command.mana"),
    COMMAND_MANA_GET("command.mana.get"),
    COMMAND_MANA_GET_OTHERS("command.mana.get.others"),
    COMMAND_MANA_SET("command.mana.set"),
    COMMAND_MANA_SET_OTHERS("command.mana.set.others"),
    COMMAND_MOB("command.mob"),
    COMMAND_MOB_MENU("command.mob.menu"),
    COMMAND_MOB_MENU_SPAWN("command.mob.menu.spawn"),
    COMMAND_MOB_SPAWN("command.spawn"),
    COMMAND_RELOAD("command.reload"),
    COMMAND_SKILL("command.skill"),
    COMMAND_SKILL_MENU("command.skill.menu"),
    COMMAND_SKILL_CAST("command.skill.cast"),
    COMMAND_SKILL_FORCE("command.skill.force"),
    COMMAND_UTIL("command.util"),
    COMMAND_UTIL_GOD("command.util.god"),
    COMMAND_UTIL_GOD_OTHERS("command.util.god.others"),
    ;

    public final String name;

    Permission(String name) {
        this.name = "essencemagic." + name;
    }
}
