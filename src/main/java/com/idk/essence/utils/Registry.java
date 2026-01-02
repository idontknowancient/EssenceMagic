package com.idk.essence.utils;

import com.idk.essence.items.systemItems.WandProcessingTable;
import com.idk.essence.utils.particles.Circle;
import com.idk.essence.utils.particles.RotatingSquare;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public class Registry {

    public enum SystemItem {

        WAND_PROCESSING_TABLE(WandProcessingTable::new),
        ;

        public final Function<String, com.idk.essence.items.SystemItem> constructor;

        SystemItem(Function<String, com.idk.essence.items.SystemItem> constructor) {
            this.constructor = constructor;
        }

    }

    public enum CustomParticle {

        CIRCLE(Circle::new),
        ROTATING_SQUARE(RotatingSquare::new),
        ;

        public final Function<ConfigurationSection, com.idk.essence.utils.particles.CustomParticle> constructor;

        CustomParticle(Function<ConfigurationSection, com.idk.essence.utils.particles.CustomParticle> constructor) {
            this.constructor = constructor;
        }

    }
}
