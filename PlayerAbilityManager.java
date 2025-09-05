package com.augmentedvoid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAbilityManager implements Listener {
    private final Map<UUID, Long> lastAbilityUse = new HashMap<>();
    private final Map<UUID, Boolean> abilityActive = new HashMap<>();
    private final PotionManager potionManager;
    
    private static final long ABILITY_COOLDOWN = 30 * 1000; // 30 seconds in milliseconds
    private static final long ABILITY_DURATION = 20 * 1000; // 20 seconds active duration
    private static final int WITHER_EFFECT_DURATION = 400; // 20 seconds
    private static final int WITHER_EFFECT_AMPLIFIER = 1; // Wither II
    private static final int SLOWNESS_EFFECT_DURATION = 400; // 20 seconds
    private static final int SLOWNESS_EFFECT_AMPLIFIER = 1; // Slowness II
    private static final int BLINDNESS_EFFECT_DURATION = 400; // 20 seconds
    private static final int BLINDNESS_EFFECT_AMPLIFIER = 0; // Blindness I
    
    public PlayerAbilityManager(PotionManager potionManager) {
        this.potionManager = potionManager;
    }
    
    public void useAbility(Player player, int abilityLevel) {
        if (player == null) {
            return;
        }
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check cooldown
        if (lastAbilityUse.containsKey(playerId)) {
            long lastUse = lastAbilityUse.get(playerId);
            if (currentTime - lastUse < ABILITY_COOLDOWN) {
                long remaining = (ABILITY_COOLDOWN - (currentTime - lastUse)) / 1000;
                player.sendMessage(Component.text("Ability is on cooldown for " + remaining + " more seconds", 
                    NamedTextColor.RED));
                return;
            }
        }
        
        // Check if player has active potion effect
        if (potionManager != null && !potionManager.hasActivePotionEffect(player)) {
            player.sendMessage(Component.text("You need an active VOID effect to use abilities!", NamedTextColor.RED));
            return;
        }
        
        if (abilityLevel <= 0) {
            player.sendMessage(Component.text("Invalid ability level!", NamedTextColor.RED));
            return;
        }
        
        if (abilityLevel != 1) {
            player.sendMessage(Component.text("Only ability level 1 is available!", NamedTextColor.RED));
            return;
        }
        
        // Apply effects to nearby entities
        activateAbility(player, abilityLevel);
        
        // Set cooldown
        lastAbilityUse.put(playerId, currentTime);
        
        player.sendMessage(Component.text("Void ability activated! Your next attacks will inflict void effects for 20 seconds.", NamedTextColor.DARK_PURPLE));
    }
    
    private void activateAbility(Player player, int abilityLevel) {
        UUID playerId = player.getUniqueId();
        abilityActive.put(playerId, true);
        
        // Schedule ability deactivation after duration
        org.bukkit.Bukkit.getScheduler().runTaskLater(
            org.bukkit.Bukkit.getPluginManager().getPlugin("AugmentedVoid"), 
            () -> {
                abilityActive.remove(playerId);
                if (player.isOnline()) {
                    player.sendMessage(Component.text("Void ability has worn off.", NamedTextColor.GRAY));
                }
            }, 
            400L // 20 seconds in ticks
        );
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        UUID playerId = player.getUniqueId();
        
        // Check if player has ability active and VOID effect
        if (!abilityActive.getOrDefault(playerId, false) || 
            !potionManager.hasActivePotionEffect(player)) {
            return;
        }
        
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getEntity();
            
            // Apply Wither effect
            target.addPotionEffect(new PotionEffect(
                PotionEffectType.WITHER, 
                WITHER_EFFECT_DURATION,
                WITHER_EFFECT_AMPLIFIER,
                false, 
                true
            ));
            
            // Apply Slowness effect
            target.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS, 
                SLOWNESS_EFFECT_DURATION,
                SLOWNESS_EFFECT_AMPLIFIER,
                false, 
                true
            ));
            
            // Apply Blindness effect
            target.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS, 
                BLINDNESS_EFFECT_DURATION,
                BLINDNESS_EFFECT_AMPLIFIER,
                false, 
                true
            ));
            
            // Visual feedback
            player.sendMessage(Component.text("Void effects applied to " + target.getName() + "!", NamedTextColor.DARK_PURPLE));
            
            // Spawn dark particles around the target
            target.getWorld().spawnParticle(
                org.bukkit.Particle.CAMPFIRE_COSY_SMOKE, 
                target.getLocation().add(0, 1, 0), 
                10, 
                0.5, 0.5, 0.5, 
                0.1
            );
        }
    }
    
    public boolean hasActiveAbility(Player player) {
        return abilityActive.getOrDefault(player.getUniqueId(), false);
    }
    
    public void clearAbility(Player player) {
        UUID playerId = player.getUniqueId();
        abilityActive.remove(playerId);
        lastAbilityUse.remove(playerId);
    }
}