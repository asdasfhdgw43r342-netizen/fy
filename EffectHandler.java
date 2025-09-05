package com.augmentedvoid;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EffectHandler implements Listener {
    
    private final DragonHeadManager dragonHeadManager;
    
    public EffectHandler(DragonHeadManager dragonHeadManager) {
        this.dragonHeadManager = dragonHeadManager;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (dragonHeadManager.hasAugmentedVoidEffects(player)) {
            // Add particle effects when near void
            if (player.getLocation().getY() < 15) {
                player.getWorld().spawnParticle(
                    org.bukkit.Particle.PORTAL, 
                    player.getLocation().add(0, 0.5, 0), 
                    5, 
                    0.5, 0.5, 0.5, 
                    0.1
                );
                
                // Add dark particles for void effect
                player.getWorld().spawnParticle(
                    org.bukkit.Particle.CLOUD, 
                    player.getLocation().add(0, 1, 0), 
                    3, 
                    0.3, 0.3, 0.3, 
                    0.05
                );
            }
        }
    }
}