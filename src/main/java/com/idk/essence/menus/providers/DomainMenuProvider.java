package com.idk.essence.menus.providers;

import com.idk.essence.magics.MagicManager;
import com.idk.essence.menus.PaginatedProvider;
import com.idk.essence.menus.ClickAction;
import dev.triumphteam.gui.components.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class DomainMenuProvider extends PaginatedProvider {

    @Override
    public String getSectionName() {
        return "magic-domain";
    }

    @Override
    public Collection<ItemStack> getItems() {
        return MagicManager.getAllDomains().stream().map(domain -> domain.getItemBuilder().build()).toList();
    }

    @Override
    public @Nullable GuiAction<@NotNull InventoryClickEvent> getClickAction() {
        return ClickAction.detailInfoAction();
    }
}
