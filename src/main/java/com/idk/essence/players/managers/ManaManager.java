package com.idk.essence.players.managers;

import com.idk.essence.players.AbstractDataManager;
import com.idk.essence.players.PlayerDataRegistry;
import com.idk.essence.players.providers.ManaProvider;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.placeholders.Placeholder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class ManaManager extends AbstractDataManager implements ManaProvider {

    // In config
    @Setter private int manaLevel;
    @Setter private int manaRecoverySpeed;

    // Not in config
    @Setter private double maxMana;
    private double mana;

    public ManaManager(OfflinePlayer player) {
        super(player);

        // Get data from yml or default value
        final PlayerDataRegistry levelRegistry = PlayerDataRegistry.MANA_LEVEL;
        manaLevel = getOrDefault(levelRegistry.getName(), c -> c.getInteger(levelRegistry.getName()),
                () -> manaFile.getInteger(levelRegistry.getDefaultPath(), 0));

        final PlayerDataRegistry speedRegistry = PlayerDataRegistry.MANA_RECOVERY_SPEED;
        manaRecoverySpeed = getOrDefault(speedRegistry.getName(), c -> c.getInteger(speedRegistry.getName()),
                () -> manaFile.getInteger(speedRegistry.getDefaultPath(), 60));

        manaSetup();
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
    @Override
    public void deductMana(double amount) {
        mana -= amount;
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return getPlayerInstance();
    }

    @Override
    public Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new LinkedHashMap<>();
        placeholders.put(Placeholder.PLAYER.name, Optional.ofNullable(getOfflinePlayer()).map(OfflinePlayer::getName).orElse(""));
        placeholders.put(Placeholder.MANA_LEVEL.name, String.valueOf(manaLevel));
        placeholders.put(Placeholder.MANA.name, String.valueOf(Util.Tool.round(mana, 2)));
        placeholders.put(Placeholder.DEFAULT_MANA.name, String.valueOf(Util.Tool.round(ManaProvider.getDefaultMana(), 2)));
        placeholders.put(Placeholder.MAX_MANA.name, String.valueOf(Util.Tool.round(maxMana, 2)));
        placeholders.put(Placeholder.MANA_RECOVERY_SPEED.name, String.valueOf(manaRecoverySpeed));
        return placeholders;
    }

    @Override
    public void setToConfig() {
        if(getConfig() == null) return;
        getConfig().set(PlayerDataRegistry.MANA_LEVEL.getName(), manaLevel);
        getConfig().set(PlayerDataRegistry.MANA_RECOVERY_SPEED.getName(), manaRecoverySpeed);
    }
}
