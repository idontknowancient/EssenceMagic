package com.idk.essencemagic.player;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.configs.ConfigFile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ManaHandler implements Listener {

    private static ConfigFile.ConfigName cm;
    private static double defaultMana;
    public static final Map<Player, Double> manaMap = new HashMap<>();

    public static void initialize() {
        manaMap.clear();
        setMana();
        showInActionBar();
        recover();
    }

    public static void setMana() {
        cm = ConfigFile.ConfigName.MANA;
        defaultMana = cm.getDouble("default-value");
        for(Player p : Bukkit.getOnlinePlayers()) {
            manaMap.put(p, defaultMana);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!manaMap.containsKey(p))
            manaMap.put(p, defaultMana);
    }

    private static void showInActionBar() {
        new BukkitRunnable() {
            @Override
            public void run() {
                //self-cancelling bukkit runnable
                if(!cm.getBoolean("show-in-action-bar"))
                    this.cancel();
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(
                            Util.colorize("&7                        Mana: &b" + manaMap.get(p) + "&7/&b" + defaultMana)));
                }
            }
        }.runTaskTimer(EssenceMagic.getPlugin(), 0L, 5L);
    }

    private static void recover() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!cm.getBoolean("naturally-recover"))
                    this.cancel();
                for(Player p : manaMap.keySet()) {
                    double mana = manaMap.get(p);
                    if(mana <= defaultMana) {
                        if(mana + 1 > 20)
                            manaMap.put(p, 20d);
                        else
                            manaMap.put(p, mana + 1);
                    }
                }
            }
        }.runTaskTimer(EssenceMagic.getPlugin(), 0L, (long) cm.getDouble("recovery-speed") * 20);
    }
}
