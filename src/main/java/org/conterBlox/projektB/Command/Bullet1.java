package org.conterBlox.projektB.Command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.conterBlox.projektB.ProjektB;
import org.bukkit.util.Vector;


import java.util.*;
import java.util.stream.Collectors;

public class Bullet1 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, что отправитель команды - это игрок
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender; // Приводим sender к Player

        // Проверяем, что команда называется "bullet1"
        if (command.getName().equalsIgnoreCase("bullet1")) {
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
                createRedCross(randomElement.getLocation(), randomElement);
            } else {
                player.sendMessage("No players nearby in the specified radius.");
            }
        }
        return true;
    }


    private void createRedCross(Location location, Player player) {
        double length = 5; // Длина креста от центра
        int particlesCount = 50; // Количество частиц в одной линии
        double height = 1; // Высота частиц над землёй
        final int[] ticks = {0}; // Используем массив для хранения ticks

        new BukkitRunnable() {
            @Override
            public void run() {
                if (ticks[0] < 40) { // 40 тиков = 2 секунды (20 тиков в секунду)
                    // Создание частиц креста
                    double[] angles = {0, Math.PI / 2, Math.PI, 3 * Math.PI / 2};
                    for (double angle : angles) {
                        for (int i = 0; i <= length * particlesCount; i++) {
                            double progress = i / (double) particlesCount;
                            double x = Math.cos(angle) * progress;
                            double z = Math.sin(angle) * progress;

                            Location particleLocation = location.clone().add(x, height, z);

                            if (particleLocation.getWorld() != null) {
                                particleLocation.getWorld().spawnParticle(Particle.DUST, particleLocation, 1, new Particle.DustOptions(Color.RED, 1));
                            }
                        }
                    }
                    ticks[0]++;
                } else if (ticks[0] == 40) {
                    // Стреляем стрелы после 40 тиков
                    shootArrows(location);
                    ticks[0]++;
                } else if (ticks[0] >= 60) {
                    // Останавливаем задачу после 60 тиков (3 секунды)
                    cancel();
                    location.getWorld().strikeLightning(location);
                }
            }
        }.runTaskTimer(ProjektB.getInstance(), 0, 1); // Запускаем задачу с интервалом 1 тик
    }

    private void shootArrows(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        // Длина линий креста
        double lineLength = 5.0;
        // Высота спавна стрел над землёй
        double height = 1.0;

        // Направления для линий креста
        Vector[] directions = {
                new Vector(1.0, 0.0, 0.0),  // Вперёд
                new Vector(-1.0, 0.0, 0.0), // Назад
                new Vector(0.0, 0.0, 1.0),  // Вправо
                new Vector(0.0, 0.0, -1.0)  // Влево
        };

        for (Vector direction : directions) {
            // Вычисляем точку спавна стрелы на краю линии
            Vector offset = direction.clone().multiply(lineLength);
            Location arrowSpawnLocation = location.clone().add(offset).add(0, height, 0);

            // Направление стрелы в центр
            Vector toCenter = location.toVector().subtract(arrowSpawnLocation.toVector()).normalize().multiply(0.5);

            // Создаём стрелу
            Arrow arrow = world.spawnArrow(
                    arrowSpawnLocation, // Точка спавна
                    toCenter,           // Направление стрелы к центру
                    2.5f,               // Начальная скорость
                    0                   // Точность
            );
            arrow.setShooter(null); // Стрелы без владельца
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED); // Нельзя подобрать
            arrow.setDamage(5.0); // Устанавливаем высокий урон
        }
    }
}