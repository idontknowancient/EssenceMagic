package com.idk.essence.players;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class PlayerData implements ManaProvider {

    private final Player player;
    private final ManaManager manaManager;

    public PlayerData(Player player) {
        this.player = player;
        manaManager = new ManaManager(player);

    }

    @Override
    public int getManaLevel() {
        return manaManager.getManaLevel();
    }

    @Override
    public int getManaRecoverySpeed() {
        return manaManager.getManaRecoverySpeed();
    }

    @Override
    public double getMaxMana() {
        return manaManager.getMaxMana();
    }

    @Override
    public double getMana() {
        return manaManager.getMana();
    }

    @Override
    public void setMaxMana(double maxMana) {
        manaManager.setMaxMana(Math.max(0, maxMana));
    }

    @Override
    public void setMana(double mana) {
        manaManager.setMana(mana);
    }

    /**
     * Deduct mana by the amount.
     */
    @Override
    public void deductMana(double amount) {
        manaManager.deductMana(amount);
    }

    @Override
    public Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new LinkedHashMap<>();
        placeholders.putAll(manaManager.getPlaceholders());
        return placeholders;
    }

    /**
     * Set all data values to config.
     */
    @Override
    public void setToConfig() {
        if(getConfig() == null) return;
        // Set player id, case-sensitive
        if(!getConfig().isString("id"))
            getConfig().set("id",  getPlayer().getName());
        else if(!Optional.ofNullable(getConfig().getString("id"))
                .map(id -> id.equals(player.getName())).orElse(false))
            getConfig().set("id",  player.getName());

        // Set data
        manaManager.setToConfig();

        getConfig().save();
    }

}
