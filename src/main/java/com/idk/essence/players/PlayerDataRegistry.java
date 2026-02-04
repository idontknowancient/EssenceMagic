package com.idk.essence.players;
import lombok.Getter;

public enum PlayerDataRegistry {

    // Mana
    MANA_LEVEL("mana-level", "default-level"),
    MANA_RECOVERY_SPEED("mana-recovery-speed", "recovery-speed"),

    // Magic Data
    GOTTEN_APTITUDES("gotten-aptitudes", null),
    LEARNED_DOMAINS("learned-domains", null),
    LEARNED_SIGNETS("learned-signets", null),
    ;

    @Getter private final String name;
    @Getter private final String defaultPath;

    PlayerDataRegistry(String name, String defaultPath) {
        this.name = name;
        this.defaultPath = defaultPath;
    }
}
