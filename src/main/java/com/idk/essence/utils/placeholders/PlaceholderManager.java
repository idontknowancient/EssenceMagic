package com.idk.essence.utils.placeholders;

import com.idk.essence.Essence;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.magics.Magic;
import com.idk.essence.items.arcana.Wand;
import com.idk.essence.items.arcana.WandHandler;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.messages.Message;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @NotNull
    public static String translate(@Nullable String string, Object ... infos) {
        if(string == null || string.isEmpty()) return "";
        for(Object info : infos) {

            if(info == null) continue;
            Object actualInfo = info;
            // Check if the info is a custom item and cast to ? implements PlaceholderProvider
            if(info instanceof ItemStack item) {
                if(ArtifactFactory.isArtifact(item))
                    actualInfo = ArtifactFactory.getBuilder(Key.Type.ARTIFACT.getContent(item));
                else if(ItemFactory.isCustom(item))
                    actualInfo = ItemFactory.getBuilder(Key.Type.ITEM.getContent(item));
                if(actualInfo == null) actualInfo = info;
            }

            switch(actualInfo) {
                // Handle providers (including Item / Artifact / Arcana)
                case PlaceholderProvider provider -> string = provider.applyTo(string);

                // Handle PAPI
                case Player p -> string = PlaceholderAPI.setPlaceholders(p, string);

                // Handle normal items
                case ItemStack i -> string = handle(string, i);

                // Handle command usage
                case String s -> string = handle(string, s);

                default -> {}
            }
        }
        return string;
    }

    private static String handle(String string, ItemStack info) {
        string = string.replace(Placeholder.ITEM_DISPLAY_NAME.name, Message.serialize(info.getItemMeta().displayName()));
        string = string.replace(Placeholder.ITEM_TYPE.name, info.getType().name());
        return string;
    }

    private static String handle(String string, String info) {
        string = string.replace(Placeholder.USAGE.name, info);
        return string;
    }

    // use when initializing
    private static String handleWand(String string, Wand info) {
        String[] containerMagic = info.getDefaultMagic().toString().split(";");

        if(string.contains(Placeholder.WAND_NAME.name))
            string = string.replace(Placeholder.WAND_NAME.name, info.getName());
        if(string.contains(Placeholder.WAND_DISPLAY_NAME.name))
            string = string.replace(Placeholder.WAND_DISPLAY_NAME.name, String.valueOf(info.getDisplayName()));
        if(string.contains(Placeholder.WAND_MANA.name))
            // rounding two digits
            string = string.replace(Placeholder.WAND_MANA.name, String.valueOf(Math.round(info.getDefaultMana() * 100.00) / 100.00));
        if(string.contains(Placeholder.WAND_SLOT.name))
            string = string.replace(Placeholder.WAND_SLOT.name, info.getSlot()+"");
        if(string.contains(Placeholder.WAND_MAGIC.name)) {
            StringBuilder loreMagic = new StringBuilder();
            for(int i = 0; i < info.getSlot(); i++) {
                // using "\n" in lore, and ";" in container
                if(Magic.magics.containsKey(containerMagic[i]))
                    loreMagic.append(Magic.magics.get(containerMagic[i]).getDisplayName()).append("\n");
                else
                    loreMagic.append(Wand.getEmptyString()).append("\n");
            }
            string = string.replace(Placeholder.WAND_MAGIC.name, loreMagic.toString());
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
//                string = string.replace(Placeholder.WAND_MAGIC_USING.name,
//                        Magic.magics.get(using).getDisplayName());
//            else
//                string = string.replace(Placeholder.WAND_MAGIC_USING.name, "");
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
            string = string.replace(Placeholder.WAND_NAME.name, name);
        }
        if(string.contains(Placeholder.WAND_DISPLAY_NAME.name)) {
            string = string.replace(Placeholder.WAND_DISPLAY_NAME.name, info.getItemMeta().getDisplayName());
        }
        if(string.contains(Placeholder.WAND_MANA.name)) {
            Double Mana = container.get(Wand.getManaKey(), PersistentDataType.DOUBLE);
            // assign default value if the key doesn't exist
            double mana = Mana != null ? Mana : 0.0;
            string = string.replace(Placeholder.WAND_MANA.name, mana+"");
        }
        if(string.contains(Placeholder.WAND_SLOT.name)) {
            string = string.replace(Placeholder.WAND_SLOT.name, slot+"");
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
            string = string.replace(Placeholder.WAND_MAGIC.name, loreMagic.toString());
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
//                string = string.replace(Placeholder.WAND_MAGIC_USING.name,
//                        Magic.magics.get(using).getDisplayName());
//            else
//                string = string.replace(Placeholder.WAND_MAGIC_USING.name, "");
        }
        if(string.contains(Placeholder.WAND_MANA_INJECTION.name))
            string = string.replace(Placeholder.WAND_MANA_INJECTION.name, WandHandler.getInjection(info)+"");
        return string;
    }
}
