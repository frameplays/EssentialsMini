package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import de.framedev.essentialsmin.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
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
                            if (args[1].equalsIgnoreCase("**")) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player != null) {
                                        if (plugin.getVaultManager().getEco().has(p, amount)) {
                                            plugin.getVaultManager().getEco().withdrawPlayer(p, amount);
                                            plugin.getVaultManager().getEco().depositPlayer(player, amount);
                                            String send = plugin.getCustomMessagesConfig().getString("Money.MSG.Pay");
                                            send = send.replace('&', '§');
                                            send = send.replace("[Target]", player.getName());
                                            send = send.replace("[Money]", String.valueOf(amount) + plugin.getCurrencySymbol());
                                            String got = plugin.getCustomMessagesConfig().getString("Money.MSG.GotPay");
                                            if (got != null) {
                                                got = new TextUtils().replaceAndToParagraph(got);
                                                got = new TextUtils().replaceObject(got, "[Player]", sender.getName());
                                                got = new TextUtils().replaceObject(got, "[Money]", amount + plugin.getCurrencySymbol());
                                            }
                                            player.sendMessage(plugin.getPrefix() + got);
                                            sender.sendMessage(plugin.getPrefix() + send);
                                        } else {
                                            sender.sendMessage(plugin.getPrefix() + "§cNicht genug Geld! §aBalance §6: " + plugin.getVaultManager().getEco().getBalance((Player) sender) + plugin.getCurrencySymbol());
                                        }
                                    }
                                }
                            } else {
                                Player player = Bukkit.getPlayer(args[1]);
                                if (player != null) {
                                    if (plugin.getVaultManager().getEco().has(p, amount)) {
                                        plugin.getVaultManager().getEco().withdrawPlayer(p, amount);
                                        plugin.getVaultManager().getEco().depositPlayer(player, amount);
                                        String send = plugin.getCustomMessagesConfig().getString("Money.MSG.Pay");
                                        send = send.replace('&', '§');
                                        send = send.replace("[Target]", player.getName());
                                        send = send.replace("[Money]", String.valueOf(amount) + plugin.getCurrencySymbol());
                                        String got = plugin.getCustomMessagesConfig().getString("Money.MSG.GotPay");
                                        if (got != null) {
                                            got = new TextUtils().replaceAndToParagraph(got);
                                            got = new TextUtils().replaceObject(got, "[Player]", sender.getName());
                                            got = new TextUtils().replaceObject(got, "[Money]", amount + plugin.getCurrencySymbol());
                                        }
                                        player.sendMessage(plugin.getPrefix() + got);
                                        sender.sendMessage(plugin.getPrefix() + send);
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + "§cNicht genug Geld! §aBalance §6: " + plugin.getVaultManager().getEco().getBalance((Player) sender) + plugin.getCurrencySymbol());
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + "§c Dieser Spieler ist nicht Online!");
                                }
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
                        String balance = plugin.getCustomMessagesConfig().getString("Money.MSG.Balance");
                        balance = new TextUtils().replaceAndToParagraph(balance);
                        balance = new TextUtils().replaceObject(balance, "[Money]", plugin.getVaultManager().getEco().getBalance(player) + plugin.getCurrencySymbol());
                        player.sendMessage(plugin.getPrefix() + balance);
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else if (args.length == 1) {
                if (sender.hasPermission(plugin.getPermissionName() + "balance.others")) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                    String balance = plugin.getCustomMessagesConfig().getString("Money.MoneyBalance.Other.MSG");
                    balance = new TextUtils().replaceAndToParagraph(balance);
                    balance = new TextUtils().replaceObject(balance, "[Target]", player.getName());
                    balance = new TextUtils().replaceObject(balance, "[Money]", plugin.getVaultManager().getEco().getBalance(player) + plugin.getCurrencySymbol());
                    sender.sendMessage(plugin.getPrefix() + balance);
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
                                    plugin.getVaultManager().getEco().withdrawPlayer(player, plugin.getVaultManager().getEco().getBalance(player));
                                    plugin.getVaultManager().getEco().depositPlayer(player, amount);
                                    String set = plugin.getCustomMessagesConfig().getString("Money.MSG.Set");
                                    if (set != null) {
                                        set = new TextUtils().replaceAndToParagraph(set);
                                        set = new TextUtils().replaceObject(set, "[Money]", amount + plugin.getCurrencySymbol());
                                    }
                                    player.sendMessage(plugin.getPrefix() + set);
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
                                plugin.getVaultManager().getEco().withdrawPlayer(player, plugin.getVaultManager().getEco().getBalance(player));
                                plugin.getVaultManager().getEco().depositPlayer(player, amount);
                                String setOther = plugin.getCustomMessagesConfig().getString("MoneySet.Other.MSG");
                                if (setOther != null) {
                                    setOther = new TextUtils().replaceAndToParagraph(setOther);
                                    setOther = new TextUtils().replaceObject(setOther, "[Target]", player.getName());
                                    setOther = new TextUtils().replaceObject(setOther, "[Money]", amount + plugin.getCurrencySymbol());
                                }
                                String set = plugin.getCustomMessagesConfig().getString("Money.MSG.Set");
                                if (set != null) {
                                    set = new TextUtils().replaceAndToParagraph(set);
                                    set = new TextUtils().replaceObject(set, "[Money]", amount + plugin.getCurrencySymbol());
                                }
                                sender.sendMessage(plugin.getPrefix() + setOther);
                                if (player.isOnline()) {
                                    Player online = (Player) player;
                                    online.sendMessage(plugin.getPrefix() + set);
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
                        list.add(String.valueOf(plugin.getVaultManager().getEco().getBalance((Player) sender)));
                    }
                    return list;
                }
            } else if (args.length == 2) {
                if (sender.hasPermission(plugin.getPermissionName() + "pay")) {
                    ArrayList<String> players = new ArrayList<>();
                    ArrayList<String> empty = new ArrayList<>();
                    players.add("**");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    for (String s : players) {
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                            empty.add(s);
                        }
                    }
                    Collections.sort(empty);
                    return empty;
                }
            }
        }
        return null;
    }
}
