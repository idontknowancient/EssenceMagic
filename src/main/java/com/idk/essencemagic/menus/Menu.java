package com.idk.essencemagic.menus;

import com.idk.essencemagic.elements.Element;
import com.idk.essencemagic.items.Item;
import com.idk.essencemagic.mobs.Mob;
import com.idk.essencemagic.skills.SingleSkill;
import com.idk.essencemagic.skills.SingleSkillOld;
import com.idk.essencemagic.skills.Skill;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
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
        Inventory elementMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("element.size"), cm.outString("element.title"));
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
        Inventory itemMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("item.size"), cm.outString("item.title"));
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
        Inventory mobMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("mob.size"), cm.outString("mob.title"));

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
        Inventory skillMenu = Bukkit.createInventory(new CustomHolder(), cm.getInteger("skill.size"), cm.outString("skill.title"));

        for(Skill skill : Skill.skills.values()) {
            ItemStack item = new ItemStack(Material.valueOf(cm.getString("skill.item").toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(skill.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("&7Trigger: " + skill.getTrigger().name);

            if(skill.getSingleSkills().isEmpty()) {
                lore.add(cm.outString("skill.no-skill"));
            } else {
                for(SingleSkill singleSkill : skill.getSingleSkills().values()) {
                    lore.add("&f" + singleSkill.getName() + "&f:");
                    lore.add("  &7Type: " + singleSkill.getSkillType().name);
                    lore.add("  &7Targets: " + singleSkill.getTargets());
                    lore.add("  &7Requirements: " + singleSkill.getRequirements());
                    lore.add("  &7Cooldown: " + singleSkill.getCooldown());
                    lore.add("  &7Probability: " + singleSkill.getProbability());
                    lore.add("  &7Costs: " + singleSkill.getCosts());
                }
            }
            lore.replaceAll(Util::colorize);
            meta.setLore(lore);
            item.setItemMeta(meta);
            skillMenu.addItem(item);
        }

        if(cm.isString("skill.occupation")) {
            for(int i = 0; i < cm.getInteger("mob.size"); i++) {
                if(skillMenu.getItem(i) == null) skillMenu.setItem(
                        i, new ItemStack(Material.valueOf(cm.getString("skill.occupation").toUpperCase())));
            }
        }
        return skillMenu;
    }
}
