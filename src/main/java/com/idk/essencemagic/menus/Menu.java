package com.idk.essencemagic.menus;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.magics.Magic;
import com.idk.essencemagic.menus.holders.CancelHolder;
import com.idk.essencemagic.menus.holders.DetailInfoHolder;
import com.idk.essencemagic.menus.holders.GetItemHolder;
import com.idk.essencemagic.menus.holders.ShiftSpawnHolder;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.skills.Skill;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.items.wands.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static Inventory getElementMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory elementMenu = Bukkit.createInventory(new CancelHolder(), cm.getInteger("element.size"), cm.outString("element.title"));
        for(Element e : Element.elements.values())
            elementMenu.setItem(e.getSlot(), e.getSymbolItem());

        //occupation
        if(cm.isString("element.occupation")) {
            for(int i = 0; i < cm.getInteger("element.size"); i++) {
                if(elementMenu.getItem(i) == null) elementMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("element.occupation").toUpperCase())));
            }
        }
        return elementMenu;
    }

    public static Inventory getItemMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory itemMenu = Bukkit.createInventory(new GetItemHolder(), cm.getInteger("item.size"), cm.outString("item.title"));
        for(Item i : Item.items.values())
            itemMenu.addItem(i.getItem());
        if(cm.isString("item.occupation")) {
            for(int i = 0; i < cm.getInteger("item.size"); i++) {
                if(itemMenu.getItem(i) == null) itemMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("item.occupation").toUpperCase())));
            }
        }
        return itemMenu;
    }

    public static Inventory getMobMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory mobMenu = Bukkit.createInventory(new ShiftSpawnHolder(), cm.getInteger("mob.size"), cm.outString("mob.title"));

        for(Mob m : Mob.mobs.values()) {
            ItemStack item = new ItemStack(
                    Material.valueOf(m.getType().toString() + "_spawn_egg" .toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(m.getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add(Util.colorize("&fInterior name: " + m.getName()));
            lore.add(Util.colorize("&fType: " + m.getType().toString()));
            if(m.getHealth() != -1)
                lore.add(Util.colorize("&fHealth: " + m.getHealth()));
            else
                lore.add(Util.colorize("&fHealth: default"));
            lore.add(Util.colorize("&fElement: " + m.getElement().getDisplayName()));
            lore.add("");
            lore.addAll(m.getDescription());
            meta.setLore(lore);

            item.setItemMeta(meta);
            mobMenu.addItem(item);
        }

        if(cm.isString("mob.occupation")) {
            for(int i = 0; i < cm.getInteger("mob.size"); i++) {
                if(mobMenu.getItem(i) == null) mobMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("mob.occupation").toUpperCase())));
            }
        }
        return mobMenu;
    }

    public static Inventory getSkillMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory skillMenu = Bukkit.createInventory(new DetailInfoHolder(), cm.getInteger("skill.size"), cm.outString("skill.title"));

        for(Skill skill : Skill.skills.values()) {
            ItemStack item = new ItemStack(Material.valueOf(cm.getString("skill.item").toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return skillMenu;
            meta.setDisplayName(skill.getDisplayName());
            meta.setLore(skill.getInfo());
            item.setItemMeta(meta);
            skillMenu.addItem(item);
        }

        if(cm.isString("skill.occupation")) {
            for(int i = 0; i < cm.getInteger("skill.size"); i++) {
                if(skillMenu.getItem(i) == null) skillMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("skill.occupation").toUpperCase())));
            }
        }

        return skillMenu;
    }

    public static Inventory getMagicMenu() {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MENUS;
        Inventory magicMenu = Bukkit.createInventory(new DetailInfoHolder(), cm.getInteger("magic.size"), cm.outString("magic.title"));

        for(Magic magic : Magic.magics.values()) {
            ItemStack item = new ItemStack(Material.valueOf(cm.getString("magic.item").toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return magicMenu;
            meta.setDisplayName(magic.getDisplayName());
            meta.setLore(magic.getInfo());
            item.setItemMeta(meta);
            magicMenu.addItem(item);
        }

        if(cm.isString("magic.occupation")) {
            for(int i = 0; i < cm.getInteger("magic.size"); i++) {
                if(magicMenu.getItem(i) == null) magicMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("magic.occupation").toUpperCase())));
            }
        }

        return magicMenu;
    }

    public static Inventory getWandMenu() {
        ConfigFile.ConfigName cw = ConfigFile.ConfigName.MENUS;
        Inventory wandMenu = Bukkit.createInventory(new GetItemHolder(), cw.getInteger("wand.size"), cw.outString("wand.title"));

        for(Wand wand : Wand.wands.values()) {
            wandMenu.addItem(wand.getItemStack());
        }

        if(cw.isString("wand.occupation")) {
            for(int i = 0; i < cw.getInteger("wand.size"); i++) {
                if(wandMenu.getItem(i) == null) wandMenu.setItem(
                        i, new ItemStack(Material.valueOf(cw.getString("wand.occupation").toUpperCase())));
            }
        }

        return wandMenu;
    }
}
