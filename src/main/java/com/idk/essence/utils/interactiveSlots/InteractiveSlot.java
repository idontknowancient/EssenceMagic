package com.idk.essence.utils.interactiveSlots;

import com.idk.essence.Essence;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigFile;
import com.jeff_media.morepersistentdatatypes.datatypes.serializable.ConfigurationSerializableArrayDataType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
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

    @Getter private static final NamespacedKey interactiveSlotKey = new NamespacedKey(Essence.getPlugin(), "interactive-slot-key");

    @Getter private static final PersistentDataType<byte[],InteractiveSlot[]> dataType = new ConfigurationSerializableArrayDataType<>(InteractiveSlot[].class);

    private BukkitTask task = null;
    // do we really need to serialize the class?
    private final ConfigurationSection section;
    private Location location;
    private double radian = 0;
    @Setter private boolean displayParticle;
    private final double radius;
    private final double yOffset;
    private final Color color;
    @Setter private ItemStack storedItem;

    public InteractiveSlot(ConfigurationSection section) {
        this.section = section;
        displayParticle = section.getBoolean("display", true);
        radius = section.getDouble("radius", 3);
        yOffset = section.getDouble("y-offset", 1.2);
        color = Util.stringToColor(section.getString("color", "WHITE"));
        storedItem = null;
    }

    public void generate(Location location) {
        this.location = location;
        if(location.getWorld() == null) return;

        Particle.DustOptions options = new Particle.DustOptions(color, 1);
        ConfigFile.ConfigName EssenceMagic;
        if(displayParticle)
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    location.getWorld().spawnParticle(Particle.DUST, location, 1, 0, 0, 0, 0, options);
                }
            }.runTaskTimer(Essence.getPlugin(), 0, 1);

        activatingSlots.put(location, this);
    }

    public void generate(Location center, double radian) {
        location = center.clone().add(radius * Math.sin(radian) + 0.5, yOffset, radius * Math.cos(radian) + 0.5);
        this.radian = radian;
        generate(location);
    }

    public void remove() {
        if(task != null)
            task.cancel();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("section", section);
        map.put("location", location);

        return map;
    }

    public static InteractiveSlot deserialize(Map<String, Object> map) {
        ConfigurationSection section = (ConfigurationSection) map.get("section");
        Location location = (Location) map.get("location");

        return activatingSlots.containsKey(location) ? activatingSlots.get(location) :
                new InteractiveSlot(section);
    }
}