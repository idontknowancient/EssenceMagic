package com.idk.essencemagic.utils.configs;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.utils.Util;
import com.idk.essencemagic.utils.placeholders.InternalPlaceholderHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;

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

    default boolean isString(String path) {
        return getConfig().isString(path);
    }

    default String getString(String path) {
        return getConfig().getString(path);
    }

    default String getString(String path, String default_) {
        return getConfig().getString(path, default_);
    }

    default boolean isInteger(String path) {
        return getConfig().isInt(path);
    }

    default int getInteger(String path) {
        return getConfig().getInt(path);
    }

    default int getInteger(String path, int default_) {
        return getConfig().getInt(path, default_);
    }

    default boolean isDouble(String path) {
        return getConfig().isDouble(path);
    }

    default double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    default double getDouble(String path, double default_) {
        return getConfig().getDouble(path, default_);
    }

    default boolean isBoolean(String path) {
        return getConfig().isBoolean(path);
    }

    default boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    default boolean getBoolean(String path, boolean default_) {
        return getConfig().getBoolean(path, default_);
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

    default void set(String path, Object value) {
        getConfig().set(path, value);
    }

    default String outString(String path) { //with no placeholders
        return Util.colorize(getString(path));
    }

    default String outString(String path, String default_) { //with no placeholders
        return Util.colorize(getString(path, default_));
    }

    default String outString(String path, Object info) { //with placeholders
        return Util.colorize(
                InternalPlaceholderHandler.translatePlaceholders(getString(path), info));
    }

    default String outString(String path, String default_, Object info) { //with placeholders
        return Util.colorize(
                InternalPlaceholderHandler.translatePlaceholders(getString(path, default_), info));
    }

    default List<String> outStringList(String path) {
        List<String> colorized = new ArrayList<>();
        for(String s : getStringList(path))
            colorized.add(Util.colorize(s));

        return colorized;
    }

    default List<String> outStringList(String path, Object info) {
        List<String> colorized = new ArrayList<>();
        for(String s : getStringList(path))
            colorized.add(Util.colorize(InternalPlaceholderHandler.translatePlaceholders(s, info)));

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
