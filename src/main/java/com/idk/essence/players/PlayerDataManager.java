package com.idk.essence.players;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataManager implements Listener {

    @Getter private static final PlayerDataManager instance = new PlayerDataManager();
    private static final Map<UUID, PlayerData> playerData = new HashMap<>();

    private PlayerDataManager() {}

    public static void initialize() {
        playerData.clear();
        PlayerDataRegistry.initialize();
        ManaManager.initialize();
        // Set all online players' data
        Bukkit.getOnlinePlayers().forEach(PlayerDataManager::setPlayerData);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        setPlayerData(event.getPlayer());
    }

    private static void setPlayerData(Player player) {
        setupConfig(player);
        if(has(player))
            updatePlayerData(player);
        else
            add(player);
    }

    /**
     * Create yml data file and set all default values if not exist.
     */
    private static void setupConfig(Player player) {
        // File
        ConfigManager.Folder.PLAYER_DATA.createFile(player.getUniqueId().toString());
        // Content
        EssenceConfig config = ConfigManager.Folder.PLAYER_DATA.getConfig(player.getUniqueId().toString());
        if(config == null) return;
        PlayerDataRegistry.setToConfig(config, player);
    }

    /**
     * Fetch data from config and update to the map.
     */
    private static void updatePlayerData(Player player) {
        EssenceConfig config = ConfigManager.Folder.PLAYER_DATA.getConfig(player.getUniqueId().toString());
        if(config == null) return;
        get(player).setManaLevel(config.getInteger(PlayerDataRegistry.MANA_LEVEL.getName()));
        get(player).setManaRecoverySpeed(config.getInteger(PlayerDataRegistry.MANA_RECOVERY_SPEED.getName()));
    }

    public static boolean has(Player player) {
        return player != null && playerData.containsKey(player.getUniqueId());
    }

    public static void add(Player player) {
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
     * Get player data of a player.
     */
    @Nullable
    public static PlayerData get(String playerName) {
        return playerData.get(Optional.ofNullable(Bukkit.getPlayer(playerName)).map(Player::getUniqueId).orElse(null));
    }
}
