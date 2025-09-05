package com.augmentedvoid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PotionManager {
    
    private final Set<UUID> activePotions = new HashSet<>();
    
    public PotionManager() {
    }
    
    public ItemStack createAugmentedVoidPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("VOID the heart of darkness").color(NamedTextColor.BLACK));
            meta.lore(Arrays.asList(
                Component.text("A mysterious potion that grants").color(NamedTextColor.GRAY),
                Component.text("supernatural protection when consumed.").color(NamedTextColor.GRAY),
                Component.empty(),
                Component.text("Effects when active:").color(NamedTextColor.DARK_GRAY),
                Component.text("• Walk on void with speed boost").color(NamedTextColor.YELLOW),
                Component.text("• No fall damage").color(NamedTextColor.YELLOW),
                Component.text("• Fire/Lava immunity").color(NamedTextColor.YELLOW),
                Component.text("• Special abilities with dragon head").color(NamedTextColor.YELLOW),
                Component.empty(),
                Component.text("Duration: Permanent until lost").color(NamedTextColor.GOLD)
            ));
            
            // Set base potion data to make it drinkable (using water bottle as base)
            meta.setBasePotionData(new PotionData(PotionType.WATER, false, false));
            meta.setColor(org.bukkit.Color.BLACK); // Black color
            
            // Add custom model data for resource pack support
            meta.setCustomModelData(1001); // This will be used for the eye of ender texture
            
            // Add custom NBT data to identify this as a VOID potion for status effects
            meta.getPersistentDataContainer().set(
                new org.bukkit.NamespacedKey(org.bukkit.Bukkit.getPluginManager().getPlugin("AugmentedVoid"), "void_potion"),
                org.bukkit.persistence.PersistentDataType.BYTE,
                (byte) 1
            );
            
            potion.setItemMeta(meta);
        }
        
        return potion;
    }
    
    public boolean isAugmentedVoidPotion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION) {
            return false;
        }
        
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        return meta.displayName().equals(Component.text("VOID the heart of darkness").color(NamedTextColor.BLACK));
    }
    
    public void activatePotion(Player player) {
        UUID playerId = player.getUniqueId();
        activePotions.add(playerId);
        
        // Give visual feedback
        player.sendMessage(Component.text("The void's power flows through you...").color(NamedTextColor.DARK_PURPLE));
    }
    
    public boolean hasActivePotionEffect(Player player) {
        UUID playerId = player.getUniqueId();
        return activePotions.contains(playerId);
    }
    
    public void drainPotion(Player player) {
        UUID playerId = player.getUniqueId();
        if (activePotions.contains(playerId)) {
            activePotions.remove(playerId);
            
            // Give back the potion
            ItemStack potion = createAugmentedVoidPotion();
            player.getInventory().addItem(potion);
            
            player.sendMessage(Component.text("The void's power has been drained back into a potion.")
                .color(NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("You don't have an active VOID effect to drain.")
                .color(NamedTextColor.RED));
        }
    }
    
    public void removeEffect(Player player) {
        UUID playerId = player.getUniqueId();
        if (activePotions.contains(playerId)) {
            activePotions.remove(playerId);
            player.sendMessage(Component.text("The void's power fades from your body...").color(NamedTextColor.GRAY));
        }
    }
}