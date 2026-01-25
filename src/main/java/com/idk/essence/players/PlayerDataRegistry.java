package com.idk.essence.players;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public enum PlayerDataRegistry {

    MANA_LEVEL("mana-level", ConfigManager.DefaultFile.MANA.getInteger("default-level", 0)),
    MANA_RECOVERY_SPEED("mana-recovery-speed", ConfigManager.DefaultFile.MANA.getInteger("recovery-speed", 5)),
    ;

    @Getter private final String name;
    @Getter private final Object default_;

    PlayerDataRegistry(String name, Object default_) {
        this.name = name;
        this.default_ = default_;
    }

    /**
     * Set default value of player data if not exist. Auto save.
     * @param config data file of the player
     */
    public static void setToConfig(EssenceConfig config, Player player) {
        // Set player id, case-sensitive
        if(!config.isString("id"))
            config.set("id",  player.getName());
        else if(!Optional.ofNullable(config.getString("id"))
                .map(id -> id.equals(player.getName())).orElse(false))
            config.set("id",  player.getName());
        // Set player data
        Arrays.stream(PlayerDataRegistry.values()).forEach(registry -> {
            if(!config.has(registry.name))
                config.set(registry.name, registry.default_);
        });
        config.save();
    }
}
