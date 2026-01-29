package com.idk.essence.players;

import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import com.idk.essence.utils.placeholders.Placeholder;
import com.idk.essence.utils.placeholders.PlaceholderProvider;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerData implements PlaceholderProvider, ManaManager {

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

    @Override
    public Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(Placeholder.PLAYER.name, player.getName());
        placeholders.put(Placeholder.MANA_LEVEL.name, String.valueOf(manaLevel));
        placeholders.put(Placeholder.MANA.name, String.valueOf(Util.MathTool.round(mana, 2)));
        placeholders.put(Placeholder.DEFAULT_MANA.name, String.valueOf(Util.MathTool.round(ManaManager.getDefaultMana(), 2)));
        placeholders.put(Placeholder.MAX_MANA.name, String.valueOf(Util.MathTool.round(maxMana, 2)));
        placeholders.put(Placeholder.MANA_RECOVERY_SPEED.name, String.valueOf(manaRecoverySpeed));
        return placeholders;
    }
}
