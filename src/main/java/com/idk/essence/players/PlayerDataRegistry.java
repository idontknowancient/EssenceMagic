package com.idk.essence.players;
import lombok.Getter;

public enum PlayerDataRegistry {

    MANA_LEVEL("mana-level", "default-level"),
    MANA_RECOVERY_SPEED("mana-recovery-speed", "recovery-speed"),
    ;

    @Getter private final String name;
    @Getter private final String defaultPath;

    PlayerDataRegistry(String name, String defaultPath) {
        this.name = name;
        this.defaultPath = defaultPath;
    }
}
