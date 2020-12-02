package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 26.10.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class PayCMD extends CommandBase {

    private final Main plugin;

    public PayCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("pay", this);
        setup("balance", this);
        setup("eco", this);
        setupTabCompleter("pay", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pay")) {
            if (sender instanceof Player) {
                if (sender.hasPermission(plugin.getPermissionName() + "pay")) {
                    if (args.length == 2) {
                        Player p = (Player) sender;
                        if (isDouble(args[0])) {
                            double amount = Double.parseDouble(args[0]);
                            Player player = Bukkit.getPlayer(args[1]);
                            if (player != null) {
                                if (plugin.getEco().has(p, amount)) {
                                    plugin.getEco().withdrawPlayer(p, amount);
                                    plugin.getEco().depositPlayer(player, amount);
                                    player.sendMessage(plugin.getPrefix() + "§6" + sender.getName() + " §ahat dir §6" + amount + plugin.getCurrencySymbol() + "§a gegeben!");
                                    sender.sendMessage(plugin.getPrefix() + "§aDu hast an §6" + player.getName() + "§a, §6" + amount + plugin.getCurrencySymbol() + " §agegeben!");
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + "§cNicht genug Geld! §aBalance §6: " + plugin.getEco().getBalance((Player) sender) + plugin.getCurrencySymbol());
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§c Dieser Spieler ist nicht Online!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§6" + args[0] + " §cist keine Zahl!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("§6/pay <Amount> <PlayerName>"));
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        if (command.getName().equalsIgnoreCase("balance")) {
            if (args.length == 0) {
                if (sender.hasPermission(plugin.getPermissionName() + "balance")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        player.sendMessage(plugin.getPrefix() + "§aDein Geld : §6" + plugin.getEco().getBalance(player) + plugin.getCurrencySymbol());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else if (args.length == 1) {
                if (sender.hasPermission(plugin.getPermissionName() + "balance.others")) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                    sender.sendMessage(plugin.getPrefix() + "§aDas Geld von §6" + player.getName() + " §abeträgt §6" + plugin.getEco().getBalance(player) + plugin.getCurrencySymbol());
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        }
        if (command.getName().equalsIgnoreCase("eco")) {
            try {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args.length == 2) {
                        if (isDouble(args[1])) {
                            double amount = Double.parseDouble(args[1]);
                            if (sender.hasPermission(plugin.getPermissionName() + "eco.set")) {
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    plugin.getEco().withdrawPlayer(player, plugin.getEco().getBalance(player));
                                    plugin.getEco().depositPlayer(player, amount);
                                    player.sendMessage(plugin.getPrefix() + "§aDein Geld wurde auf §6" + amount + plugin.getCurrencySymbol() + " §agesetzt!");
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§6" + args[1] + " §cist keine Zahl!");
                        }
                    } else if (args.length == 3) {
                        if (sender.hasPermission(plugin.getPermissionName() + "eco.set.others")) {
                            if (isDouble(args[1])) {
                                double amount = Double.parseDouble(args[1]);
                                OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
                                if (player == null) {
                                    sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                                    return true;
                                }
                                plugin.getEco().withdrawPlayer(player, plugin.getEco().getBalance(player));
                                plugin.getEco().depositPlayer(player, amount);
                                sender.sendMessage(plugin.getPrefix() + "§aDas Geld von §6" + args[2] + " §awurde auf §6" + amount + plugin.getCurrencySymbol() + " §agesetzt!");
                                if (player.isOnline()) {
                                    Player online = (Player) player;
                                    online.sendMessage(plugin.getPrefix() + "§aDein Geld wurde auf §6" + amount + plugin.getCurrencySymbol() + " §agesetzt!");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§6" + args[1] + " §cist keine Zahl!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return false;
    }

    public boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pay")) {
            if (args.length == 1) {
                ArrayList<String> list = new ArrayList<>();
                if (sender instanceof Player) {
                    if (sender.hasPermission(plugin.getPermissionName() + "pay")) {
                        list.add(String.valueOf(plugin.getEco().getBalance((Player) sender)));
                    }
                    return list;
                }
            }
        }
        return null;
    }
}
