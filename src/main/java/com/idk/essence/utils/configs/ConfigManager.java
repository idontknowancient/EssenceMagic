package com.idk.essence.utils.configs;

import com.idk.essence.Essence;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.stream.Stream;

public class ConfigManager {

    public static void initialize() {
        for(ConfigFolder folder : ConfigFolder.values()) {
            folder.setupFolders();
        }
        for(ConfigDefaultFile file : ConfigDefaultFile.values()) {
            file.copyFromResources();
        }
        for(ConfigFolder folder : ConfigFolder.values()) {
            folder.setupFiles();
        }
    }

    public enum ConfigFolder {
        ELEMENTS("elements"),
        /**
         * Magical items
         */
        ITEMS_ARCANA("items/arcana"),
        /**
         * System items
         */
        ITEMS_ARTIFACT("items/artifacts"),
        /**
         * Regular items
         */
        ITEMS_ITEMS("items/items"),
        MAGICS("magics"),
        MOBS("mobs"),
        SKILLS("skills"),
        WANDS("wands"),
        ;

        private static final Essence plugin = Essence.getPlugin();

        private final String folderName;

        private final List<EssenceConfig> configFiles = new ArrayList<>();

        ConfigFolder(String folderName) {
            this.folderName = folderName;
        }

        private void setupFolders() {
            if(!plugin.getDataFolder().exists()) {
                if(!plugin.getDataFolder().mkdirs()) {
                    Essence.getPlugin().getComponentLogger().error("Cannot create plugin folder!");
                    return;
                }
            }

            Path folderPath = plugin.getDataFolder().toPath().resolve(folderName);
            if(!Files.exists(folderPath)) {
                if(!folderPath.toFile().mkdirs()) {
                    Essence.getPlugin().getComponentLogger().error("Cannot initialize folder: {} !", folderName);
                }
            }
        }

        private void setupFiles() {
            configFiles.clear();
            Path folderPath = plugin.getDataFolder().toPath().resolve(folderName);

            try(Stream<Path> stream = Files.walk(folderPath)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".yml"))
                        .forEach(path -> {
                            // If folderPath is "plugins/Essence/items"
                            // path is "plugins/Essence/items/weapons/gold_sword.yml"
                            Path relativePath = folderPath.relativize(path);
                            // id will be "weapons/gold_sword.yml"
                            String fileName = relativePath.toString().replace("\\", "/");
                            configFiles.add(new ConfigFolderFile(path.toFile(), fileName));
                        });
            } catch(IOException e) {
                Essence.getPlugin().getComponentLogger().error("Error occurred when walking folder: {} !", folderName);
            }
        }

        /**
         * Load all content.
         * @param consumer for example, ItemBuilder::register
         */
        public void load(BiConsumer<String, EssenceConfig> consumer) {
            for(EssenceConfig configFile : configFiles) {
                for(String name : configFile.getConfig().getKeys(false)) {
                    consumer.accept(name, configFile);
                }
            }
        }
    }

    public static class ConfigFolderFile implements EssenceConfig {

        private final String fileName;
        private final File file;
        private final YamlConfiguration config;

        /**
         * @param fileName .yml will be automatically appended if missing
         */
        public ConfigFolderFile(File file, String fileName) {
            this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
            this.file = file;
            config = YamlConfiguration.loadConfiguration(file);
        }

        @Override
        public Essence getPlugin() {
            return Essence.getPlugin();
        }

        @Override
        public String getFileName() {
            return fileName;
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

    public enum ConfigDefaultFile implements EssenceConfig {
        ARTIFACTS("items/artifacts",  "Artifacts"),
        ELEMENTS("elements", "Default_Elements"),
        ITEMS("items/items", "Default_Items"),
        MAGICS("magics", "Default_Magics"),
        MOBS("mobs", "Default_Mobs"),
        SKILLS("skills", "Default_Skills"),
        WANDS("wands", "Default_Wands"),

        CONFIG("config"),
        MANA("mana"),
        MENUS("menus"),
        MESSAGES("messages"),
        PLAYER_DATA("player_data"),
        ;

        private final String fileName;
        private final File file;
        private final Path folderPath;
        private final YamlConfiguration config = new YamlConfiguration();

        ConfigDefaultFile(String fileName) {
            this.fileName = fileName + ".yml";
            this.file = new File(getPlugin().getDataFolder(), this.fileName);
            folderPath = null;
        }

        ConfigDefaultFile(String folderName, String fileName) {
            this.fileName = fileName + ".yml";
            folderPath = getPlugin().getDataFolder().toPath().resolve(folderName);
            this.file = new File(folderPath.toFile(), this.fileName);
        }

        @Override
        public Essence getPlugin() {
            return Essence.getPlugin();
        }

        @Override
        public String getFileName() {
            return fileName;
        }

        @Override
        public File getFile() {
            return file;
        }

        @Override
        public YamlConfiguration getConfig() {
            return config;
        }

        private boolean copyCheck() {
            if(folderPath == null) return true;
            // Create folder if not exist
            if(!Files.exists(folderPath)) {
                if(!folderPath.toFile().mkdirs()) {
                    Essence.getPlugin().getComponentLogger().error("Cannot create folder!");
                }
            }
            // If there is any yml file in the folder, system will not create the default file.
            try(Stream<Path> stream = Files.walk(folderPath)) {
                return stream.filter(Files::isRegularFile).noneMatch(path -> path.toString().endsWith(".yml"));
            } catch(IOException e) {
                getPlugin().getComponentLogger().error("Error occurred when checking folder.", e);
                return false;
            }
        }

        public void copyFromResources() {
            if(!copyCheck()) return;
            File file = getFile();
            YamlConfiguration config = getConfig();
            if(!file.exists()) {
                try(InputStream is = getPlugin().getClass().getResourceAsStream("/" + getFileName())) {
                    if(is != null) {
                        Files.copy(is, Paths.get(file.toURI()));
                        config.load(file);
                    }
                } catch (IOException | InvalidConfigurationException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "", e);
                }
            } else {
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    getPlugin().getLogger().log(Level.SEVERE, "", e);
                }
            }
        }
    }
}
