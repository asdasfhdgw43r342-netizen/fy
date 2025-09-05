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
        // Apply status effect to show VOID in GUI
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (potionManager.hasActivePotionEffect(player)) {
                // Apply a custom effect that will show as "Void" in inventory
                // Using LUCK effect renamed to show as void theme
                PotionEffect voidEffect = new PotionEffect(
                    PotionEffectType.LUCK, 
                    100, // 5 seconds duration (refreshed every 4 seconds)
                    0, // Level 1
                    true, // Ambient (less particles)
                    false, // No particles  
                    true // Show icon
                );
                player.addPotionEffect(voidEffect);
            } else {
                // Remove the effect if player no longer has void potion active
                if (player.hasPotionEffect(PotionEffectType.LUCK)) {
                    player.removePotionEffect(PotionEffectType.LUCK);
                }
            }
        }
    }
}