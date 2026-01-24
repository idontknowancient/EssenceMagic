package com.idk.essence.utils.placeholders;

import com.idk.essence.Essence;
import com.idk.essence.elements.Element;
import com.idk.essence.elements.ElementFactory;
import com.idk.essence.items.SystemItem;
import com.idk.essence.magics.Magic;
import com.idk.essence.players.ManaHandler;
import com.idk.essence.players.PlayerData;
import com.idk.essence.items.arcana.Wand;
import com.idk.essence.items.arcana.WandHandler;
import com.idk.essence.skills.SkillTemplate;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.logging.Level;

public class PlaceholderManager {

    public static void initialize() {
        if(!PlaceholderAPI.isRegistered(Essence.getPlugin().getPluginMeta().getName().toLowerCase())) {
            try {
                new EssencePlaceholder().register();
            } catch(NoClassDefFoundError e) {
                Essence.getPlugin().getLogger().log(Level.SEVERE, "", e);
            }
        }
    }

    public static String translate(String string, Object info) {
        if(string == null || string.equalsIgnoreCase("")) return "";

        if(info instanceof String s)
            string = handleString(string, s);
        else if(info instanceof Component c)
            string = handleComponent(string, c);
        else if(info instanceof ItemStack i)
            string = handleItemStack(string, i);
        else if(info instanceof SystemItem s)
            string = handleSystemItem(string, s);
        else if(info instanceof SkillTemplate s)
            string = handleSkillTemplate(string ,s);
        else if(info instanceof Wand w)
            string = handleWand(string, w);
        else if(info instanceof PlayerData d)
            string = handlePlayerData(string, d);
        else if(info instanceof Double d)
            string = handleDouble(string, d);
        else if(info instanceof Player p)
            string = handlePlaceholderAPI(string, p);
        return string;
    }

    private static String handleString(String string, String info) {
        if(string.contains(Placeholder.PLAYER.name))
            string = string.replaceAll(Placeholder.PLAYER.name, info);
        if(string.contains(Placeholder.USAGE.name))
            string = string.replaceAll(Placeholder.USAGE.name, info);
        return string;
    }

    private static String handleComponent(String string, Component info) {
        return string;
    }

    private static String handleItemStack(String string, ItemStack info) {
        ItemMeta meta = info.getItemMeta();
        if(meta == null) return string;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(container.has(Wand.getWandKey()))
            string = handleWand(string, info);
        if(string.contains(Placeholder.ITEM_ELEMENT.name))
            string = string.replaceAll(Placeholder.ITEM_ELEMENT.name,
                    Optional.ofNullable(ElementFactory.get(info)).map(Element::getDisplayName).orElse(Component.text("")).toString());
        return string;
    }

    private static String handleSystemItem(String string, SystemItem info) {
        if(string.contains(Placeholder.ITEM_NAME.name))
            string = string.replaceAll(Placeholder.ITEM_NAME.name, info.getName());
//        if(string.contains(Placeholder.ITEM_DISPLAY_NAME.name))
//            string = string.replaceAll(Placeholder.ITEM_DISPLAY_NAME.name, info.getDisplayName());
        if(string.contains(Placeholder.ITEM_TYPE.name))
            string = string.replaceAll(Placeholder.ITEM_TYPE.name, info.getMaterial().name());
        return string;
    }

    private static String handleSkillTemplate(String string, SkillTemplate info) {
        if(string.contains(Placeholder.SKILL_DISPLAY_NAME.name))
            string = string.replaceAll(Placeholder.SKILL_DISPLAY_NAME.name, MiniMessage.miniMessage().serialize(info.getDisplayName()));
        return string;
    }

    // use when initializing
    private static String handleWand(String string, Wand info) {
        String[] containerMagic = info.getDefaultMagic().toString().split(";");

        if(string.contains(Placeholder.WAND_NAME.name))
            string = string.replaceAll(Placeholder.WAND_NAME.name, info.getName());
        if(string.contains(Placeholder.WAND_DISPLAY_NAME.name))
            string = string.replaceAll(Placeholder.WAND_DISPLAY_NAME.name, String.valueOf(info.getDisplayName()));
        if(string.contains(Placeholder.WAND_MANA.name))
            // rounding two digits
            string = string.replaceAll(Placeholder.WAND_MANA.name, String.valueOf(Math.round(info.getDefaultMana() * 100.00) / 100.00));
        if(string.contains(Placeholder.WAND_SLOT.name))
            string = string.replaceAll(Placeholder.WAND_SLOT.name, info.getSlot()+"");
        if(string.contains(Placeholder.WAND_MAGIC.name)) {
            StringBuilder loreMagic = new StringBuilder();
            for(int i = 0; i < info.getSlot(); i++) {
                // using "\n" in lore, and ";" in container
                if(Magic.magics.containsKey(containerMagic[i]))
                    loreMagic.append(Magic.magics.get(containerMagic[i]).getDisplayName()).append("\n");
                else
                    loreMagic.append(Wand.getEmptyString()).append("\n");
            }
            string = string.replaceAll(Placeholder.WAND_MAGIC.name, loreMagic.toString());
        }
        if(string.contains(Placeholder.WAND_MAGIC_USING.name)) {
            // include indicating number`
            int usingNum = 0;
            try {
                usingNum = Integer.parseInt(containerMagic[containerMagic.length - 1]);
            } catch (NumberFormatException ignored) {

            }
            String using = containerMagic[usingNum];
//            if(Magic.magics.containsKey(using))
//                string = string.replaceAll(Placeholder.WAND_MAGIC_USING.name,
//                        Magic.magics.get(using).getDisplayName());
//            else
//                string = string.replaceAll(Placeholder.WAND_MAGIC_USING.name, "");
        }
        return string;
    }

    // use when updating
    private static String handleWand(String string, ItemStack info) {
        // checked before
        assert info.getItemMeta() != null;
        PersistentDataContainer container = info.getItemMeta().getPersistentDataContainer();
        Integer Slot = container.get(Wand.getSlotKey(), PersistentDataType.INTEGER);
        int slot = Slot != null ? Slot : 1;
        String ContainerMagic = container.get(Wand.getWandMagicKey(), PersistentDataType.STRING);
        String[] containerMagic = ContainerMagic != null ? ContainerMagic.split(";") : new String[0];

        if(string.contains(Placeholder.WAND_NAME.name)) {
            String Name = container.get(Wand.getWandKey(), PersistentDataType.STRING);
            String name = Name != null ? Name : "";
            string = string.replaceAll(Placeholder.WAND_NAME.name, name);
        }
        if(string.contains(Placeholder.WAND_DISPLAY_NAME.name)) {
            string = string.replaceAll(Placeholder.WAND_DISPLAY_NAME.name, info.getItemMeta().getDisplayName());
        }
        if(string.contains(Placeholder.WAND_MANA.name)) {
            Double Mana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE);
            // assign default value if the key doesn't exist
            double mana = Mana != null ? Mana : 0.0;
            string = string.replaceAll(Placeholder.WAND_MANA.name, mana+"");
        }
        if(string.contains(Placeholder.WAND_SLOT.name)) {
            string = string.replaceAll(Placeholder.WAND_SLOT.name, slot+"");
        }
        if(string.contains(Placeholder.WAND_MAGIC.name)) {
            StringBuilder loreMagic = new StringBuilder();
            for(int i = 0; i < slot; i++) {
                // using "\n" in lore, and ";" in container
                if(Magic.magics.containsKey(containerMagic[i]))
                    loreMagic.append(Magic.magics.get(containerMagic[i]).getDisplayName()).append("\n");
                else
                    loreMagic.append(Wand.getEmptyString()).append("\n");
            }
            string = string.replaceAll(Placeholder.WAND_MAGIC.name, loreMagic.toString());
        }
        if(string.contains(Placeholder.WAND_MAGIC_USING.name)) {
            // include indicating number`
            int index = 0;
            try {
                index = Integer.parseInt(containerMagic[containerMagic.length - 1]);
            } catch (NumberFormatException ignored) {
            }
            String using = containerMagic[index];
//            if(Magic.magics.containsKey(using))
//                string = string.replaceAll(Placeholder.WAND_MAGIC_USING.name,
//                        Magic.magics.get(using).getDisplayName());
//            else
//                string = string.replaceAll(Placeholder.WAND_MAGIC_USING.name, "");
        }
        if(string.contains(Placeholder.WAND_MANA_INJECTION.name))
            string = string.replaceAll(Placeholder.WAND_MANA_INJECTION.name, WandHandler.getInjection(info)+"");
        return string;
    }

    private static String handlePlayerData(String string, PlayerData info) {
        if(string.contains(Placeholder.PLAYER.name))
            string = string.replaceAll(Placeholder.PLAYER.name, String.valueOf(info.getPlayerName()));
        if(string.contains(Placeholder.MANA_LEVEL.name))
            string = string.replaceAll(Placeholder.MANA_LEVEL.name, String.valueOf(info.getManaLevel()));
        if(string.contains(Placeholder.MANA.name))
            // rounding two digits
            string = string.replaceAll(Placeholder.MANA.name, String.valueOf(Math.round(info.getMana() * 100.00) / 100.00));
        if(string.contains(Placeholder.DEFAULT_MANA.name))
            string = string.replaceAll(Placeholder.DEFAULT_MANA.name, String.valueOf(ManaHandler.getDefaultMana()));
        if(string.contains(Placeholder.MAX_MANA.name))
            // rounding two digits
            string = string.replaceAll(Placeholder.MAX_MANA.name, String.valueOf(Math.round(info.getMaxMana() * 100.00) / 100.00));
        if(string.contains(Placeholder.MANA_RECOVERY_SPEED.name))
            string = string.replaceAll(Placeholder.MANA_RECOVERY_SPEED.name, String.valueOf(info.getManaRecoverySpeed()));
        return string;
    }

    private static String handleDouble(String string, double info) {
        if(string.contains(Placeholder.DEFAULT_MANA.name))
            string = string.replaceAll(Placeholder.DEFAULT_MANA.name, String.valueOf(ManaHandler.getDefaultMana()));
        return string;
    }

    private static String handlePlaceholderAPI(String string, Player player) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }
}
