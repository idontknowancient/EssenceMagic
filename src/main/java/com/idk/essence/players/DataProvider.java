package com.idk.essence.players;

import com.idk.essence.Essence;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import com.idk.essence.utils.placeholders.PlaceholderProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public interface DataProvider extends PlaceholderProvider {

    Essence plugin = Essence.getPlugin();

    OfflinePlayer getOfflinePlayer();

    /**
     * Automatically save to config.
     */
    void setToConfig();

    @NotNull
    default UUID getUUID() {
        return getOfflinePlayer().getUniqueId();
    }

    @Nullable
    default Player getOnlinePlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    @Nullable
    default EssenceConfig getConfig() {
        return ConfigManager.Folder.PLAYER_DATA.getConfig(getUUID().toString());
    }

    @NotNull
    default <T> T getOrDefault(String path, Function<EssenceConfig, T> function, Supplier<T> default_) {
        return Optional.ofNullable(getConfig()).filter(c -> c.has(path)).map(function).orElseGet(default_);
    }
}
