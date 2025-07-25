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
    COMMAND_WAND("command.wand"),
    COMMAND_WAND_GET("command.wand.get"),
    COMMAND_WAND_INFO("command.wand.info"),
    COMMAND_WAND_MAGIC("command.wand.magic"),
    COMMAND_WAND_MAGIC_SET("command.wand.magic.set"),
    COMMAND_WAND_MAGIC_REMOVE("command.wand.magic.remove"),
    COMMAND_WAND_MANA("command.wand.mana"),
    COMMAND_WAND_MANA_ADD("command.wand.mana.add"),
    COMMAND_WAND_MANA_SET("command.wand.mana.set"),
    COMMAND_WAND_MENU("command.wand.menu"),
    COMMAND_WAND_MENU_GET("command.wand.menu.get"),
    COMMAND_WAND_UPDATE("command.wand.update"),
    ;

    public final String name;

    Permission(String name) {
        this.name = "essencemagic." + name;
    }
}
