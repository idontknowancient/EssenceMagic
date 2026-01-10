package com.idk.essence.players;

import com.idk.essence.Essence;
import com.idk.essence.utils.configs.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public interface ManaHandler {

    Essence plugin = Essence.getPlugin();
    ConfigManager.ConfigDefaultFile cm = ConfigManager.ConfigDefaultFile.MANA;
    List<Integer> taskIds = new ArrayList<>();
    int interval = cm.getInteger("update-interval");

    int getManaLevel();

    double getMana();

    static double getDefaultMana() {
        return cm.getDouble("default-value");
    }

    double getMaxMana();

    double getManaRecoverySpeed();

    Player getPlayer();

    void setMaxMana(double maxMana);

    void setMana(double mana);

    static void initialize() {
        //notice!
        for(int id : taskIds)
            Bukkit.getScheduler().cancelTask(id);
        taskIds.clear();
    }

    default void setup() {
        setMaxMana(getDefaultMana() + getManaLevel() * cm.getDouble("max-mana-modifier"));
        setMana(getMaxMana());
        showInActionBar();
        recover();
    }

    default void showInActionBar() {
        taskIds.add(new BukkitRunnable() {
            @Override
            public void run() {
                //self-cancelling bukkit runnable
                if(!cm.getBoolean("show-in-action-bar"))
                    this.cancel();
                if(getPlayer() != null) {
                    getPlayer().sendActionBar(cm.outString("show-message", PlayerData.dataMap.get(getPlayer().getName())));
                }
            }
        }.runTaskTimer(plugin, 0L, interval).getTaskId());
    }

    default void recover() {
        taskIds.add(new BukkitRunnable() {
            @Override
            public void run() {
                if(!cm.getBoolean("naturally-recover"))
                    this.cancel();
                if(getPlayer() != null) {
                    if(getMana() <= getMaxMana()) {
                        setMana(Math.min(getMana() + getManaRecoverySpeed()*interval/20.00d, getMaxMana()));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval).getTaskId());
    }
}
