package com.augmentedvoid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private final AugmentedVoidPlugin plugin;
    private final PotionManager potionManager;
    
    public CommandHandler(AugmentedVoidPlugin plugin, PotionManager potionManager) {
        this.plugin = plugin;
        this.potionManager = potionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("userability")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                return true;
            }
            
            Player player = (Player) sender;
            
            if (args.length == 0) {
                player.sendMessage(Component.text("Usage: /userability <1>", NamedTextColor.RED));
                return true;
            }
            
            try {
                int ability = Integer.parseInt(args[0]);
                if (ability != 1) {
                    throw new NumberFormatException();
                }
                plugin.getAbilityManager().useAbility(player, ability);
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Please use: /userability 1", NamedTextColor.RED));
            }
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("userdrain")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                return true;
            }
            
            Player player = (Player) sender;
            
            if (args.length == 0) {
                player.sendMessage(Component.text("Usage: /userdrain <1>", NamedTextColor.RED));
                return true;
            }
            
            try {
                int level = Integer.parseInt(args[0]);
                if (level != 1) {
                    throw new NumberFormatException();
                }
                potionManager.drainPotion(player);
            } catch (NumberFormatException e) {
                player.sendMessage(Component.text("Please use: /userdrain 1", NamedTextColor.RED));
            }
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("userrecipe")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                return true;
            }
            
            Player player = (Player) sender;
            showRecipe(player);
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("augmentedvoid")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                return true;
            }
            
            Player player = (Player) sender;
            
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                showHelp(player);
                return true;
            }
            
            switch (args[0].toLowerCase()) {
                case "dragonhead":
                    player.getInventory().addItem(plugin.createDragonHead());
                    player.sendMessage(Component.text("You have been given a dragon head!", NamedTextColor.GREEN));
                    break;
                    
                case "give":
                    player.getInventory().addItem(potionManager.createAugmentedVoidPotion());
                    player.sendMessage(Component.text("You have been given a VOID potion!", NamedTextColor.GREEN));
                    break;
                    
                case "recipe":
                    showRecipe(player);
                    break;
                    
                default:
                    showHelp(player);
                    break;
            }
        }
        
        return true;
    }
    
    private void showHelp(Player player) {
        player.sendMessage(Component.text("=== AugmentedVoid Plugin Help ===", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Craft and drink the VOID potion using 9 dirt blocks.", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("Effects when active:", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("• Walk on the void", NamedTextColor.AQUA));
        player.sendMessage(Component.text("• Immunity to fall damage", NamedTextColor.AQUA));
        player.sendMessage(Component.text("• Immunity to fire and lava damage", NamedTextColor.AQUA));
        player.sendMessage(Component.text("Commands:", NamedTextColor.GOLD));
        player.sendMessage(Component.text("• /userability 1 - Use void ability (affects nearby enemies)", NamedTextColor.AQUA));
        player.sendMessage(Component.text("• /userdrain 1 - Drain effect back into potion", NamedTextColor.AQUA));
        player.sendMessage(Component.text("• /augmentedvoid dragonhead - Get a dragon head", NamedTextColor.AQUA));
        player.sendMessage(Component.text("• /augmentedvoid give - Get a VOID potion", NamedTextColor.AQUA));
        player.sendMessage(Component.text("• /userrecipe - Show crafting recipe", NamedTextColor.AQUA));
    }
    
    private void showRecipe(Player player) {
        player.sendMessage(Component.text("=== VOID Potion Recipe ===", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Crafting Pattern (3x3):", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("[D] [D] [D]", NamedTextColor.GRAY));
        player.sendMessage(Component.text("[D] [P] [D]", NamedTextColor.GRAY));
        player.sendMessage(Component.text("[D] [D] [D]", NamedTextColor.GRAY));
        player.sendMessage(Component.text("D = Dirt Block", NamedTextColor.AQUA));
        player.sendMessage(Component.text("P = Glass Bottle", NamedTextColor.AQUA));
        player.sendMessage(Component.text("Result: VOID the heart of darkness potion", NamedTextColor.DARK_PURPLE));
    }
}