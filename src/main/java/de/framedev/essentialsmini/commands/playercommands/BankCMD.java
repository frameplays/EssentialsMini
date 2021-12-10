package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 23.11.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class BankCMD extends CommandBase {

    private final Main plugin;

    public BankCMD(Main plugin) {
        super(plugin, "bank");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (sender.hasPermission("essentialsmini.bank.list")) {
                    List<String> banks = plugin.getVaultManager().getBanks();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < banks.size(); ++i) {
                        stringBuilder.append(banks.get(i));
                        if (i < banks.size() - 1) {
                            stringBuilder.append(", ");
                        }
                    }
                    sender.sendMessage(plugin.getPrefix() + "§6<<<===>>>");
                    sender.sendMessage(plugin.getPrefix() + "§a" + stringBuilder.toString());
                    sender.sendMessage(plugin.getPrefix() + "§6<<<===>>>");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                if (!sender.hasPermission(plugin.getPermissionName() + "bank.info")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }
                String name = args[1];
                if (plugin.getVaultManager().getEconomy().getBanks().contains(name)) {
                    OfflinePlayer owner = null;
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        if (plugin.getVaultManager().getEconomy().isBankOwner(name, player).transactionSuccess()) {
                            owner = player;
                        }
                    }
                    sender.sendMessage("BankName : " + name);
                    sender.sendMessage("Balance : " + plugin.getVaultManager().getEconomy().bankBalance(name).balance);
                    if (owner != null)
                        sender.sendMessage("Owner : " + owner.getName());
                    sender.sendMessage("Members : " + plugin.getVaultManager().getBankMembers(name));
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                }
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.create")) {
                        EconomyResponse economyResponse = plugin.getVaultManager().getEconomy().createBank(args[1], player);
                        if (economyResponse.transactionSuccess()) {
                            player.sendMessage(plugin.getPrefix() + "§aBank Successfully Created!");
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cError while Creating Bank! §6" + economyResponse.errorMessage);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            }
            if (args[0].equalsIgnoreCase("balance")) {
                String bankName = args[1];
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.balance")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().isBankOwner(bankName, player).transactionSuccess() || plugin.getVaultManager().getEconomy().isBankMember(bankName, player).transactionSuccess()) {
                                player.sendMessage(plugin.getPrefix() + "§aThe Balance from the Bank is §6" + plugin.getVaultManager().getEconomy().bankBalance(bankName).balance);
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cYou are not a BankMember or the Owner!");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            }
            if (args[0].equalsIgnoreCase("remove")) {
                String bankName = args[1];
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.remove")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().isBankOwner(bankName, player).transactionSuccess()) {
                                if (plugin.getVaultManager().getEconomy().deleteBank(bankName).transactionSuccess()) {
                                    player.sendMessage(plugin.getPrefix() + "§cBank successfully deleted!");
                                } else {
                                    player.sendMessage(plugin.getPrefix() + "§cError while deleting Bank!");
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cYou are not the Owner!");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            }
            if (args[0].equalsIgnoreCase("listmembers")) {
                String bankName = args[1];
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.listmembers")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().isBankOwner(bankName, player).transactionSuccess() || plugin.getVaultManager().getEco().isBankMember(bankName, player).transactionSuccess()) {
                                List<String> bankMembers = new ArrayList<>(plugin.getVaultManager().getBankMembers(bankName));
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < bankMembers.size(); ++i) {
                                    stringBuilder.append(bankMembers.get(i));
                                    if (i < bankMembers.size() - 1) {
                                        stringBuilder.append(", ");
                                    }
                                }
                                player.sendMessage(plugin.getPrefix() + "§6<<<===>>>");
                                player.sendMessage(plugin.getPrefix() + "§a" + stringBuilder.toString());
                                player.sendMessage(plugin.getPrefix() + "§6<<<===>>>");
                            }
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("deposit")) {
                String bankName = args[1];
                double amount = Double.parseDouble(args[2]);
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.deposit")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().has(player, amount)) {
                                plugin.getVaultManager().getEconomy().withdrawPlayer(player, amount);
                                if (plugin.getVaultManager().getEconomy().bankDeposit(bankName, amount).transactionSuccess()) {
                                    player.sendMessage(plugin.getPrefix() + "§6" + amount + " §awas successfully transferred to the bank!");
                                } else {
                                    player.sendMessage(plugin.getPrefix() + "§cError while Deposit to the Bank!");
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cNot enougt Money!");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            } else if (args[0].equalsIgnoreCase("withdraw")) {
                String bankName = args[1];
                double amount = Double.parseDouble(args[2]);
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.withdraw")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().isBankOwner(bankName, player).transactionSuccess() || plugin.getVaultManager().getEconomy().isBankMember(bankName, player).transactionSuccess()) {
                                if (plugin.getVaultManager().getEconomy().bankHas(bankName, amount).transactionSuccess()) {
                                    plugin.getVaultManager().getEconomy().depositPlayer(player, amount);
                                    plugin.getVaultManager().getEconomy().bankWithdraw(bankName, amount);
                                    player.sendMessage(plugin.getPrefix() + "§aYou successfully withdrew §6" + amount + " §afrom the bank!");
                                } else {
                                    player.sendMessage(plugin.getPrefix() + "§cThe Bank has not enought Money!");
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cYou are not a BankMember or the Owner!");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            } else if (args[0].equalsIgnoreCase("addmember")) {
                String bankName = args[1];
                OfflinePlayer offline = Bukkit.getOfflinePlayer(args[2]);
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.addmember")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().isBankOwner(bankName, player).transactionSuccess()) {
                                plugin.getVaultManager().addBankMember(bankName, offline);
                                player.sendMessage(plugin.getPrefix() + "§6" + offline.getName() + " §ais now Successfully a Member of your Bank!");
                                if (offline.isOnline())
                                    ((Player) offline).sendMessage(plugin.getPrefix() + "§aYou are now a Member of §6" + player.getName() + "'s §aBank!");
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cYou are not the Bank Owner!");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            } else if (args[0].equalsIgnoreCase("removemember")) {
                String bankName = args[1];
                OfflinePlayer offline = Bukkit.getOfflinePlayer(args[2]);
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.removemember")) {
                        if (plugin.getVaultManager().getEconomy().getBanks().contains(bankName)) {
                            if (plugin.getVaultManager().getEconomy().isBankOwner(bankName, player).transactionSuccess()) {
                                plugin.getVaultManager().removeBankMember(bankName, offline);
                                player.sendMessage(plugin.getPrefix() + "§6" + offline.getName() + " §ais no longer a member of your Bank!");
                                if (offline.isOnline())
                                    ((Player) offline).sendMessage(plugin.getPrefix() + "§cYou are no longer a Member of §6" + player.getName() + "'s §cBank!");
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cYou are not the Bank Owner!");
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cThis Bank doesn't exist!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cNo Permissions!");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> cmds = new ArrayList<String>(Arrays.asList("remove", "create", "balance", "withdraw", "deposit", "addmember", "removemember", "listmembers", "list", "info"));
            List<String> empty = new ArrayList<>();
            for (String s : cmds) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    empty.add(s);
                }
            }
            Collections.sort(empty);
            return empty;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list")) return new ArrayList<>();
            if (args[0].equalsIgnoreCase("info")) return new ArrayList<>();
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove"))
                return new ArrayList<>(Collections.singletonList("<BANKNAME>"));
            List<String> banksList = new ArrayList<>();
            List<String> empty = new ArrayList<>();
            for (String banks : plugin.getVaultManager().getEconomy().getBanks()) {
                if (plugin.getVaultManager().getEconomy().isBankMember(banks, (OfflinePlayer) sender).transactionSuccess() || plugin.getVaultManager().getEconomy().isBankOwner(banks, (OfflinePlayer) sender).transactionSuccess()) {
                    banksList.add(banks);
                }
            }
            for (String s : banksList) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase()))
                    empty.add(s);
            }
            Collections.sort(empty);
            return empty;
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("listmembers") || args[0].equalsIgnoreCase("info"))
                return new ArrayList<>();
            if (args[0].equalsIgnoreCase("balance")) return new ArrayList<>();
            if (args[0].equalsIgnoreCase("addmember") || args[0].equalsIgnoreCase("removemember")) {
                List<String> players = new ArrayList<>();
                List<String> empty = new ArrayList<>();
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    players.add(offlinePlayer.getName());
                }
                for (String s : players) {
                    if (s.toLowerCase().startsWith(args[2].toLowerCase()))
                        empty.add(s);
                }
                Collections.sort(empty);
                return empty;
            }
            List<String> empty = new ArrayList<>();
            empty.add(plugin.getVaultManager().getEconomy().getBalance((OfflinePlayer) sender) + "");
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }
}
