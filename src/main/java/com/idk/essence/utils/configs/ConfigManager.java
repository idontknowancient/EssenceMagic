package com.idk.essence.utils.configs;

import com.idk.essence.Essence;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.stream.Stream;

public class ConfigManager {

    public static void initialize() {
        for(Folder folder : Folder.values()) {
            folder.setupFolders();
        }
        for(DefaultFile file : DefaultFile.values()) {
            file.copyFromResources();
        }
        for(Folder folder : Folder.values()) {
            folder.setupFiles();
        }
    }

    public enum Folder {
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
        PLAYER_DATA("player_data"),
        SKILLS("skills"),
        WANDS("wands"),
        ;

        private static final Essence plugin = Essence.getPlugin();

        private final String folderName;

        private final Map<String, EssenceConfig> configFiles = new HashMap<>();

        Folder(String folderName) {
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
                            configFiles.put(fileName, new FolderFile(path.toFile(), fileName));
                        });
            } catch(IOException e) {
                Essence.getPlugin().getComponentLogger().error("Error occurred when walking folder: {} !", folderName);
            }
        }

        /**
         * Create files in this folder if not exist.
         * @param fileName no .yml needed
         */
        public void createFile(String fileName) {
            if(!fileName.endsWith(".yml")) fileName += ".yml";
            Path path = plugin.getDataFolder().toPath().resolve(folderName).resolve(fileName);
            try {
                // Ensure all parent folders are created
                Files.createDirectories(path.getParent());
                // Create if not exist
                if(Files.notExists(path)) {
                    Files.createFile(path);
                    configFiles.put(fileName, new FolderFile(path.toFile(), fileName));
                }
            } catch(IOException e) {
                Essence.getPlugin().getComponentLogger().error("Error occurred when creating file: {} !", fileName);
            }
        }

        /**
         * Get EssenceConfig by file name.
         * @param fileName relative file name. e.g. items/artifacts/Artifacts.yml -> Artifacts.yml & elements/Elements.yml -> Elements.yml
         */
        @Nullable
        public EssenceConfig getConfig(String fileName) {
            fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
            return configFiles.get(fileName);
        }

        /**
         * Load all content.
         * @param consumer for example, ItemBuilder::register
         */
        public void load(BiConsumer<String, EssenceConfig> consumer) {
            for(EssenceConfig configFile : configFiles.values()) {
                for(String name : configFile.getConfig().getKeys(false)) {
                    consumer.accept(name, configFile);
                }
            }
        }
    }

    public static class FolderFile implements EssenceConfig {

        private final String fileName;
        private final File file;
        private final YamlConfiguration config;

        /**
         * @param fileName .yml will be automatically appended if missing
         */
        public FolderFile(File file, String fileName) {
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

    public enum DefaultFile implements EssenceConfig {
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

        DefaultFile(String fileName) {
            this.fileName = fileName + ".yml";
            this.file = new File(getPlugin().getDataFolder(), this.fileName);
            folderPath = null;
        }

        DefaultFile(String folderName, String fileName) {
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
