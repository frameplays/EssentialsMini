package de.framedev.essentialsmin.commands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 14.08.2020 20:52
 */

import de.framedev.essentialsmin.main.Main;
import net.minecraft.server.v1_16_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SunRainThunderCMD implements CommandExecutor {

    private final Main plugin;

    public SunRainThunderCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("sun",this);
        plugin.getCommands().put("rain",this);
        plugin.getCommands().put("thunder",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("sun")) {
                if(player.hasPermission(plugin.getPermissionName() + "sun")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.getWorld().setStorm(false);
                            player.getWorld().setThundering(false);
                        }
                    }.runTaskLater(plugin,60);
                    player.sendMessage(plugin.getPrefix() + "§aIn der Welt §6" + player.getWorld().getName() + " §ascheint nun die Sonne!");
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
            if (command.getName().equalsIgnoreCase("rain")) {
                if(player.hasPermission(plugin.getPermissionName() + "rain")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.getWorld().setStorm(true);
                        }
                    }.runTaskLater(plugin,60);
                    player.sendMessage(plugin.getPrefix() + "§aIn der Welt §6" + player.getWorld().getName() + " §aregnet es nun!");
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
            if (command.getName().equalsIgnoreCase("thunder")) {
                if(player.hasPermission(plugin.getPermissionName() + "thunder")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.getWorld().setStorm(true);
                            player.getWorld().setThundering(true);
                        }
                    }.runTaskLater(plugin,60);
                    player.sendMessage(plugin.getPrefix() + "§aIn der Welt §6" + player.getWorld().getName() + " §aist nun ein gewitter!");
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }
}
