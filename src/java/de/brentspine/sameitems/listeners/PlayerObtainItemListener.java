package de.brentspine.sameitems.listeners;

import de.brentspine.sameitems.Main;
import de.brentspine.sameitems.util.BossBarManager;
import de.brentspine.sameitems.util.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerObtainItemListener implements Listener {

    public static HashMap<Material, String> obtainedItems = new HashMap<>();
    public static HashMap<String, Integer> lives = new HashMap<>();
    public static HashMap<String, HashMap<String, Integer>> livesTookFrom = new HashMap<>();

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        BossBarManager.run();
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        if(!Main.instance.getTimer().isRunning()) {
            return;
        }
        Player player = (Player) event.getEntity();
        String damager = obtainedItems.get(event.getItem().getItemStack().getType());
        if(obtainedItems.containsKey(event.getItem().getItemStack().getType())) {
            if(obtainedItems.get(event.getItem().getItemStack().getType()) == player.getName()) {
                return;
            }
            player.sendMessage(damager);
            player.sendMessage(Main.PREFIX + "Das Item §9" + event.getItem().getItemStack().getType() + "§7 wurde bereits eingesammelt von §9" + obtainedItems.get(event.getItem().getItemStack().getType()) + " §4§l(-♥)");
            lives.put(player.getName(), lives.get(player.getName()) - 1);

            Integer oldValue;
            try {
                oldValue = livesTookFrom.get(damager).get(player.getName());
                livesTookFrom.get(damager).put(player.getName() , oldValue + 1);
            } catch (Exception e) {
                livesTookFrom.put(damager, new HashMap<>());
                oldValue = 0;
                livesTookFrom.get(damager).put(player.getName(), oldValue + 1);
            }
            player.sendMessage(livesTookFrom.get(damager).get(player.getName()) + "");

            player.playSound(player.getLocation(), Sound.ENTITY_CAT_HURT, 1, 1);
            player.sendTitle("§4-♥", "", 5, 35, 10);
            if(lives.get(player.getName()) <= 0) {
                player.setHealth(0);
                BossBarManager.bars.get(player.getName()).removePlayer(player);
                BossBarManager.bars.remove(player.getName());
                if(SettingsManager.giveBackLivesOnDeath) {
                    try {
                        lives.put(damager, livesTookFrom.get(player.getName()).get(damager) + lives.get(damager));
                    } catch (Exception e) {
                        livesTookFrom.put(damager, new HashMap<>());
                        lives.put(damager, livesTookFrom.get(player.getName()).get(damager) + lives.get(damager));
                    }
                    player.sendMessage(Main.PREFIX + "§9" + damager + "§7 wurden alle Leben zurückgegeben die ihm von §9" + player.getName() + "§7 abgezogen wurden");
                }
            }
            return;
        }
        obtainedItems.put(event.getItem().getItemStack().getType(), player.getName());
        player.sendMessage(Main.PREFIX + "" + event.getItem().getItemStack().getType().name() + " §8(obtained)");
    }

    public static void reset() {
        lives = new HashMap<>();
        livesTookFrom = new HashMap<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            lives.put(player.getName(), SettingsManager.startLives);
            HashMap<String, Integer> hashMap = new HashMap<>();
            for(Player current : Bukkit.getOnlinePlayers()) {
                hashMap.put(current.getName(), 0);
            }
            livesTookFrom.put(player.getName(), hashMap);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(Main.instance.getTimer().isRunning() && !lives.containsKey(event.getPlayer().getName())) {
            lives.put(event.getPlayer().getName(), SettingsManager.startLives);
            HashMap<String, Integer> hashMap = new HashMap<>();
            for(Player current : Bukkit.getOnlinePlayers()) {
                hashMap.put(current.getName(), 0);
            }
            livesTookFrom.put(event.getPlayer().getName(), hashMap);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BossBarManager.bars.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(Main.PREFIX + "§9" + event.getEntity().getPlayer().getName() + "§7 ist gestorben");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

}
