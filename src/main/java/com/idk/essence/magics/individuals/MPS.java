package com.idk.essence.magics.individuals;

import com.idk.essence.Essence;
import com.idk.essence.items.arcana.ArcanaFactory;
import com.idk.essence.utils.Key;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Magical Processing System, part of Individual Magical System.
 * Handle mana infusion.
 */
public class MPS implements Listener {

    @Getter private static final MPS instance = new MPS();
    private static final Map<UUID, ManaInfusion> activeInfusions = new LinkedHashMap<>();
    private static final Map<UUID, Long> lastInfusion = new LinkedHashMap<>();
    private static BukkitTask infusionTask;

    private MPS() {}

    public static void initialize() {
        activeInfusions.clear();
        lastInfusion.clear();
        if(infusionTask != null)
            infusionTask.cancel();

        infusionTask = new BukkitRunnable() {
            @Override
            public void run() {
                // Prevent ConcurrentModificationException
                Iterator<Map.Entry<UUID, ManaInfusion>> iterator = activeInfusions.entrySet().iterator();

                while(iterator.hasNext()) {
                    Map.Entry<UUID, ManaInfusion> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    ManaInfusion infusion = entry.getValue();
                    if(infusion.isHidden()) {
                        iterator.remove();
                        continue;
                    }

                    Player player = infusion.getPlayer();
                    // Stop mana infusion
                    if(!player.isOnline()) {
                        infusion.stop();
                        iterator.remove();
                        continue;
                    }

                    // Fade away
                    String usingWandName =
                            Key.Type.ARCANA_WAND.getContentOrDefault(player.getInventory().getItemInMainHand(), "");
                    String infusedWandName =
                            Key.Type.ARCANA_WAND.getContentOrDefault(infusion.getWand(), "");
                    if(!usingWandName.equals(infusedWandName) || System.currentTimeMillis() - lastInfusion.get(uuid) > 200) {
                        infusion.fade();
                        continue;
                    }

                    infusion.infuse();
                }
            }
        }.runTaskTimer(Essence.getPlugin(), 0L, 2L);
    }

    @EventHandler
    public void onManaInfusion(PlayerInteractEvent event) {
        if(!event.getAction().isRightClick() || !ArcanaFactory.isWand(event.getItem())) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ManaInfusion infusion = activeInfusions.get(uuid);
        lastInfusion.put(uuid, System.currentTimeMillis());

        if(infusion == null) {
            activeInfusions.put(uuid, new ManaInfusion(player, event.getItem()));
        } else if(infusion.isHidden()) {
            infusion.reset();
        }
    }
}
