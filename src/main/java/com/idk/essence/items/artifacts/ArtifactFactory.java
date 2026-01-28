package com.idk.essence.items.artifacts;

import com.idk.essence.Essence;
import com.idk.essence.items.items.ItemFactory;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArtifactFactory implements Listener {

    /**
     * Used to register listener
     */
    @Getter private static final ArtifactFactory instance = new ArtifactFactory();

    /**
     * Store all activateArtifacts enabled (can adjust in config)
     */
    private static final Map<String, ArtifactBuilder> activateArtifacts = new HashMap<>();

    /**
     * Store all behaviors no matter whether an artifact is enabled
     */
    private static final Map<String, ArtifactBehavior>  behaviors = new HashMap<>();

    private ArtifactFactory() {}

    public static void initialize() {
        activateArtifacts.clear();
        behaviors.clear();
        ArtifactRegistry.registerBehaviors();
        ConfigManager.Folder.ITEMS_ARTIFACT.load(ArtifactFactory::register);
    }

    /**
     * Pass event to behavior to handle more actions if the item is an artifact.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();
        if(!isPlaceable(item)) event.setCancelled(true);

        // Transfer all PDC to the block and override all PDC
        CustomBlockData data = new CustomBlockData(block, Essence.getPlugin());
        Util.copyPDC(item.getPersistentDataContainer(), data);

        // Pass the event to behavior
        Optional.ofNullable(getBehavior(item)).ifPresent(behavior -> behavior.onBlockPlace(event));
    }

    /**
     * Pass event to behavior to handle more actions if the item is an artifact.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(!isArtifact(block)) return;

        // Drop artifact
        if(event.isDropItems()) {
            event.setDropItems(false);
            Optional.ofNullable(getArtifact(block)).ifPresent(artifact ->
                    block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), artifact));
        }

        // Pass the event to behavior
        Optional.ofNullable(getBehavior(block)).ifPresent(behavior -> behavior.onBlockBreak(event));
    }

    /**
     * Pass event to behavior to handle more actions if the item is an artifact.
     */
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(block == null || !isArtifact(block) || !event.getAction().isRightClick()) return;

        // Pass the event to behavior
        event.setCancelled(true);
        Optional.ofNullable(getBehavior(block)).ifPresent(behavior -> behavior.onBlockInteract(event));
    }

    /**
     * Pass event to behavior to handle more actions if the item is an artifact.
     */
    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(!isUsable(item)) event.setCancelled(true);

        // Pass the event to behavior
        Optional.ofNullable(getBehavior(item)).ifPresent(behavior -> behavior.onItemInteract(event));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isArtifact(ItemStack item) {
        return ItemFactory.isCustom(item) && item.getItemMeta().getPersistentDataContainer().has(Key.Class.ARTIFACT.get());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isArtifact(Block block) {
        CustomBlockData data = new CustomBlockData(block, Essence.getPlugin());
        return data.has(Key.Class.ITEM.get(), PersistentDataType.STRING) &&
                data.has(Key.Class.ARTIFACT.get(), PersistentDataType.STRING);
    }

    /**
     * Check if an item stack is placeable. Only effective for blocks.
     */
    public static boolean isPlaceable(ItemStack item) {
        if(!isArtifact(item)) return true;
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer()
                .get(Key.Feature.PLACEABLE.get(),  PersistentDataType.BOOLEAN)).orElse(true);
    }

    /**
     * Check if an item stack is usable. Only effective for interactable items.
     */
    public static boolean isUsable(ItemStack item) {
        if(!isArtifact(item)) return true;
        return Optional.ofNullable(item.getItemMeta().getPersistentDataContainer()
                .get(Key.Feature.USABLE.get(),  PersistentDataType.BOOLEAN)).orElse(true);
    }

    public static boolean hasActiveArtifact(String internalName) {
        return activateArtifacts.containsKey(internalName);
    }

    @Nullable
    public static ItemStack getArtifact(String internalName) {
        return Optional.ofNullable(activateArtifacts.get(internalName)).map(ArtifactBuilder::build).orElse(null);
    }

    @Nullable
    public static ItemStack getArtifact(Block block) {
        CustomBlockData data = new CustomBlockData(block, Essence.getPlugin());
        return Optional.ofNullable(activateArtifacts.get(data
                .get(Key.Class.ARTIFACT.get(), PersistentDataType.STRING))).map(ArtifactBuilder::build).orElse(null);
    }

    @Nullable
    public static ConfigurationSection getParticleSection(String internalName) {
        return Optional.ofNullable(activateArtifacts.get(internalName))
                .map(ArtifactBuilder::getParticleSection).orElse(null);
    }

    @Nullable
    public static ConfigurationSection getNodeSection(String internalName) {
        return Optional.ofNullable(activateArtifacts.get(internalName))
                .map(ArtifactBuilder::getNodeSection).orElse(null);
    }

    public static Collection<String> getAllActivateKeys() {
        return activateArtifacts.keySet();
    }

    public static void addBehavior(String internalName, ArtifactBehavior behavior) {
        behaviors.put(internalName, behavior);
    }

    @Nullable
    public static ArtifactBehavior getBehavior(String internalName) {
        return behaviors.get(internalName);
    }

    @Nullable
    public static ArtifactBehavior getBehavior(ItemStack item) {
        if(!isArtifact(item)) return null;
        return getBehavior(item.getItemMeta().getPersistentDataContainer().get(Key.Class.ARTIFACT.get(),  PersistentDataType.STRING));
    }

    @Nullable
    public static ArtifactBehavior getBehavior(Block block) {
        if(!isArtifact(block)) return null;
        CustomBlockData data = new CustomBlockData(block, Essence.getPlugin());
        return getBehavior(data.get(Key.Class.ARTIFACT.get(),  PersistentDataType.STRING));
    }

    /**
     * Register an artifact. Case-sensitive.
     * @param internalName the internal name of the artifact
     */
    public static void register(String internalName, EssenceConfig config) {
        if(!config.has(internalName) || !behaviors.containsKey(internalName) || activateArtifacts.containsKey(internalName)) return;
        // Not enabled
        if(!config.getBoolean(internalName + ".enabled", true)) return;

        ArtifactBuilder builder = new ArtifactBuilder(config.getString(internalName + ".material", "stone"))
                .internalName(internalName)
                .displayName(config.outString(internalName + ".display-name", ""))
                .lore(config.getStringList(internalName + ".lore"))
                .glowing(config.getBoolean(internalName + ".glowing", false))
                .placeable(config.getBoolean(internalName + ".placeable", true))
                .usable(config.getBoolean(internalName + ".usable", true))
                .particle(config.getConfigurationSection(internalName + ".particle"))
                .node(config.getConfigurationSection(internalName + ".node"));
        activateArtifacts.put(internalName, builder);
    }
}
