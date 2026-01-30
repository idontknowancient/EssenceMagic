package com.idk.essence.items.arcana;

import com.idk.essence.items.arcana.types.SimpleRune;
import com.idk.essence.items.arcana.types.SimpleWand;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum ArcanaRegistry {

    WAND("wand", SimpleWand.Builder::new),
    RUNE("rune", SimpleRune.Builder::new),
    ;

    @Getter private final String name;
    @Getter private final Function<String, Arcana.Builder<?>> constructor;

    ArcanaRegistry(String name, Function<String, Arcana.Builder<?>> constructor) {
        this.name = name;
        this.constructor = constructor;
    }

    @Nullable
    public static ArcanaRegistry get(@Nullable String name) {
        if(name == null) return null;
        for(ArcanaRegistry registry : ArcanaRegistry.values()) {
            if(registry.name.equalsIgnoreCase(name)) return registry;
        }
        return null;
    }
}
