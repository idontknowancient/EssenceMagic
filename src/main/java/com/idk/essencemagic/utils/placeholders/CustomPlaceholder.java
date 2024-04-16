package com.idk.essencemagic.utils.placeholders;

import com.idk.essencemagic.EssenceMagic;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPlaceholder extends PlaceholderExpansion {

    private final EssenceMagic plugin = EssenceMagic.getPlugin();

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; //telling placeholderAPI not to unload the placeholders
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.startsWith("element_")) {
            String input = params.substring(8);
            if(input.equals("name")) {
                //testing
            }
        }

        return super.onRequest(player, params);
    }
}
