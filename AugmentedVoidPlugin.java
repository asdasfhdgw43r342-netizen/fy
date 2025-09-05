package com.augmentedvoid;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AugmentedVoidPlugin extends JavaPlugin {
    private static AugmentedVoidPlugin instance;
    private PlayerAbilityManager abilityManager;
    private PotionManager potionManager;
    private DragonHeadManager dragonHeadManager;
    private RecipeManager recipeManager;
    private VoidWalkingTask voidWalkingTask;
    private VoidStatusEffectTask statusEffectTask;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.potionManager = new PotionManager();
        this.abilityManager = new PlayerAbilityManager(potionManager);
        this.dragonHeadManager = new DragonHeadManager(potionManager);
        this.recipeManager = new RecipeManager(this, potionManager);

        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(dragonHeadManager, this);
        pm.registerEvents(abilityManager, this);
        pm.registerEvents(new PlayerEventListener(dragonHeadManager), this);
        pm.registerEvents(new PotionEventListener(potionManager), this);
        pm.registerEvents(new EffectHandler(dragonHeadManager), this);
        
        // Register recipes
        recipeManager.registerRecipes();
        
        // Start void walking task
        this.voidWalkingTask = new VoidWalkingTask(this, dragonHeadManager);
        this.voidWalkingTask.runTaskTimer(this, 0L, 5L); // Run every 5 ticks
        
        // Start status effect task for GUI icon
        this.statusEffectTask = new VoidStatusEffectTask(this, potionManager);
        this.statusEffectTask.runTaskTimer(this, 0L, 80L); // Run every 4 seconds
        
        // Register commands
        getCommand("augmentedvoid").setExecutor(new CommandHandler(this, potionManager));
        getCommand("userability").setExecutor(new CommandHandler(this, potionManager));
        getCommand("userdrain").setExecutor(new CommandHandler(this, potionManager));
        getCommand("userrecipe").setExecutor(new CommandHandler(this, potionManager));
        
        getLogger().info("AugmentedVoid plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        if (voidWalkingTask != null) {
            voidWalkingTask.cancel();
        }
        if (statusEffectTask != null) {
            statusEffectTask.cancel();
        }
        getLogger().info("AugmentedVoid plugin has been disabled!");
    }

    public static AugmentedVoidPlugin getInstance() {
        return instance;
    }

    public PlayerAbilityManager getAbilityManager() {
        return abilityManager;
    }

    public PotionManager getPotionManager() {
        return potionManager;
    }

    public ItemStack createDragonHead() {
        ItemStack head = new ItemStack(Material.DRAGON_HEAD);
        ItemMeta meta = head.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text("Dragon Head", 
                net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE));
            head.setItemMeta(meta);
        }
        return head;
    }
}