package com.idk.essence.items.arcana;

import com.idk.essence.utils.Key;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArcanaFactory implements Listener {

    /**
     * Used to register listener
     */
    @Getter private static final ArcanaFactory instance = new ArcanaFactory();

    private static final Map<String, Arcana> activeArcana = new HashMap<>();

    private ArcanaFactory() {}

    public static void initialize() {
        activeArcana.clear();
        ConfigManager.Folder.ITEMS_ARCANA.load(ArcanaFactory::register);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        // Wand can't break block
        if(isWand(player.getEquipment().getItemInMainHand()))
            event.setCancelled(true);
    }

    public static boolean has(String internalName) {
        return activeArcana.containsKey(internalName);
    }

    public static boolean isArcana(@Nullable ItemStack item) {
        return Key.Type.ARCANA.check(item);
    }

    public static boolean isWand(@Nullable ItemStack item) {
        return Key.Type.ARCANA_WAND.check(item);
    }

    public static boolean isRune(@Nullable ItemStack item) {
        return Key.Type.ARCANA_RUNE.check(item);
    }

    @Nullable
    public static Arcana get(@Nullable String internalName) {
        return activeArcana.get(internalName);
    }

    @Nullable
    public static ItemStack getItem(@NotNull String internalName) {
        Arcana arcana = get(internalName);
        return Optional.ofNullable(arcana).map(Arcana::create).orElse(null);
    }

    public static Collection<String> getAllKeys() {
        return activeArcana.keySet();
    }

    public static Collection<ItemStack> getAllItems() {
        return activeArcana.values().stream().map(Arcana::create).toList();
    }

    /**
     * Register an arcana. Case-sensitive.
     * @param internalName the internal name of the arcana
     */
    public static void register(@NotNull String internalName, EssenceConfig config) {
        if(!config.has(internalName) || has(internalName)) return;

        ArcanaRegistry registry = ArcanaRegistry.get(config.getString(internalName + ".type"));
        if(registry == null) return;
        Arcana arcana = registry.getConstructor().apply(config.getString(internalName + ".material"))
                .internalName(internalName).fromConfig(config).build();
        activeArcana.put(internalName, arcana);
    }
}
