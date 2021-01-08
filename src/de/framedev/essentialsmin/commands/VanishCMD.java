package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 15.07.2020 11:59
 */
public class VanishCMD implements CommandExecutor, Listener {

    private final Main plugin;
    public static ArrayList<String> hided = new ArrayList<>();

    public VanishCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("vanish", this);
        plugin.getListeners().add(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vanish")) {
            if (sender.hasPermission("essentialsmini.vanish")) {
                if (args.length == 0) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (hided.contains(player.getName())) {
                            Bukkit.getOnlinePlayers().forEach(o -> {
                                o.showPlayer(this.plugin, player);
                            });
                            hided.remove(player.getName());
                            player.sendMessage(plugin.getPrefix() + "§cDu bist nun nicht mehr im Vanish!");
                            return true;
                        } else {
                            Bukkit.getOnlinePlayers().forEach(o -> {
                                if (!o.hasPermission("essentialsmini.vanish.see")) {
                                    o.hidePlayer(this.plugin, player);
                                }
                            });
                            hided.add(player.getName());
                            player.sendMessage(plugin.getPrefix() + "§aDu bist nun im Vanish!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } else if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (hided.contains(target.getName())) {
                            Bukkit.getOnlinePlayers().forEach(o -> {
                                o.showPlayer(this.plugin, target);
                            });
                            hided.remove(target.getName());
                            target.sendMessage(plugin.getPrefix() + "§cDu bist nun nicht mehr im Vanish!");
                            sender.sendMessage(plugin.getPrefix() + "§6" + target.getName() + " §chat kein Vanish mehr!");
                            return true;

                        } else {
                            Bukkit.getOnlinePlayers().forEach(o -> {
                                if (!o.hasPermission("essentialsmini.vanish.see")) {
                                    o.hidePlayer(this.plugin, target);
                                }
                            });
                            hided.add(target.getName());
                            target.sendMessage(plugin.getPrefix() + "§aDu bist nun im Vanish!");
                            sender.sendMessage(plugin.getPrefix() + "§6" + target.getName() + " §ahat nun Vanish!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler ist nicht Online!");
                    }
                    return true;
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/vanish §coder §6/vanish <PlayerName>"));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (String vanish : hided) {
            if (!event.getPlayer().hasPermission("essentialsmini.vanish.see")) {
                event.getPlayer().hidePlayer(plugin, Bukkit.getPlayer(vanish));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (hided.contains(event.getPlayer().getName())) hided.remove(event.getPlayer().getName());
    }
}
