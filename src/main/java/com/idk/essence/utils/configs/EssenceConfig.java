package com.idk.essence.utils.configs;

import com.idk.essence.Essence;
import com.idk.essence.utils.messages.Message;
import com.idk.essence.utils.placeholders.PlaceholderManager;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public interface EssenceConfig {

    Essence getPlugin();
    String getFileName();
    File getFile();
    YamlConfiguration getConfig();

    default boolean has(String path) {
        return getConfig().contains(path);
    }

    default boolean isString(String path) {
        return getConfig().isString(path);
    }

    @Nullable default String getString(String path) {
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

    @NotNull default List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    @NotNull default List<String> getStringListOrString(String path) {
        if(isList(path))
            return getStringList(path);
        else if(isString(path))
            return List.of(Optional.ofNullable(getString(path)).orElse(""));
        return List.of("");
    }

    @NotNull default List<String> getStringListOrString(String path, String default_) {
        if(isList(path))
            return getStringList(path);
        else if(isString(path))
            return List.of(Optional.ofNullable(getString(path)).orElse(default_));
        return List.of(default_);
    }

    default boolean isConfigurationSection(String path) {
        return getConfig().isConfigurationSection(path);
    }

    @Nullable default ConfigurationSection getConfigurationSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    default void set(String path, Object value) {
        getConfig().set(path, value);
    }

    /**
     * Without placeholders, without default value.
     * @return the component
     */
    @NotNull default Component outString(String path) { //with no placeholders
        return Message.parse(getString(path));
    }

    /**
     * Without placeholders, with default value.
     * @return the component
     */
    default Component outString(String path, String default_) { //with no placeholders
        return Message.parse(getString(path, default_));
    }

    /**
     * With placeholders, without default value.
     * @return the component
     */
    @NotNull default Component outString(String path, Object info) { //with placeholders
        return Message.parse(PlaceholderManager.translate(getString(path), info));
    }

    /**
     * With placeholders, with default value.
     * @return the component
     */
    default Component outString(String path, String default_, Object info) { //with placeholders
        return Message.parse(PlaceholderManager.translate(getString(path, default_), info));
    }

    /**
     * Without placeholders
     * @return the component list
     */
    @NotNull default List<Component> outStringList(String path) {
        return getStringList(path).stream().map(Message::parse).toList();
    }

    /**
     * With placeholders
     * @return the component list
     */
    @NotNull default List<Component> outStringList(String path, Object info) {
        return getStringList(path).stream().map(string -> Message.parse(string, info)).toList();
    }

    default void save() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            Essence.getPlugin().getComponentLogger().error("Unable to save file: {}!", getFile().getName(), e);
        }
    }
}
