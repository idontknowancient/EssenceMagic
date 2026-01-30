package com.idk.essence.utils.recipes;

import com.idk.essence.Essence;
import com.idk.essence.items.ItemResolver;
import com.idk.essence.utils.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RecipeManager implements Listener {

    @Getter private static final RecipeManager instance = new RecipeManager();
    private static final Set<NamespacedKey> recipes = new HashSet<>();
    private static final Map<String, ConfigurationSection> recipeToBeAdded = new HashMap<>();

    private RecipeManager() {}

    public static void shutdown() {
        recipes.forEach(Bukkit::removeRecipe);
        recipes.clear();
        recipeToBeAdded.clear();
    }

    public static void initialize() {
        recipeToBeAdded.forEach(RecipeManager::loadRecipe);
        Util.System.info("Registered Recipes", recipes.size());
    }

    /**
     * Stop players from crafting if they have not found the recipe.
     */
    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if(!(recipe instanceof Keyed keyed)) return;
        NamespacedKey key = keyed.getKey();
        if(!recipes.contains(key)) return;
        if(!(event.getView().getPlayer() instanceof Player player)) return;
        if(!player.hasDiscoveredRecipe(key)) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    public static void add(@NotNull String internalName, @Nullable ConfigurationSection section) {
        if(section == null || recipeToBeAdded.containsKey(internalName)) return;
        recipeToBeAdded.put(internalName, section);
    }

    public static void loadRecipe(@NotNull String internalName, @NotNull ConfigurationSection recipeSection) {
        String type = recipeSection.getString("type", "shaped");
        switch(type.toUpperCase()) {
            case "SHAPED" -> registerShaped(internalName, recipeSection);
            case "SHAPELESS" -> registerShapeless(internalName, recipeSection);
        }
    }

    @NotNull
    private static NamespacedKey getRecipeKey(String internalName) {
        String sanitized = internalName.toLowerCase().replaceAll("[^a-z0-9._-]", "_");
        return new NamespacedKey(Essence.getPlugin(), sanitized);
    }

    @NotNull
    private static RecipeChoice parseIngredient(@Nullable String internalName) {
        if(internalName == null) return new RecipeChoice.MaterialChoice(Material.STONE);
        ItemStack item = ItemResolver.get(internalName);
        if(item != null)
            return new RecipeChoice.ExactChoice(item);
        else
            return new RecipeChoice.MaterialChoice(
                    Optional.ofNullable(Material.getMaterial(internalName.toUpperCase())).orElse(Material.STONE));
    }

    private static void registerShaped(String internalName, ConfigurationSection section) {
        ItemStack result = ItemResolver.get(internalName);
        List<String> shape = section.getStringList("shape");
        ConfigurationSection ingredients = section.getConfigurationSection("ingredients");
        if(result == null || shape.isEmpty() || ingredients == null) return;

        NamespacedKey key = getRecipeKey(internalName);
        result.setAmount(Math.clamp(section.getInt("count", 1), 1, 64));
        ShapedRecipe recipe = new ShapedRecipe(key, result);

        // Shape, e.g. "E E", "DTD", "OCO"
        recipe.shape(shape.toArray(new String[0]));

        // Ingredients, e.g. E: echo_shard
        for(String symbol : ingredients.getKeys(false)) {
            recipe.setIngredient(symbol.charAt(0), parseIngredient(ingredients.getString(symbol)));
        }

        // Prevent repetition
        Bukkit.removeRecipe(key);
        Bukkit.addRecipe(recipe);
        recipes.add(key);
    }

    private static void registerShapeless(String internalName, ConfigurationSection section) {
        ItemStack result = ItemResolver.get(internalName);
        List<String> ingredients = section.getStringList("ingredients");
        if(result == null || ingredients.isEmpty()) return;

        NamespacedKey key = getRecipeKey(internalName);
        result.setAmount(Math.clamp(section.getInt("count", 1), 1, 64));
        ShapelessRecipe recipe = new ShapelessRecipe(key, result);

        for(String input : ingredients) {
            recipe.addIngredient(parseIngredient(input));
        }

        // Prevent repetition
        Bukkit.removeRecipe(key);
        Bukkit.addRecipe(recipe);
        recipes.add(key);
    }
}
