package de.brentspine.sameitems.listeners;

import de.brentspine.sameitems.Main;
import de.brentspine.sameitems.util.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerObtainItemListener implements Listener {

    public static HashMap<Material, String> obtainedItems = new HashMap<>();
    public static HashMap<String, Integer> lives = new HashMap<>();

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        if(!Main.instance.getTimer().isRunning()) {
            return;
        }
        Player player = (Player) event.getEntity();
        if(obtainedItems.containsKey(event.getItem().getItemStack().getType())) {
            if(obtainedItems.get(event.getItem().getItemStack().getType()) == player.getName()) {
                return;
            }
            player.sendMessage(obtainedItems.get(event.getItem().getItemStack().getType()));
            player.sendMessage(Main.PREFIX + "Das Item §9" + event.getItem().getItemStack().getType() + "§7 wurde bereits eingesammelt von §9" + obtainedItems.get(event.getItem().getItemStack().getType()) + " §4(-♥)");
            lives.put(player.getName(), lives.get(player.getName()) - 1);
            player.sendMessage(lives.get(player.getName()) + "");
            return;
        }
        obtainedItems.put(event.getItem().getItemStack().getType(), player.getName());
        player.sendMessage(Main.PREFIX + "+" + event.getItem().getItemStack().getType().name() + " §8(obtained)");
    }

    public static void reset() {
        lives = new HashMap<>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            lives.put(player.getName(), SettingsManager.startLives);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(Main.instance.getTimer().isRunning() && !lives.containsKey(event.getPlayer().getName())) {
            lives.put(event.getPlayer().getName(), SettingsManager.startLives);
        }
    }

}
