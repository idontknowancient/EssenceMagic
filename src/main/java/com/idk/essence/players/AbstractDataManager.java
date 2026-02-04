package com.idk.essence.players;

import lombok.Getter;
import org.bukkit.OfflinePlayer;

public abstract class AbstractDataManager {

    @Getter private final OfflinePlayer playerInstance;

    public AbstractDataManager(OfflinePlayer player) {
        this.playerInstance = player;
    }
}
