package com.idk.essence.players;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public enum PlayerDataRegistry {

    MANA_LEVEL("mana-level", "default-level", 0, Integer.class, ConfigManager.DefaultFile.MANA),
    MANA_RECOVERY_SPEED("mana-recovery-speed", "recovery-speed", 60, Integer.class, ConfigManager.DefaultFile.MANA),
    ;

    @Getter private final String name;
    @Getter private final String path;
    @Getter private Object default_;
    private final Class<?> clazz;
    private final EssenceConfig config;

    PlayerDataRegistry(String name, String path, Object default_, Class<?> clazz, EssenceConfig config) {
        this.name = name;
        this.path = path;
        this.clazz = clazz;
        this.default_ = default_;
        this.config = config;
    }

    /**
     * Set default values of data.
     */
    public static void initialize() {
        for(PlayerDataRegistry registry : PlayerDataRegistry.values())
            registry.setDefaultValue();
    }

    private void setDefaultValue() {
        try {
            if(clazz.equals(Integer.class))
                default_ = config.getInteger(path, (Integer) default_);
            else if(clazz.equals(Double.class))
                default_ = config.getDouble(path, (Double) default_);
            else if(clazz.equals(String.class))
                default_ = config.getString(path, (String) default_);
        } catch(ClassCastException ignored) {
        }
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
