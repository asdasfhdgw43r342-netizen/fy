package com.augmentedvoid;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DragonHeadManager implements Listener {
    private final PotionManager potionManager;
    
    public DragonHeadManager(PotionManager potionManager) {
        this.potionManager = potionManager;
    }
    
    public boolean hasAugmentedVoidEffects(Player player) {
        // Check if player has active potion effect
        return potionManager.hasActivePotionEffect(player);
    }
    
    public boolean hasAugmentedVoidWithDragonHead(Player player) {
        if (!hasAugmentedVoidEffects(player)) {
            return false;
        }
        
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        ItemStack helmet = player.getInventory().getHelmet();
        
        // Check if wearing or holding dragon head
        return (mainHand.getType() == Material.DRAGON_HEAD || 
                offHand.getType() == Material.DRAGON_HEAD ||
                (helmet != null && helmet.getType() == Material.DRAGON_HEAD));
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Let the VoidWalkingTask handle platform creation
        if (hasAugmentedVoidEffects(player) && player.getLocation().getY() < 15) {
            // Just reset fall distance, let them move naturally on the glass platforms
            player.setFallDistance(0);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getEntity();
        
        if (hasAugmentedVoidEffects(player)) {
            EntityDamageEvent.DamageCause cause = event.getCause();
            
            // Cancel fall damage, fire damage, and lava damage
            if (cause == EntityDamageEvent.DamageCause.FALL ||
                cause == EntityDamageEvent.DamageCause.FIRE ||
                cause == EntityDamageEvent.DamageCause.FIRE_TICK ||
                cause == EntityDamageEvent.DamageCause.LAVA) {
                event.setCancelled(true);
            }
        }
    }
    
    private void createVoidPlatform(Player player) {
        // Implementation for creating a temporary platform under the player
        // This is a placeholder - implement according to your needs
        player.sendActionBar(net.kyori.adventure.text.Component.text("Void platform created!", net.kyori.adventure.text.format.NamedTextColor.GREEN));
    }
}