package com.idk.essence.menus.providers;

import com.idk.essence.menus.PaginatedProvider;
import com.idk.essence.menus.ClickAction;
import com.idk.essence.skills.SkillManager;
import dev.triumphteam.gui.components.GuiAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class SkillMenuProvider extends PaginatedProvider {

    @Override
    public String getSectionName() {
        return "skill";
    }

    @Override
    public Collection<ItemStack> getItems() {
        return SkillManager.getAll().stream().map(skill -> skill.getItemBuilder().build()).toList();
    }

    @Override
    public @Nullable GuiAction<@NotNull InventoryClickEvent> getClickAction() {
        return ClickAction.detailInfoAction();
    }
}
