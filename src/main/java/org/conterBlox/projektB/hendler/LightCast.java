package org.conterBlox.projektB.hendler;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class LightCast {
    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Проверяем, что предмет не пустой и его название совпадает
        if (item.getType() == Material.CARROT_ON_A_STICK) {
            player.sendMessage("Вы активировали специальный предмет!");

        }
    }
}

