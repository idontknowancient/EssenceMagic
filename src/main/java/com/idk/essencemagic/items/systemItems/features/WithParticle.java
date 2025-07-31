package com.idk.essencemagic.items.systemItems.features;

import org.bukkit.Location;

public interface WithParticle {

    boolean isDisplayParticle();

    void setDisplayParticle(boolean displayParticle);

    void generateParticle(Location location);

}
