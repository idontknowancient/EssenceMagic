package com.idk.essencemagic.player;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public interface ManaHandler {

    EssenceMagic plugin = EssenceMagic.getPlugin();
    ConfigFile.ConfigName cm = ConfigFile.ConfigName.MANA;
    double defaultMana = cm.getDouble("default-value");
    List<Integer> taskIds = new ArrayList<>();

    int getManaLevel();

    double getMana();

    double getMaxMana();

    void setMaxMana(double maxMana);

    void setMana(double mana);

    double getManaRecoverySpeed();

    Player getPlayer();

    static void initialize() {
        //notice!
        for(int id : taskIds)
            Bukkit.getScheduler().cancelTask(id);
        taskIds.clear();
    }

    default void setup() {
        setMaxMana(defaultMana + getManaLevel() * 5);
        setMana(getMaxMana());
        showInActionBar();
        recover();
    }

    default void showInActionBar() {
        taskIds.add(new BukkitRunnable() {
            @Override
            public void run() {
                //self-cancelling bukkit runnable
                if (!cm.getBoolean("show-in-action-bar"))
                    this.cancel();
                if(getPlayer() != null) {
                    getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            ConfigFile.ConfigName.MANA.getString("")));
                }
            }
        }.runTaskTimer(plugin, 0L, cm.getInteger("update-interval")).getTaskId());
    }

    default void recover() {
        taskIds.add(new BukkitRunnable() {
            @Override
            public void run() {
                if (!cm.getBoolean("naturally-recover"))
                    this.cancel();
                if(getPlayer() != null) {
                    if (getMana() <= defaultMana) {
                        if (getMana() + 1 > 20)
                            setMana(20);
                        else
                            setMana(getMana() + 1);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L,
                (long) getManaRecoverySpeed() * 20).getTaskId());
    }
}
