package com.idk.essencemagic.utils.configs;

import com.idk.essencemagic.EssenceMagic;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigFile {

    private static List<ConfigName> configList = new ArrayList<>();

    public static void initialize() {
        configList.clear();
        for(ConfigName name : ConfigName.values()) {
            name.copyConfigFromResources();
            configList.add(name);
        }
    }

    public enum ConfigName implements EssenceConfig {
        CONFIG("config"),
        ELEMENTS("elements"),
        ITEMS("items"),
        MENUS("menus"),
        MESSAGES("messages"),
        MOBS("mobs"),
        MANA("mana"),
        PLAYER_DATA("player_data"),
        SKILLS("skills"),
        ;

        private final String configName;
        private final File file;
        private final YamlConfiguration config = new YamlConfiguration();
        ConfigName(String configName) {
            this.configName = configName+".yml";
            this.file = new File(EssenceMagic.getPlugin().getDataFolder(), this.configName);
        }

        @Override
        public EssenceMagic getPlugin() {
            return EssenceMagic.getPlugin();
        }

        @Override
        public String getConfigName() {
            return configName;
        }

        @Override
        public File getFile() {
            return file;
        }

        @Override
        public YamlConfiguration getConfig() {
            return config;
        }
    }
}
