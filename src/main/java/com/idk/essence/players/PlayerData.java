package com.idk.essence.players;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerData implements ManaManager {

    private final EssenceConfig config;
    private final Player player;

    // In config
    @Setter private int manaLevel;
    @Setter private int manaRecoverySpeed;

    // Not in config
    @Setter private double maxMana;
    private double mana;

    public PlayerData(Player player) {
        config = ConfigManager.Folder.PLAYER_DATA.getConfig(player.getUniqueId().toString());
        this.player = player;
        if(config == null) return;

        manaLevel = config.getInteger(PlayerDataRegistry.MANA_LEVEL.getName());
        manaRecoverySpeed = config.getInteger(PlayerDataRegistry.MANA_RECOVERY_SPEED.getName());
        manaSetup();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * -1 means max amount.
     */
    public void setMana(double amount) {
        if(amount == -1)
            mana = getMaxMana();
        else
            mana = amount;
    }

    /**
     * Deduct mana by the amount.
     */
    public void deductMana(double amount) {
        mana -= amount;
    }
}
