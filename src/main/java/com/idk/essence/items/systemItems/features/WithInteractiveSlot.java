package com.idk.essence.items.systemItems.features;

import org.bukkit.Location;

public interface WithInteractiveSlot {

    void generateSlotsAround(Location center, float startYaw, int count);

    void rebuildSlotsAround(Location center);

}
