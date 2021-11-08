package de.brentspine.sameitems.util;

import de.brentspine.sameitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Timer {

    private boolean running;
    private double time;

    private int seconds;
    private int minutes;
    private int hours;
    private int days;
    private int weeks;
    private int months;

    private String convertedSeconds;
    private String convertedMinutes;
    private String convertedHours;
    private String convertedDays;


    public Timer(boolean running, int time) {
        this.running = running;
        this.time = time;

        run();
    }


    public boolean isRunning() {
        return running;
    }


    public void setRunning(boolean running) {
        this.running = running;
    }


    public double getTime() {
        return time;
    }


    public void setTime(double d) {
        this.seconds = (int) d;
        convertInput();
    }


    public void sendActionBar() {

        TextComponent message = getFormattedTime();

        for(Player player : Bukkit.getOnlinePlayers()) {

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        }

    }


    public TextComponent getFormattedTime() {
        TextComponent message = new TextComponent("§c§lLoading...");
        if(!isRunning() ) {
            message = new TextComponent(ChatColor.RED.toString() +
                    ChatColor.BOLD + "Der Timer ist pausiert");
        } else {
            convertInput();
            if(days > 0)
                if(hours > 0)
                    message = new TextComponent(ChatColor.GOLD.toString() +
                            ChatColor.BOLD + convertedDays + ChatColor.GOLD + ChatColor.BOLD + convertedHours + ":" + convertedMinutes + convertedSeconds);
                else
                    message = new TextComponent(ChatColor.GOLD.toString() +
                            ChatColor.BOLD + convertedDays + ChatColor.GOLD + ChatColor.BOLD + convertedMinutes + convertedSeconds);
            else if(hours > 0)
                message = new TextComponent(ChatColor.GOLD.toString() +
                        ChatColor.BOLD + convertedHours + ":" + convertedMinutes + convertedSeconds);
            else
                message = new TextComponent(ChatColor.GOLD.toString() +
                        ChatColor.BOLD + convertedMinutes + convertedSeconds);
        }
        return message;
    }

    private void run() {
        new BukkitRunnable() {

            @Override
            public void run() {

                sendActionBar();

                if(!isRunning()) {
                    return;
                }

                seconds = seconds + 1;
                convertInput();

            }
        }.runTaskTimer(Main.instance, 20, 20);
    }



    public int getSeconds() {
        return seconds;
    }


    public void setSeconds(int seconds) {
        this.seconds = seconds;
        convertInput();
    }


    public int getMinutes() {
        return minutes;
    }


    public void setMinutes(int minutes) {
        this.minutes = minutes;
        convertInput();
    }


    public int getHours() {
        return hours;
    }


    public void setHours(int hours) {
        this.hours = hours;
        convertInput();
    }


    public int getDays() {
        return days;
    }


    public void setDays(int days) {
        this.days = days;
        convertInput();
    }


    public int getWeeks() {
        return weeks;
    }


    public void setWeeks(int weeks) {
        this.weeks = weeks;
        convertInput();
    }


    public int getMonths() {
        return months;
    }


    public void setMonths(int months) {
        this.months = months;
        convertInput();
    }

    public void convertInput() {

        int Seconds = this.seconds;
        int Minutes = this.minutes;
        int Hours = this.hours;
        int Days = this.days;
        int Weeks = this.weeks;
        int Months = this.months;



        //String convertedTime;


        while(Seconds >= 60) {
            Minutes++;
            Seconds = Seconds - 60;
        }

        while(Minutes >= 60) {
            Hours++;
            Minutes = Minutes - 60;
        }

        while(Hours >= 24) {
            Days++;
            Hours = Hours - 24;
        }

        //convertedTime = Minutes + ":" + Seconds;

        this.seconds = Seconds;
        this.minutes = Minutes;
        this.hours = Hours;
        this.days = Days;
        this.weeks = Weeks;
        this.months = Months;

        if(this.seconds <= 9)
            convertedSeconds = ":0" + this.seconds;
        else
            convertedSeconds = ":" + this.seconds;

        if(this.minutes <= 9)
            convertedMinutes = "0" + this.minutes;
        else
            convertedMinutes = "" + this.minutes;

        if(this.hours <= 9)
            convertedHours = "0" + this.hours;
        else
            convertedHours = "" + this.hours;

        if(this.days >= 2)
            convertedDays = this.days + " Tage§r" + ChatColor.GOLD + ", ";
        else if(this.days >= 1)
            convertedDays = this.days + " Tag§r" + ChatColor.GOLD + ", ";


        if(convertedSeconds == null)
            convertedSeconds = "0";

        if(convertedMinutes == null)
            convertedMinutes = "0";

        if(convertedHours == null)
            convertedHours = "0";

        if(convertedDays == null)
            convertedDays = "0";

    }

}

