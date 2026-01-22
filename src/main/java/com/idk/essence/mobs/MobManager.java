package com.idk.essence.mobs;

import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MobManager {

    private static final Map<String, MobTemplate> mobs = new HashMap<>();

    private MobManager() {}

    public static void initialize() {
        mobs.clear();
        ConfigManager.ConfigFolder.MOBS.load(MobManager::register);
    }

    public static Collection<String> getAllKeys() {
        return mobs.keySet();
    }

    public static Collection<Mob> getAll() {
        return mobs.values().stream().map(MobTemplate::getMob).toList();
    }

    public static boolean spawn(String internalName, Location location) {
        return Optional.ofNullable(mobs.get(internalName)).map(template -> {
            template.spawn(location);
            return true;
        }).orElse(false);
    }

    private static void register(String internalName, EssenceConfig config) {
        if(!config.has(internalName) || mobs.containsKey(internalName)) return;
        MobTemplate template = new MobTemplate(internalName)
                .displayName(config.outString(internalName + ".display-name", ""))
                .type(config.getString(internalName + ".type"))
                .description(config.getStringList(internalName + ".description"))
                .health(config.getDouble(internalName + ".health", -1))
                .element(config.getString(internalName + ".element"))
                .equipment(config.getConfigurationSection(internalName + ".equipment"));
        mobs.put(internalName, template);
    }
}
