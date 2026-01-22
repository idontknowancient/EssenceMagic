package com.idk.essence.menus;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.ItemBuilder;
import com.idk.essence.items.ItemFactory;
import com.idk.essence.magics.Magic;
import com.idk.essence.menus.holders.CancelHolder;
import com.idk.essence.menus.holders.DetailInfoHolder;
import com.idk.essence.menus.holders.GetItemHolder;
import com.idk.essence.menus.holders.ShiftSpawnHolder;
import com.idk.essence.mobs.Mob;
import com.idk.essence.mobs.MobManager;
import com.idk.essence.skills.SkillManager;
import com.idk.essence.skills.SkillTemplate;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.items.wands.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu {

    private static final ConfigManager.ConfigDefaultFile cm = ConfigManager.ConfigDefaultFile.MENUS;

    public static Inventory getElementMenu() {
        Inventory elementMenu = createInventory(new CancelHolder(), "element");
        for(Element e : ElementFactory.getAll())
            elementMenu.setItem(e.getSlot(), e.getSymbolItem());
        setOccupation(elementMenu, "element");
        return elementMenu;
    }

    public static Inventory getItemMenu() {
        Inventory itemMenu = createInventory(new GetItemHolder(), "item");
        for(ItemStack i : ItemFactory.getAll())
            itemMenu.addItem(i);
        setOccupation(itemMenu, "item");
        return itemMenu;
    }

    public static Inventory getMobMenu() {
        Inventory mobMenu = createInventory(new ShiftSpawnHolder(), "mob");
        for(Mob m : MobManager.getAll())
            mobMenu.addItem(m.getItemBuilder().build());
        setOccupation(mobMenu, "element");

//        for(Mob m : Mob.mobs.values()) {
//            ItemStack item = new ItemStack(
//                    Material.valueOf(m.getType().toString() + "_spawn_egg" .toUpperCase()));
//            ItemMeta meta = item.getItemMeta();
//            meta.setDisplayName(m.getDisplayName());
//
//            List<String> lore = new ArrayList<>();
//            lore.add(Util.colorize("&fInterior name: " + m.getInternalName()));
//            lore.add(Util.colorize("&fType: " + m.getType().toString()));
//            if(m.getHealth() != -1)
//                lore.add(Util.colorize("&fHealth: " + m.getHealth()));
//            else
//                lore.add(Util.colorize("&fHealth: default"));
//            lore.add(Util.colorize("&fElement: " + m.getElement().getDisplayName()));
//            lore.add("");
//            lore.addAll(m.getDescription());
//            meta.setLore(lore);
//
//            item.setItemMeta(meta);
//            mobMenu.addItem(item);
//        }
        return mobMenu;
    }

    public static Inventory getSkillMenu() {
        Inventory skillMenu = createInventory(new DetailInfoHolder(), "skill");
        for(SkillTemplate template : SkillManager.getAll())
            skillMenu.addItem(template.getItemBuilder().build());
        setOccupation(skillMenu, "skill");
        return skillMenu;
    }

    public static Inventory getMagicMenu() {
        Inventory magicMenu = createInventory(new DetailInfoHolder(), "magic");

        for(Magic magic : Magic.magics.values()) {
            ItemStack item = new ItemStack(Material.valueOf(cm.getString("magic.item").toUpperCase()));
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return magicMenu;
            meta.displayName(magic.getDisplayName());
            meta.setLore(magic.getInfo());
            item.setItemMeta(meta);
            magicMenu.addItem(item);
        }

        setOccupation(magicMenu, "magic");
        return magicMenu;
    }

    public static Inventory getWandMenu() {
        Inventory wandMenu = createInventory(new GetItemHolder(), "wand");

        for(Wand wand : Wand.wands.values()) {
            wandMenu.addItem(wand.getItemStack());
        }

        setOccupation(wandMenu, "wand");
        return wandMenu;
    }

    private static Inventory createInventory(InventoryHolder holder, String sectionName) {
        return Bukkit.createInventory(holder, cm.getInteger(sectionName + ".size"),
                cm.outString(sectionName + ".title"));
    }

    private static void setOccupation(Inventory inventory, String sectionName) {
        if(cm.isString(sectionName + ".occupation")) {
            for(int i = 0; i < cm.getInteger(sectionName + ".size"); i++) {
                if(inventory.getItem(i) == null)
                    inventory.setItem(i, new ItemBuilder(cm.getString(sectionName + ".occupation")).build());
            }
        }
    }
}
