package com.idk.essencemagic.utils.configs;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public interface EssenceConfig {

    EssenceMagic getPlugin();
    String getConfigName();
    File getFile();
    YamlConfiguration getConfig();

    default void copyConfigFromResources() {
        if(!getPlugin().getDataFolder().exists()) getPlugin().getDataFolder().mkdirs();
        File file = getFile();
        YamlConfiguration config = getConfig();
        if(!file.exists()) {
            try(InputStream is = getPlugin().getClass().getResourceAsStream("/" + getConfigName())) {
                if(is != null) {
                    Files.copy(is, Paths.get(file.toURI()));
                    config.load(file);
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    default boolean isString(String path) {
        return getConfig().isString(path);
    }

    default String getString(String path) {
        return getConfig().getString(path);
    }

    default boolean isInteger(String path) {
        return getConfig().isInt(path);
    }

    default int getInteger(String path) {
        return getConfig().getInt(path);
    }
    default boolean isDouble(String path) {
        return getConfig().isDouble(path);
    }

    default double getDouble(String path) {
        return getConfig().getDouble(path);
    }
    default boolean isBoolean(String path) {
        return getConfig().isBoolean(path);
    }

    default boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    default boolean isList(String path) {
        return getConfig().isList(path);
    }

    default List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    default boolean isConfigurationSection(String path) {
        return getConfig().isConfigurationSection(path);
    }

    default ConfigurationSection getConfigurationSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    default String outString(String path) { //with no placeholders
        return Util.colorize(getString(path));
    }

    default String outString(String path, Player p) { //with placeholders
        return Util.colorize(Util.translatePlaceholder(p, getString(path)));
    }

    default List<String> outStringList(String path) {
        List<String> colorized = new ArrayList<>();
        getStringList(path).forEach(s->colorized.add(Util.colorize(s)));

        return colorized;
    }

    default List<String> outStringList(String path, Player p) {
        List<String> colorized = new ArrayList<>();
        getStringList(path).forEach(s->colorized.add(Util.colorize(Util.translatePlaceholder(p, s))));

        return colorized;
    }

    default void save() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
