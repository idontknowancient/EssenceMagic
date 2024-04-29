package com.idk.essencemagic.player;

import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData implements ManaHandler {

    public static final Map<String, PlayerData> dataMap = new HashMap<>();
    public static final String[] dataName = {
            "mana-level", "mana-recovery-speed"
    };
    @Getter private final String playerName;
    @Getter private final String playerUUID;
    @Setter @Getter private int manaLevel;
    @Setter @Getter private double mana;
    @Setter @Getter private double maxMana;
    @Setter @Getter private double manaRecoverySpeed;

    public PlayerData(Player player) {
        ConfigFile.ConfigName cp = ConfigFile.ConfigName.PLAYER_DATA;
        playerName = player.getName();
        playerUUID = player.getUniqueId().toString();
        manaLevel = cp.getInteger(playerName + "." + dataName[0]);
        manaRecoverySpeed = cp.getDouble(playerName + "." + dataName[1]);
        setup();
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(playerName);
    }
}
