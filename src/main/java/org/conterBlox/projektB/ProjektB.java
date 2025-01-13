package org.conterBlox.projektB;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.conterBlox.projektB.Command.LightBolt;
import org.conterBlox.projektB.Command.bullet1;
import org.conterBlox.projektB.hendler.EnderChestInteractHandler;
import org.conterBlox.projektB.hendler.InventoryCloseHandler;
import org.conterBlox.projektB.hendler.SimpleEventHendler;

public final class ProjektB extends JavaPlugin {

    private static ProjektB instance;
    private Inventory sharedEnderChest;

    @Override
    public void onEnable() {
        instance = this;

        // Регистрация существующих обработчиков и команд
        getServer().getPluginManager().registerEvents(new SimpleEventHendler(), this);
        getServer().getPluginCommand("lightbolt").setExecutor(new LightBolt());
//        getServer().getPluginCommand("bullet1").setExecutor( new bullet1());

        // Создаем общий инвентарь
        sharedEnderChest = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Shared Ender Chest");

        // Регистрируем новые обработчики для эндер-честов
        getServer().getPluginManager().registerEvents(new EnderChestInteractHandler(sharedEnderChest), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseHandler(sharedEnderChest, this), this);

        getLogger().info("Shared Ender Chest plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Логика отключения плагина
    }

    public static ProjektB getInstance() { // Метод для получения экземпляра
        return instance;
    }

}
