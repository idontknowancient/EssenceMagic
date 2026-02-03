package com.idk.essence.players;

import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class AbstractDataManager {

    @Getter private final Player playerInstance;

    public AbstractDataManager(Player player) {
        this.playerInstance = player;
    }
}
