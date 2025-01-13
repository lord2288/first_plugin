package org.conterBlox.projektB.hendler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class InventoryCloseHandler implements Listener {
    private final Inventory sharedEnderChest;
    private final Plugin plugin;

    public InventoryCloseHandler(Inventory sharedEnderChest, Plugin plugin) {
        this.sharedEnderChest = sharedEnderChest;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(sharedEnderChest)) {
            plugin.getLogger().info(event.getPlayer().getName() + " closed the shared Ender Chest.");
        }
    }
}

