package org.conterBlox.projektB.Command;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.conterBlox.projektB.ProjektB;
import org.bukkit.Particle;

import java.util.*;
import java.util.stream.Collectors;

public class LightBolt implements CommandExecutor  {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Проверяем, что отправитель команды - это игрок
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender; // Приводим sender к Player


        // Проверяем, что команда называется "lightbolt"
        if (command.getName().equalsIgnoreCase("lightbolt")) {
            // Получаем список игроков в радиусе 10 блоков, исключая тех, кто в команде lorteam
            List<Player> nearbyPlayers = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getLocation().distance(player.getLocation()) <= 10)
                    .filter(p -> p.getScoreboard().getPlayerTeam(p) == null || !p.getScoreboard().getPlayerTeam(p).getName().equals("lorteam"))
                    .collect(Collectors.toList());


            // Отправляем сообщение с именами игроков в радиусе
            if (!nearbyPlayers.isEmpty()) {
                String playerNames = nearbyPlayers.stream()
                        .map(Player::getName)
                        .collect(Collectors.joining(", "));
                Random r = new Random();


                // Получаем случайный элемент из списка
                Player randomElement = nearbyPlayers.get(r.nextInt(nearbyPlayers.size()));
                player.sendMessage("random player: " + randomElement.getName());
                createRedCircle(randomElement.getLocation());
            } else {
                player.sendMessage("No players nearby in the specified radius.");
            }
        }
        return true;
    }


    private void createRedCircle(Location location) {
        double radius = 1; // Радиус круга
        int particlesCount = 100; // Количество частиц
        double height = 0.1; // Высота частиц над землёй
        final int[] ticks = {0}; // Используем массив для хранения ticks

        // Используем BukkitRunnable для создания частиц
        new BukkitRunnable() {
            @Override
            public void run() {
                if (ticks[0] < 60) { // 40 тиков = 2 секунды (20 тиков в секунду)
                    // Вычисляем координаты частиц
                    for (int i = 0; i < particlesCount; i++) {
                        double angle = 2 * Math.PI * i / particlesCount; // Угол для каждой частицы
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);
                        Location particleLocation = location.clone().add(x, height, z);

                        // Проверка на null для мира
                        if (particleLocation.getWorld() != null) {
                            // Используем частицы дыхания дракона
                            particleLocation.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, new Particle.DustOptions(Color.RED, 1));
                        }
                    }
                    ticks[0]++; // Увеличиваем значение ticks
                } else {
                    cancel();
                    location.getWorld().strikeLightning(location);// Останавливаем задачу после 2 секунд
                }
            }
        }.runTaskTimer(ProjektB.getInstance(), 0, 1); // Запускаем задачу с интервалом 1 тик
    }
}