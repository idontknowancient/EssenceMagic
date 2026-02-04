package com.idk.essence.players;

import com.idk.essence.players.managers.MagicDataManager;
import com.idk.essence.players.managers.ManaManager;
import com.idk.essence.players.providers.MagicDataProvider;
import com.idk.essence.players.providers.ManaProvider;
import com.idk.essence.utils.Util;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class PlayerData implements ManaProvider, MagicDataProvider {

    @Getter private final OfflinePlayer offlinePlayer;
    private final ManaManager manaManager;
    private final MagicDataManager magicDataManager;

    /**
     * Use offline player to make users can modify their data even though they are offline.
     */
    public PlayerData(OfflinePlayer player) {
        this.offlinePlayer = player;
        manaManager = new ManaManager(player);
        magicDataManager = new MagicDataManager(player);
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
    public List<String> getGottenAptitudes() {
        return magicDataManager.getGottenAptitudes();
    }

    @Override
    public List<String> getLearnedDomains() {
        return magicDataManager.getLearnedDomains();
    }

    @Override
    public List<String> getLearnedSignets() {
        return magicDataManager.getLearnedSignets();
    }

    @Override
    public Map<String, String> getPlaceholders() {
        Map<String, String> placeholders = new LinkedHashMap<>();
        placeholders.putAll(manaManager.getPlaceholders());
        placeholders.putAll(magicDataManager.getPlaceholders());
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
            getConfig().set("id", getOfflinePlayer().getName());
        else if(!Optional.ofNullable(getConfig().getString("id"))
                .map(id -> id.equals(getOfflinePlayer().getName())).orElse(false))
            getConfig().set("id", getOfflinePlayer().getName());

        // Set data
        manaManager.setToConfig();
        magicDataManager.setToConfig();

        getConfig().save();
    }

}
