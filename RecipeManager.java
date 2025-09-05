package com.augmentedvoid;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeManager {
    
    private final JavaPlugin plugin;
    private final PotionManager potionManager;
    
    public RecipeManager(JavaPlugin plugin, PotionManager potionManager) {
        this.plugin = plugin;
        this.potionManager = potionManager;
    }
    
    public void registerRecipes() {
        registerAugmentedVoidPotionRecipe();
    }
    
    private void registerAugmentedVoidPotionRecipe() {
        ItemStack result = potionManager.createAugmentedVoidPotion();
        NamespacedKey key = new NamespacedKey(plugin, "void_potion");
        
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("DDD", "DPD", "DDD");
        recipe.setIngredient('D', Material.DIRT);
        recipe.setIngredient('P', Material.GLASS_BOTTLE);
        
        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("Registered VOID potion recipe!");
    }
}