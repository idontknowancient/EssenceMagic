package com.idk.essence.mobs;

import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.*;

public class MobManager implements Listener {

    @Getter private static final MobManager instance = new MobManager();
    private static final Map<String, MobTemplate> mobs = new LinkedHashMap<>();
    private static boolean cancelEndermanMovingBlock;

    private MobManager() {}

    public static void initialize() {
        mobs.clear();
        cancelEndermanMovingBlock = ConfigManager.DefaultFile.CONFIG.getBoolean("cancel-enderman-moving-block");
        ConfigManager.Folder.MOBS.load(MobManager::register);
        Util.System.info("Registered Mobs", mobs.size());
    }

    @EventHandler
    public void onEndermanMoveBlock(EntityChangeBlockEvent event) {
        if(event.getEntityType() == EntityType.ENDERMAN && cancelEndermanMovingBlock) {
            event.setCancelled(true);
        }
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
