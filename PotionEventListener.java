package com.augmentedvoid;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PotionEventListener implements Listener {
    
    private final PotionManager potionManager;
    
    public PotionEventListener(PotionManager potionManager) {
        this.potionManager = potionManager;
    }
    
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (potionManager.isAugmentedVoidPotion(item)) {
            // Activate the potion effect
            potionManager.activatePotion(player);
        }
    }
}