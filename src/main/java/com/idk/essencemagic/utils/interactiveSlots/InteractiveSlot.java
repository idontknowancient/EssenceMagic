package com.idk.essencemagic.utils.interactiveSlots;

import com.idk.essencemagic.EssenceMagic;
import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableArrayDataType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class InteractiveSlot implements ConfigurationSerializable {

    public static final Map<Location, InteractiveSlot> activatingSlots = new LinkedHashMap<>();

    @Getter private static final NamespacedKey interactiveSlotKey = new NamespacedKey(EssenceMagic.getPlugin(), "interactive-slot-key");

    @Getter private static final PersistentDataType<byte[],InteractiveSlot[]> dataType = new ConfigurationSerializableArrayDataType<>(InteractiveSlot[].class);

    private BukkitTask task = null;

    private final Location location;

    private final Particle particle;

    @Setter private boolean displayParticle;

    @Setter private Color color;

    @Setter private ItemStack storedItem;

    public InteractiveSlot(Location location, boolean displayParticle, Color color) {
        this.location = location;
        this.particle = Particle.REDSTONE;
        this.color = color;
        this.displayParticle = displayParticle;
    }

    public void generate() {
        if(location.getWorld() == null) return;
        Particle.DustOptions options = new Particle.DustOptions(color, 1);
        if(displayParticle)
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0, options);
                }
            }.runTaskTimer(EssenceMagic.getPlugin(), 0, 1);
        activatingSlots.put(location, this);
    }

    public void remove() {
        if(task != null)
            task.cancel();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("location", location);
        map.put("displayParticle", displayParticle);
        map.put("color", color);

        return map;
    }

    public static InteractiveSlot deserialize(Map<String, Object> map) {
        Location location = (Location) map.get("location");
        boolean displayParticle = (boolean) map.get("displayParticle");
        Color color = (Color) map.get("color");

        return activatingSlots.containsKey(location) ? activatingSlots.get(location) :
                new InteractiveSlot(location, displayParticle, color);
    }
}
