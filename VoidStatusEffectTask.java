package com.augmentedvoid;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidStatusEffectTask extends BukkitRunnable {
    
    private final JavaPlugin plugin;
    private final PotionManager potionManager;
    
    public VoidStatusEffectTask(JavaPlugin plugin, PotionManager potionManager) {
        this.plugin = plugin;
        this.potionManager = potionManager;
    }
    
    @Override
    public void run() {
        // Apply invisible status effect to show VOID icon in GUI
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (potionManager.hasActivePotionEffect(player)) {
                // Apply a harmless invisible effect that shows the custom icon
                // Using LUCK effect with custom model data for the icon
                PotionEffect voidEffect = new PotionEffect(
                    PotionEffectType.LUCK, 
                    100, // 5 seconds duration (refreshed every 4 seconds)
                    0, // Level 1
                    true, // Ambient (less particles)
                    false, // No particles
                    true // Show icon
                );
                player.addPotionEffect(voidEffect);
            }
        }
    }
}