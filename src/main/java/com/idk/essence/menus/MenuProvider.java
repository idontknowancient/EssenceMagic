package com.idk.essence.menus;

import com.idk.essence.utils.configs.ConfigManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public interface MenuProvider<G extends BaseGui> {

    ConfigManager.DefaultFile configFile = ConfigManager.DefaultFile.CONFIG;

    G createMenu();

    @Nullable
    default GuiItem getOccupationItem(@Nullable ConfigurationSection section) {
        if(section == null) return null;
        Material material = com.idk.essence.items.items.ItemBuilder.getMaterial(section.getString("material"));
        return ItemBuilder.from(new com.idk.essence.items.items.ItemBuilder(material)
                .displayName(Component.text("")).build()).asGuiItem();
    }

    default void setOccupation(G gui, @Nullable ConfigurationSection section) {
        if(section == null) return;
        String type = section.getString("type", "remain");
        GuiItem occupation = getOccupationItem(section);

        if(occupation != null && type.equalsIgnoreCase("remain"))
            gui.getFiller().fill(occupation);
        else if(occupation != null && type.equalsIgnoreCase("bottom"))
            gui.getFiller().fillBottom(occupation);
    }
}
