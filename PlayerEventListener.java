package com.augmentedvoid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEventListener implements Listener {
    
    private final DragonHeadManager dragonHeadManager;
    
    public PlayerEventListener(DragonHeadManager dragonHeadManager) {
        this.dragonHeadManager = dragonHeadManager;
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        
        if (newItem != null && newItem.getType() == Material.DRAGON_HEAD) {
            if (dragonHeadManager.hasAugmentedVoidEffects(player)) {
                player.sendMessage(Component.text("The void bends to your will...", NamedTextColor.DARK_PURPLE));
            }
        }
    }
}