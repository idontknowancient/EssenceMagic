package com.idk.essence.menus.providers;

import com.idk.essence.magics.MagicManager;
import com.idk.essence.menus.PaginatedProvider;
import com.idk.essence.menus.ClickAction;
import dev.triumphteam.gui.components.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MagicMenuProvider extends PaginatedProvider {

    @Override
    public String getSectionName() {
        return "magic";
    }

    @Override
    public Collection<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        items.addAll(MagicManager.getAllDomains().stream().map(domain -> domain.getItemBuilder().build()).toList());
        items.addAll(MagicManager.getAllSignets().stream().map(signet -> signet.getItemBuilder().build()).toList());
        return items;
    }

    @Override
    public @Nullable GuiAction<@NotNull InventoryClickEvent> getClickAction() {
        return ClickAction.detailInfoAction();
    }
}
