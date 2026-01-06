package com.idk.essence.mobs;

import com.idk.essence.utils.configs.ConfigFile;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MobFactory {

    private static final Map<String, MobTemplate> mobs = new HashMap<>();

    private static ConfigFile.ConfigName cm;

    public static void initialize() {
        mobs.clear();
        cm = ConfigFile.ConfigName.MOBS;
        for(String name : cm.getConfig().getKeys(false)) {
            register(name);
        }
    }

    public static Collection<String> getAllKeys() {
        return mobs.keySet();
    }

    public static boolean spawn(String internalName, Location location) {
        return Optional.ofNullable(mobs.get(internalName)).map(template -> {
            template.spawn(location);
            return true;
        }).orElse(false);
    }

    private static void register(String internalName) {
        if(!cm.has(internalName) || mobs.containsKey(internalName)) return;
        MobTemplate template = new MobTemplate(internalName)
                .displayName(cm.outString(internalName + ".display-name", ""))
                .type(cm.getString(internalName + ".type"))
                .description(cm.outStringList(internalName + ".description"))
                .health(cm.getDouble(internalName + ".health"))
                .element(cm.getString(internalName + ".element"))
                .equipment(cm.getConfigurationSection(internalName + ".equipment"));
        mobs.put(internalName, template);
    }
}
