package org.conterBlox.projektB.hendler;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class EnderChestInteractHandler implements Listener {
    private final Inventory sharedEnderChest;

    public EnderChestInteractHandler(Inventory sharedEnderChest) {
        this.sharedEnderChest = sharedEnderChest;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.ENDER_CHEST) {
                event.setCancelled(true);
                event.getPlayer().openInventory(sharedEnderChest);
            }
        }
    }
}

