package com.idk.essence.players;

import com.idk.essence.utils.configs.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerDataHandler implements Listener {

    private static ConfigManager.ConfigDefaultFile cp;
    private static ConfigManager.ConfigDefaultFile cm;

    public static void initialize() {
        cp = ConfigManager.ConfigDefaultFile.PLAYER_DATA;
        cm = ConfigManager.ConfigDefaultFile.MANA;
        PlayerData.dataMap.clear();
        ManaHandler.initialize();
        setPlayerData();
    }

    private static void setPlayerData() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            setSinglePlayerData(p);
        }
    }

    //initialize a player's dataName in player_data.yml
    public static void initPlayerData(String playerName) {
        //mana-level
        if(!cp.isInteger(playerName + "." + PlayerData.dataName[0]))
            cp.set(playerName + "." + PlayerData.dataName[0], cm.getInteger("default-level"));
        //mana-recovery-speed
        if(!cp.isDouble(playerName + "." + PlayerData.dataName[1]) &&
                !cp.isInteger(playerName + "." + PlayerData.dataName[1]))
            cp.set(playerName + "." + PlayerData.dataName[1], cm.getDouble("recovery-speed"));
        cp.save();
    }

    public static void updatePlayerData(String playerName) {
        PlayerData.dataMap.get(playerName).setManaLevel(cp.getInteger(playerName + ".mana-level"));
        String recoveryPath = playerName + ".mana-recovery-speed";
        if(cp.isDouble(recoveryPath))
            PlayerData.dataMap.get(playerName).setManaRecoverySpeed(cp.getDouble(recoveryPath));
        else if(cp.isInteger(recoveryPath))
            PlayerData.dataMap.get(playerName).setManaRecoverySpeed(cp.getInteger(recoveryPath));
    }

    public static void setSinglePlayerData(Player player) {
        String playerName = player.getName();
        initPlayerData(playerName);
        if(PlayerData.dataMap.containsKey(playerName))
            updatePlayerData(playerName);
        else
            PlayerData.dataMap.put(playerName, new PlayerData(player));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        setSinglePlayerData(e.getPlayer());
    }
}
