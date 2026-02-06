package com.idk.essence.menus.providers;

import com.idk.essence.elements.ElementFactory;
import com.idk.essence.menus.PaginatedProvider;
import com.idk.essence.menus.ClickAction;
import dev.triumphteam.gui.components.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ElementMenuProvider extends PaginatedProvider {

    @Override
    public String getSectionName() {
        return "element";
    }

    @Override
    public Collection<ItemStack> getItems() {
        return ElementFactory.getAll().stream().map(element -> element.getItemBuilder().build()).toList();
    }

    @Override
    public @Nullable GuiAction<@NotNull InventoryClickEvent> getClickAction() {
        return ClickAction.detailInfoAction();
    }
}
