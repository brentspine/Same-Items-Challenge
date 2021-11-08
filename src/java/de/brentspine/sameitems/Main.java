package de.brentspine.sameitems;

import de.brentspine.sameitems.commands.TimerCommand;
import de.brentspine.sameitems.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;
    private Timer timer;

    public static final String PREFIX = "§b§lChallenge §8» §7";
    public static final String NOPERM = PREFIX + "§cDazu hast du keine Berechtigungen!";

    @Override
    public void onEnable() {
        instance = this;
        timer = new Timer(false, 0);
        getCommand("timer").setExecutor(new TimerCommand());
        Bukkit.broadcastMessage("Hello World");
    }


    public Timer getTimer() {
        return timer;
    }

}
