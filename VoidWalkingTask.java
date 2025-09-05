package com.augmentedvoid;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class VoidWalkingTask extends BukkitRunnable {
    
    private final JavaPlugin plugin;
    private final DragonHeadManager dragonHeadManager;
    private final Map<Location, Long> temporaryBlocks = new HashMap<>();
    
    public VoidWalkingTask(JavaPlugin plugin, DragonHeadManager dragonHeadManager) {
        this.plugin = plugin;
        this.dragonHeadManager = dragonHeadManager;
    }
    
    @Override
    public void run() {
        // Clean up expired temporary blocks
        cleanupExpiredBlocks();
        
        // Check all online players
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (dragonHeadManager.hasAugmentedVoidEffects(player)) {
                handleVoidWalking(player);
            }
        }
    }
    
    private void handleVoidWalking(Player player) {
        Location playerLoc = player.getLocation();
        World world = playerLoc.getWorld();
        
        if (world == null || playerLoc.getY() > 15) {
            return;
        }
        
        // Only create platform if player is falling or near void
        if (playerLoc.getY() <= 15) {
            // Create glass platform under the player
            int centerX = playerLoc.getBlockX();
            int centerZ = playerLoc.getBlockZ();
            int platformY = (int) Math.floor(playerLoc.getY() - 1.0); // Platform 1 block under player's feet
            
            // Create a 3x3 glass platform
            for (int x = centerX - 1; x <= centerX + 1; x++) {
                for (int z = centerZ - 1; z <= centerZ + 1; z++) {
                    Location blockLoc = new Location(world, x, platformY, z);
                    Block block = world.getBlockAt(blockLoc);
                    
                    if (block.getType() == Material.AIR && blockLoc.getY() >= 0) {
                        block.setType(Material.GLASS);
                        temporaryBlocks.put(blockLoc.clone(), System.currentTimeMillis() + 3000); // Remove after 3 seconds
                    }
                }
            }
            
            // Reset fall distance to prevent damage
            player.setFallDistance(0);
        }
    }
    
    private void cleanupExpiredBlocks() {
        long currentTime = System.currentTimeMillis();
        temporaryBlocks.entrySet().removeIf(entry -> {
            if (entry.getValue() <= currentTime) {
                Location loc = entry.getKey();
                Block block = loc.getWorld().getBlockAt(loc);
                if (block.getType() == Material.GLASS) {
                    block.setType(Material.AIR);
                }
                return true;
            }
            return false;
        });
    }
}