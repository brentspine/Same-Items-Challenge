package de.brentspine.sameitems.util;

import de.brentspine.sameitems.Main;
import de.brentspine.sameitems.listeners.PlayerObtainItemListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class BossBarManager {

    public static HashMap<String, BossBar> bars = new HashMap<>();

    public static void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!Main.instance.getTimer().isRunning()) {
                    return;
                }
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!bars.containsKey(player.getName())) {
                        BossBar bar = Bukkit.getServer().createBossBar("§4§l" + PlayerObtainItemListener.lives.get(player.getName()) + "♥", BarColor.BLUE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
                        bar.setVisible(true);
                        bar.setProgress(1.0);
                        bar.addPlayer(player);
                        String title = "§4§l";
                        for (int i = 0; i < PlayerObtainItemListener.lives.get(player.getName()); i++) {
                            title = title + "♥";
                        }
                        bars.put(player.getName(), bar);
                    }
                    String title = "§4§l";
                    for (int i = 0; i < PlayerObtainItemListener.lives.get(player.getName()); i++) {
                        title = title + "♥";
                    }
                    bars.get(player.getName()).setTitle(title);
                }
            }
        }.runTaskTimer(Main.instance, 20, 20);
    }

}
