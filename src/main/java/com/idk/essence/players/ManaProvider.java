package com.idk.essence.players;

import com.idk.essence.utils.configs.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public interface ManaProvider extends DataProvider {

    ConfigManager.DefaultFile manaFile = ConfigManager.DefaultFile.MANA;
    List<Integer> taskIds = new ArrayList<>();
    int interval = manaFile.getInteger("update-interval", 5);
    boolean showInActionBar = manaFile.getBoolean("show-in-action-bar", true);
    boolean naturallyRecover = manaFile.getBoolean("naturally-recover", true);

    int getManaLevel();
    int getManaRecoverySpeed();
    double getMaxMana();
    double getMana();
    void setMaxMana(double maxMana);
    void setMana(double mana);
    void deductMana(double amount);

    static double getDefaultMana() {
        return manaFile.getDouble("default-value", 20);
    }

    static void initialize() {
        for(int id : taskIds)
            Bukkit.getScheduler().cancelTask(id);
        taskIds.clear();
    }

    default void manaSetup() {
        setMaxMana(getDefaultMana() + getManaLevel() * manaFile.getDouble("max-mana-modifier", 5));
        setMana(getMaxMana());
        showInActionBar();
        recover();
    }

    default void showInActionBar() {
        taskIds.add(new BukkitRunnable() {
            @Override
            public void run() {
                if(!showInActionBar)
                    this.cancel();
                if(getPlayer() != null)
                    getPlayer().sendActionBar(manaFile.outString("show-message", PlayerDataManager.get(getPlayer())));
            }
        }.runTaskTimer(plugin, 0L, interval).getTaskId());
    }

    default void recover() {
        taskIds.add(new BukkitRunnable() {
            @Override
            public void run() {
                if(!naturallyRecover)
                    this.cancel();
                if(getPlayer() != null) {
                    if(getMana() <= getMaxMana()) {
                        // e.g. interval = 5, speed = 100(tick per mana), so add 1/20 per interval
                        setMana(Math.min(getMana() + (double) interval / getManaRecoverySpeed(), getMaxMana()));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, interval).getTaskId());
    }
}
