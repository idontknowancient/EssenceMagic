package com.idk.essencemagic.player;

import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerDataHandler implements Listener {

    private static ConfigFile.ConfigName cp;
    private static ConfigFile.ConfigName cm;

    public static void initialize() {
        cp = ConfigFile.ConfigName.PLAYER_DATA;
        cm = ConfigFile.ConfigName.MANA;
        PlayerData.dataMap.clear();
        ManaHandler.initialize();
        setPlayerData();
    }

    public static void setPlayerData() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            setSinglePlayerData(p);
        }
    }

    //initialize a player's dataName in player_data.yml
    public static void initPlayerData(String playerName) {
        //mana-level
        if(cp.isString(playerName + "." + PlayerData.dataName[0]))
            cp.set(playerName + "." + PlayerData.dataName[0], cm.getDouble("default-level"));
        //mana-recovery-speed
        if(cp.isString(playerName + "." + PlayerData.dataName[1]))
            cp.set(playerName + "." + PlayerData.dataName[1], cm.getDouble("recovery-speed"));
        cp.save();
    }

    public static void setSinglePlayerData(Player player) {
        String playerName = player.getName();
        initPlayerData(playerName);
        if(!PlayerData.dataMap.containsKey(playerName))
            PlayerData.dataMap.put(playerName, new PlayerData(player));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        setSinglePlayerData(e.getPlayer());
    }
}
