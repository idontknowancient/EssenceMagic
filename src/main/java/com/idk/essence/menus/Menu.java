package com.idk.essence.menus;

import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.items.ItemBuilder;
import com.idk.essence.items.items.ItemFactory;
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
import com.idk.essence.items.arcana.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu {

    private static final ConfigManager.ConfigDefaultFile cm = ConfigManager.ConfigDefaultFile.MENUS;

    public static Inventory getElementMenu() {
        Inventory elementMenu = createInventory(DetailInfoHolder.getInstance(), "element");
        for(Element e : ElementFactory.getAll())
            elementMenu.setItem(e.getSlot(), e.getSymbolItem());
        setOccupation(elementMenu, "element");
        return elementMenu;
    }

    public static Inventory getItemMenu() {
        Inventory itemMenu = createInventory(GetItemHolder.getInstance(), "item");
        for(ItemStack i : ItemFactory.getAll())
            itemMenu.addItem(i);
        setOccupation(itemMenu, "item");
        return itemMenu;
    }

    public static Inventory getMobMenu() {
        Inventory mobMenu = createInventory(ShiftSpawnHolder.getInstance(), "mob");
        for(Mob m : MobManager.getAll())
            mobMenu.addItem(m.getItemBuilder().build());
        setOccupation(mobMenu, "element");
        return mobMenu;
    }

    public static Inventory getSkillMenu() {
        Inventory skillMenu = createInventory(DetailInfoHolder.getInstance(), "skill");
        for(SkillTemplate template : SkillManager.getAll())
            skillMenu.addItem(template.getItemBuilder().build());
        setOccupation(skillMenu, "skill");
        return skillMenu;
    }

    public static Inventory getMagicMenu() {
        Inventory magicMenu = createInventory(DetailInfoHolder.getInstance(), "magic");

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
        Inventory wandMenu = createInventory(GetItemHolder.getInstance(), "wand");

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
