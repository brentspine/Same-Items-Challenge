package de.brentspine.sameitems.listeners;

import de.brentspine.sameitems.Main;
import de.brentspine.sameitems.util.BossBarManager;
import de.brentspine.sameitems.util.SettingsManager;
import net.minecraft.server.v1_16_R3.Advancement;
import net.minecraft.server.v1_16_R3.AdvancementTree;
import net.minecraft.server.v1_16_R3.Advancements;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerObtainItemListener implements Listener {

    public static HashMap<Material, String> obtainedItems = new HashMap<>();
    public static HashMap<String, ArrayList<Material>> whitelistedItems = new HashMap<>();
    public static HashMap<String, Integer> lives = new HashMap<>();
    public static HashMap<String, HashMap<String, Integer>> livesTookFrom = new HashMap<>();


    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        handleObtainItem(event.getWhoClicked(), event.getCurrentItem().getType());
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        handleObtainItem(event.getEntity(), event.getItem().getItemStack().getType());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if(event.getInventory().getType() == null || item == null) {
            return;
        }
        if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
            return;
        }
        if(item.getType() == Material.AIR) {
            return;
        }
        handleObtainItem(event.getWhoClicked(), item.getType());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack item = event.getCursor();
        if(event.getInventory().getType() == null || item == null) {
            return;
        }
        if(event.getInventory().getType() == InventoryType.PLAYER) {
            return;
        }
        if(item.getType() == Material.AIR) {
            return;
        }
        handleObtainItem(event.getWhoClicked(), item.getType());
    }


    public void handleObtainItem(Entity entity, Material material) {
        BossBarManager.run();
        if(!(entity instanceof Player)) {
            return;
        }
        if(!Main.instance.getTimer().isRunning()) {
            return;
        }
        Player player = (Player) entity;
        String damager = obtainedItems.get(material);
        if(obtainedItems.containsKey(material)) {
            if(obtainedItems.get(material) == player.getName() || whitelistedItems.get(player.getName()).contains(material)) {
                return;
            }
            player.sendMessage(Main.PREFIX + "Das Item §9" + material + "§7 wurde bereits eingesammelt von §9" + obtainedItems.get(material) + " §4§l(-♥)");
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
            //player.sendMessage(livesTookFrom.get(damager).get(player.getName()) + "");

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
                    ArrayList<Material> arrayList = new ArrayList<>();
                    for(Material current : obtainedItems.keySet()) {
                        arrayList.add(current);
                    }
                    for(Material current : arrayList) {
                        if(obtainedItems.get(current) == player.getName()) {
                            obtainedItems.remove(current);
                        }
                    }
                    for(Player current : Bukkit.getOnlinePlayers()) {
                        current.sendMessage(Main.PREFIX + "§9" + damager + "§7 wurden alle Leben zurückgegeben die ihm von §9" + player.getName() + "§7 abgezogen wurden und alle von §9" + player.getName() + "§7 eingesammelten Items wurden freigegeben");
                        //current.sendMessage(Main.PREFIX + "Es wurden alle von §9" + player.getName() + "§7 eingesammelten Items freigegeben");
                    }
                }
            }
        }
        if(!obtainedItems.containsKey(material)) {
            obtainedItems.put(material, player.getName());
        }
        ArrayList<Material> arrayList = whitelistedItems.get(player.getName());
        arrayList.add(material);
        whitelistedItems.put(player.getName(), arrayList);
        player.sendMessage(Main.PREFIX + "" + material + " §8(obtained)");
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
            whitelistedItems.put(player.getName(), new ArrayList<>());
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
            whitelistedItems.put(event.getPlayer().getName(), new ArrayList<>());
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


    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        if(event.getAdvancement().getKey().getKey().equalsIgnoreCase("end/kill_dragon")) {
            event.getPlayer().sendMessage(Main.PREFIX + "Congrats, you killed the Enderdragon in §9" + Main.instance.getTimer().getFormattedTime().getText());
            for(Player current : Bukkit.getOnlinePlayers()) {
                current.setGameMode(GameMode.SPECTATOR);
                if(!current.getName().equalsIgnoreCase(event.getPlayer().getName())) {
                    current.sendMessage(Main.PREFIX + "§9" + event.getPlayer().getName() + "§7 killed the Enderdragon in §9" + Main.instance.getTimer().getFormattedTime().getText());
                }
            }
            Main.instance.getTimer().setRunning(false);
        }
    }

}
