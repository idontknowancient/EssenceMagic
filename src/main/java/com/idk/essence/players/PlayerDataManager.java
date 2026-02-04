package com.idk.essence.players;

import com.idk.essence.players.providers.ManaProvider;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Online players: plugin init or login.
 * Offline players: call get().
 */
public class PlayerDataManager implements Listener {

    @Getter private static final PlayerDataManager instance = new PlayerDataManager();
    private static final Map<UUID, PlayerData> playerData = new LinkedHashMap<>();

    private PlayerDataManager() {}

    public static void initialize() {
        playerData.clear();
        ManaProvider.initialize();
        // Set all online players' data
        Bukkit.getOnlinePlayers().forEach(PlayerDataManager::setPlayerData);
        Util.System.info("Registered Player Data", playerData.size());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setPlayerData(event.getPlayer());
    }

    private static void setPlayerData(Player player) {
        createConfig(player);
        add(player);
        setupConfig(player);
    }

    /**
     * Create yml data file if not exist.
     */
    private static void createConfig(Player player) {
        ConfigManager.Folder.PLAYER_DATA.createFile(player.getUniqueId().toString());
    }

    /**
     * Set all data values to config.
     */
    private static void setupConfig(OfflinePlayer player) {
        Optional.ofNullable(get(player)).ifPresent(PlayerData::setToConfig);
    }

    public static boolean has(OfflinePlayer player) {
        return player != null && playerData.containsKey(player.getUniqueId());
    }

    /**
     * Create player data object and add it to the map.
     */
    public static void add(OfflinePlayer player) {
        playerData.put(player.getUniqueId(), new PlayerData(player));
    }

    /**
     * Get player data of a player.
     */
    @NotNull
    public static PlayerData get(Player player) {
        return playerData.get(player.getUniqueId());
    }

    /**
     * Get player data of an offline player.
     */
    @Nullable
    public static PlayerData get(OfflinePlayer player) {
        return Optional.ofNullable(player).map(p -> playerData.get(p.getUniqueId())).orElse(null);
    }

    /**
     * Get player data of a player.
     * If the player is an offline player, create its file instance and add it to the map.
     */
    @Nullable
    public static PlayerData get(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if(player != null)
            return get(player);
        OfflinePlayer offlinePlayer = Util.Tool.getPlayerByName(playerName);
        if(has(offlinePlayer))
            return get(offlinePlayer);
        if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
            create(offlinePlayer);
            return get(offlinePlayer);
        }

        return null;
    }

    /**
     * Create a player's file instance and add it to the map.
     */
    public static void create(OfflinePlayer offlinePlayer) {
        add(offlinePlayer);
        setupConfig(offlinePlayer);
    }
}
