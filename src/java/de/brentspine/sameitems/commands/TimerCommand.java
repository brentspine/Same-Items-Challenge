package de.brentspine.sameitems.commands;

import de.brentspine.sameitems.Main;
import de.brentspine.sameitems.util.Timer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length > 0) {

                switch(args[0].toLowerCase()) {

                    case "start":
                    case "resume": {
                        if(!player.hasPermission("timer.resume")) {
                            player.sendMessage(Main.NOPERM);
                            return true;
                        }
                        Timer timer = Main.instance.getTimer();

                        if(timer.isRunning()) {
                            player.sendMessage("§cDer Timer läuft bereits!");
                            break;
                        }

                        timer.setRunning(true);
                        player.sendMessage("§aDer Timer wurde fortgesetzt!");

                        timer.convertInput();

                        break;
                    }

                    case "stop":
                    case "pause": {
                        if(!player.hasPermission("timer.pause")) {
                            player.sendMessage(Main.NOPERM);
                            return true;
                        }
                        Timer timer = Main.instance.getTimer();

                        if(!timer.isRunning()) {
                            player.sendMessage("§cDer Timer ist bereits pausiert!");
                            break;
                        }

                        timer.setRunning(false);
                        player.sendMessage("§aDer Timer wurde pausiert!");
                        break;
                    }

                    case "time":
                    case "set": {
                        if(!player.hasPermission("timer.set")) {
                            player.sendMessage(Main.NOPERM);
                            return true;
                        }
                        try {
                            Timer timer = Main.instance.getTimer();
                            Integer time = 0;
                            switch (args.length) {
                                case 2:
                                    timer.setDays(0);
                                    timer.setHours(0);
                                    timer.setMinutes(0);
                                    timer.setSeconds(Integer.parseInt(args[1]));
                                    player.sendMessage("§aDie Zeit wurde auf " + args[1] + "s §agesetzt!");
                                    break;
                                case 3:
                                    timer.setDays(0);
                                    timer.setHours(0);
                                    timer.setMinutes(Integer.parseInt(args[1]));
                                    timer.setSeconds(Integer.parseInt(args[2]));
                                    player.sendMessage("§aDie Zeit wurde auf " + args[1] + "m, " + args[2] + "s §agesetzt!");
                                    break;
                                case 4:
                                    timer.setDays(0);
                                    timer.setHours(Integer.parseInt(args[1]));
                                    timer.setMinutes(Integer.parseInt(args[2]));
                                    timer.setSeconds(Integer.parseInt(args[3]));
                                    player.sendMessage("§aDie Zeit wurde auf " + args[1] + "h, " + args[2] + "m, " + args[3] + "s §agesetzt!");
                                    break;
                                case 5:
                                    timer.setDays(Integer.parseInt(args[1]));
                                    timer.setHours(Integer.parseInt(args[2]));
                                    timer.setMinutes(Integer.parseInt(args[3]));
                                    timer.setSeconds(Integer.parseInt(args[4]));
                                    player.sendMessage("§aDie Zeit wurde auf " + args[1] + "d, " + args[2] + "h, " + args[3] + "m, " + args[4] + "s §agesetzt!");
                                    break;

                                default:
                                    player.sendMessage("§2§lVerwendung");
                                    player.sendMessage("§9/timer " + args[0] + " <s> §7- Setzt die Sekunden");
                                    player.sendMessage("§9/timer " + args[0] + " <m> <s> §7- Setzt die Minuten und Sekunden");
                                    player.sendMessage("§9/timer " + args[0] + " <h> <m> <s> §7- Setzt h, m und s");
                                    player.sendMessage("§9/timer " + args[0] + " <d> <h> <m> <s> §7- Setzt d, h, m und s");
                                    player.sendMessage("§2§lBeispiele");
                                    player.sendMessage("§9/timer " + args[0] + " 3 56 17 §7- Setzt Timer auf 3h, 56m, 17s");
                                    player.sendMessage("§9/timer " + args[0] + " 6 48 §7- Setzt Timer auf 6m, 48s");
                                    player.sendMessage("§9/timer " + args[0] + " 150 §7- Setzt Timer auf 2m, 30s");
                                    player.sendMessage("§7Plugin von §dBrentspine");
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cBitte gebe eine gültige Zahl an!");
                        }
                        break;
                    }

                    case "reset": {
                        if(!player.hasPermission("timer.reset")) {
                            player.sendMessage(Main.NOPERM);
                            return true;
                        }
                        Timer timer = Main.instance.getTimer();

                        timer.setRunning(false);
                        timer.setTime(-1);
                        timer.setMonths(0);
                        timer.setWeeks(0);
                        timer.setDays(0);
                        timer.setHours(0);
                        timer.setMinutes(0);
                        timer.setSeconds(0);
                        player.sendMessage("§aDer Timer wurde zurückgesetzt");
                        break;
                    }


                    default:
                        sendUsage(player);
                        break;
                }
            } else
                sendUsage(player);
        }

        return false;
    }

    private void sendUsage(Player player) {
        player.sendMessage("§2§lVerfügbare Commands§8:");
        player.sendMessage("§9/timer resume §7- Startet den Timer");
        player.sendMessage("§9/timer pause §7- Pausiert den Timer");
        player.sendMessage("§9/timer set <Zeit> §7- Setzt die Sekunden des Timers");
        player.sendMessage("§9/timer reset §7- Setzt den Timer zurück");
        player.sendMessage("§9/timer seconds <Sekunden> §7- Setzt die Sekunden des Timers");
        player.sendMessage("§9/timer minutes <Minuten> §7- Setzt die Minuten des Timers");
        player.sendMessage("§9/timer hours <Stunden> §7- Setzt die Stunden des Timers");
        player.sendMessage("§9/timer days <Tage> §7- Setzt die Tage des Timers");
        player.sendMessage("§7Plugin von §dBrentspine");
    }

    private void sendTimeUsage(Player player, String arg1) {
        player.sendMessage("§cVerwendung: /timer " + arg1 + " <Zahl>");
    }


}
