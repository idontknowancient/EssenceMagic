package com.idk.essence.menus;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.messages.Message;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class PaginatedProvider implements MenuProvider<PaginatedGui> {

    private static final ConfigManager.DefaultFile menuFile = ConfigManager.DefaultFile.MENUS;
    private static final String previousPrefix = "previous-button";
    private static final String nextPrefix = "next-button";
    private static final String pagePrefix = "page-number";

    public abstract String getSectionName();
    public abstract Collection<ItemStack> getItems();

    /**
     * E.g. detail info / get item...
     */
    @Nullable
    public abstract GuiAction<@NotNull InventoryClickEvent> getClickAction();

    @Override
    public PaginatedGui createMenu() {
        int row = Math.clamp(menuFile.getInteger(getSectionName() + ".row"), 2, 6);
        // Basic creation
        PaginatedGui gui = Gui.paginated()
                .title(menuFile.outString(getSectionName() + ".title"))
                .rows(row)
                .pageSize(row == 1 ? 9 : (row - 1) * 9)
                .disableAllInteractions()
                .create();

        // Add item and set action
        if(getClickAction() != null)
            getItems().forEach(item -> gui.addItem(ItemBuilder.from(item).asGuiItem(getClickAction())));
        else
            getItems().forEach(item -> gui.addItem(ItemBuilder.from(item).asGuiItem()));

        // Add buttons
        update(gui);

        return gui;
    }

    protected void update(PaginatedGui gui) {
        int currentPage = gui.getCurrentPageNum();
        int totalPage = gui.getPagesNum();
        if(currentPage != 1)
            addPreviousButton(gui);
        else
            removePreviousButton(gui);

        if(currentPage != totalPage)
            addNextButton(gui);
        else
            removeNextButton(gui);

        addPageNumber(gui);
        setOccupation(gui, menuFile.getConfigurationSection(getSectionName() + ".occupation"));
        gui.update();
    }

    /**
     * Create previous button from config, and add the feature.
     * @param gui where the button is
     */
    private void addPreviousButton(PaginatedGui gui) {
        GuiItem item = ItemBuilder.from(
                        new com.idk.essence.items.items.ItemBuilder(configFile.getString(previousPrefix + ".material"))
                                .displayName(configFile.outString(previousPrefix + ".display-name", "")).build())
                .asGuiItem(e -> {gui.previous(); update(gui);});
        gui.setItem(gui.getRows(), Math.clamp(configFile.getInteger(previousPrefix + ".column"), 1, 9), item);
    }

    private void removePreviousButton(PaginatedGui gui) {
        gui.removeItem(gui.getRows(), Math.clamp(configFile.getInteger(previousPrefix + ".column"), 1, 9));
    }

    /**
     * Create next button from config, and add the feature.
     * @param gui where the button is
     */
    private void addNextButton(PaginatedGui gui) {
        GuiItem item = ItemBuilder.from(
                        new com.idk.essence.items.items.ItemBuilder(configFile.getString(nextPrefix + ".material"))
                                .displayName(configFile.outString(nextPrefix + ".display-name", "")).build())
                .asGuiItem(e ->  {gui.next(); update(gui);});
        gui.setItem(gui.getRows(), Math.clamp(configFile.getInteger(nextPrefix + ".column"), 1, 9), item);
    }

    private void removeNextButton(PaginatedGui gui) {
        gui.removeItem(gui.getRows(), Math.clamp(configFile.getInteger(nextPrefix + ".column"), 1, 9));
    }

    /**
     * Create page number from config, and add the feature.
     * @param gui where the button is
     */
    private void addPageNumber(PaginatedGui gui) {
        GuiItem item = ItemBuilder.from(
                        new com.idk.essence.items.items.ItemBuilder(configFile.getString(pagePrefix + ".material"))
                                // Placeholder
                                .displayName(Message.parse(configFile.getString(
                                        pagePrefix + ".display-name", "&7%page% / %total_page%")
                                        .replace("%page%", gui.getCurrentPageNum() + "")
                                        .replace("%total_page%", gui.getPagesNum() + "")))
                                .amount(gui.getCurrentPageNum()).build())
                .asGuiItem();
        gui.setItem(gui.getRows(), Math.clamp(configFile.getInteger(pagePrefix + ".column"), 1, 9), item);
    }
}
