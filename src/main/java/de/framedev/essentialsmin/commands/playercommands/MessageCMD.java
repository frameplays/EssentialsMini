package de.framedev.essentialsmin.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 13.08.2020 19:11
 */

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageCMD implements CommandExecutor {

    private final Main plugin;

    public MessageCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("msg", this);
        plugin.getCommands().put("r", this);
        plugin.getCommands().put("spy", this);
        plugin.getCommands().put("msgtoggle",this);
    }

    HashMap<Player, Player> reply = new HashMap<>();
    ArrayList<Player> spy = new ArrayList<>();
    ArrayList<Player> msgToggle = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("msgtoggle")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission(plugin.getPermissionName() + "msgtoggle")) {
                    if (msgToggle.contains(player)) {
                        msgToggle.remove(player);
                        player.sendMessage(plugin.getPrefix() + "§aMessages will be sent to you!");
                        return true;
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo more messages will be sent to you!");
                        msgToggle.add(player);
                        return true;
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        if (command.getName().equalsIgnoreCase("msg")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String message = "";
                if (player.hasPermission("essentialsmini.msg")) {
                    if (args.length >= 2) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (!msgToggle.contains(target)) {
                            if (target != null) {
                                for (int i = 1; i < args.length; i++) {
                                    message = message + args[i] + " ";
                                }
                                for (Player opPlayer : Bukkit.getOnlinePlayers()) {
                                    if (opPlayer.hasPermission("essentialsmini.spy")) {
                                        if (spy.contains(opPlayer)) {
                                            opPlayer.sendMessage("§6" + player.getName() + " §ahat eine Nachricht an §6" + target.getName() + " §agesendet mit dem Text §6: §c" + message);
                                        }
                                    }
                                }
                                player.sendMessage("§cme §r-> §a " + target.getName() + " §f» " + message);
                                target.sendMessage("§a" + player.getName() + " §r-> §cme  §f» " + message);
                                message = "";
                                reply.put(target, player);
                            }
                        } else if(player.hasPermission(plugin.getPermissionName() + "msgtoggle.bypass")) {
                            if (target != null) {
                                for (int i = 1; i < args.length; i++) {
                                    message = message + args[i] + " ";
                                }
                                for (Player opPlayer : Bukkit.getOnlinePlayers()) {
                                    if (opPlayer.hasPermission("essentialsmini.spy")) {
                                        if (spy.contains(opPlayer)) {
                                            opPlayer.sendMessage("§6" + player.getName() + " §ahat eine Nachricht an §6" + target.getName() + " §agesendet mit dem Text §6: §c" + message);
                                        }
                                    }
                                }
                                player.sendMessage("§cme §r-> §a " + target.getName() + " §f» " + message);
                                target.sendMessage("§a" + player.getName() + " §r-> §cme  §f» " + message);
                                message = "";
                                reply.put(target, player);
                            }
                        } else {
                            if (target != null) {
                                player.sendMessage(plugin.getPrefix() + "§cThis Player doesn't Allow Messages!");
                            }
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/msg <PlayerName> <Message>"));
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        if (command.getName().equalsIgnoreCase("r")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("essentialsmini.msg")) {
                    if (args.length >= 1) {
                        if (!reply.isEmpty()) {
                            if (reply.containsKey(player)) {
                                Player target = reply.get(player);
                                String message = "";
                                if (target != null) {
                                    for (int i = 0; i < args.length; i++) {
                                        message = message + args[i] + " ";
                                    }
                                    for (Player opPlayer : Bukkit.getOnlinePlayers()) {
                                        if (opPlayer.hasPermission("essentialsmini.spy")) {
                                            if (spy.contains(opPlayer)) {
                                                opPlayer.sendMessage("§6" + player.getName() + " §ahat eine Nachricht an §6" + target.getName() + " §agesendet mit dem Text §6: §c" + message);
                                            }
                                        }
                                    }
                                    player.sendMessage("§cme §r-> §a " + target.getName() + " §f» " + message);
                                    target.sendMessage("§a" + player.getName() + " §r-> §cme  §f» " + message);
                                    message = "";
                                    reply.remove(player);
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§aDir wurde vor kurzem keine Nachricht geschrieben!");
                            }
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/r <Message>"));
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        if (command.getName().equalsIgnoreCase("spy")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("essentialsmini.spy")) {
                    if (!spy.contains(player)) {
                        player.sendMessage(plugin.getPrefix() + "§aDir werden nun die Msg's geschickt von anderen Spielern!");
                        spy.add(player);
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§aDir werden nun nicht mehr die Msg's geschickt von anderen Spielern!");
                        spy.remove(player);
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        return false;
    }
}
