package com.idk.essencemagic.utils;

import com.idk.essencemagic.items.systemItems.WandProcessingTable;
import com.idk.essencemagic.utils.particles.Circle;
import com.idk.essencemagic.utils.particles.RotatingSquare;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public class Registry {

    public enum SystemItem {

        WAND_PROCESSING_TABLE(WandProcessingTable::new),
        ;

        public final Function<String, com.idk.essencemagic.items.SystemItem> constructor;

        SystemItem(Function<String, com.idk.essencemagic.items.SystemItem> constructor) {
            this.constructor = constructor;
        }

    }

    public enum CustomParticle {

        CIRCLE(Circle::new),
        ROTATING_SQUARE(RotatingSquare::new),
        ;

        public final Function<ConfigurationSection, com.idk.essencemagic.utils.particles.CustomParticle> constructor;

        CustomParticle(Function<ConfigurationSection, com.idk.essencemagic.utils.particles.CustomParticle> constructor) {
            this.constructor = constructor;
        }

    }
}
