package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.TextUtils;
import de.framedev.essentialsmini.utils.Variables;
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

    @SuppressWarnings("deprecation")
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
                    String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                    bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                    sender.sendMessage(plugin.getPrefix() + bankNotFound);
                }
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.bank.create")) {
                        EconomyResponse economyResponse = plugin.getVaultManager().getEconomy().createBank(args[1], player);
                        if (economyResponse.transactionSuccess()) {
                            String created = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Created");
                            created = new TextUtils().replaceAndToParagraph(created);
                            player.sendMessage(plugin.getPrefix() + created);
                        } else {
                            String error = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Error");
                            error = new TextUtils().replaceAndToParagraph(error);
                            error = new TextUtils().replaceObject(error, "%Reason%", "Creating Bank!");
                            error = new TextUtils().replaceObject(error, "%Error%", economyResponse.errorMessage);
                            player.sendMessage(plugin.getPrefix() + error);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
                                String balance = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Balance");
                                balance = new TextUtils().replaceAndToParagraph(balance);
                                balance = new TextUtils().replaceObject(balance, "%Balance%", plugin.getVaultManager().getEconomy().bankBalance(bankName).balance + "");
                                player.sendMessage(plugin.getPrefix() + balance);
                            } else {
                                String message = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotOwnerOrMember");
                                message = new TextUtils().replaceAndToParagraph(message);
                                player.sendMessage(plugin.getPrefix() + message);
                            }
                        } else {
                            String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                            bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                            player.sendMessage(plugin.getPrefix() + bankNotFound);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
                                    String deleted = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Deleted");
                                    deleted = new TextUtils().replaceAndToParagraph(deleted);
                                    player.sendMessage(plugin.getPrefix() + deleted);
                                } else {
                                    String error = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Error");
                                    error = new TextUtils().replaceAndToParagraph(error);
                                    error = new TextUtils().replaceObject(error, "%Reason%", "deleting Bank!");
                                    error = new TextUtils().replaceObject(error, "%Error%", "Error : None");
                                    player.sendMessage(plugin.getPrefix() + error);
                                }
                            } else {
                                String message = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotOwner");
                                message = new TextUtils().replaceAndToParagraph(message);
                                player.sendMessage(plugin.getPrefix() + message);
                            }
                        } else {
                            String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                            bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                            player.sendMessage(plugin.getPrefix() + bankNotFound);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
                                    String deposit = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Deposit");
                                    deposit = new TextUtils().replaceAndToParagraph(deposit);
                                    deposit = new TextUtils().replaceObject(deposit, "%Amount%", amount + "");
                                    player.sendMessage(plugin.getPrefix() + deposit);
                                } else {
                                    String error = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Error");
                                    error = new TextUtils().replaceAndToParagraph(error);
                                    error = new TextUtils().replaceObject(error, "%Reason%", "Deposit to the Bank!");
                                    error = new TextUtils().replaceObject(error, "%Error%", "Error : None");
                                    player.sendMessage(plugin.getPrefix() + error);
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cNot enougt Money!");
                            }
                        } else {
                            String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                            bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                            player.sendMessage(plugin.getPrefix() + bankNotFound);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
                                    String withdraw = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".Withdraw");
                                    withdraw = new TextUtils().replaceAndToParagraph(withdraw);
                                    withdraw = new TextUtils().replaceObject(withdraw, "%Amount%", amount + "");
                                    player.sendMessage(plugin.getPrefix() + withdraw);
                                } else {
                                    player.sendMessage(plugin.getPrefix() + "§cThe Bank has not enought Money!");
                                }
                            } else {
                                String message = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotOwnerOrMember");
                                message = new TextUtils().replaceAndToParagraph(message);
                                player.sendMessage(plugin.getPrefix() + message);
                            }
                        } else {
                            String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                            bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                            player.sendMessage(plugin.getPrefix() + bankNotFound);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
                                String message = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotOwner");
                                message = new TextUtils().replaceAndToParagraph(message);
                                player.sendMessage(plugin.getPrefix() + message);
                            }
                        } else {
                            String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                            bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                            player.sendMessage(plugin.getPrefix() + bankNotFound);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
                                String message = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotOwner");
                                message = new TextUtils().replaceAndToParagraph(message);
                                player.sendMessage(plugin.getPrefix() + message);
                            }
                        } else {
                            String bankNotFound = plugin.getCustomMessagesConfig().getString(Variables.BANK + ".NotFound");
                            bankNotFound = new TextUtils().replaceAndToParagraph(bankNotFound);
                            player.sendMessage(plugin.getPrefix() + bankNotFound);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
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
            if (args[0].equalsIgnoreCase("deposit")) {
                empty.add(plugin.getVaultManager().getEconomy().getBalance((OfflinePlayer) sender) + "");
            } else if (args[0].equalsIgnoreCase("withdraw")) {
                empty.add(String.valueOf(plugin.getVaultManager().getEconomy().bankBalance(args[1]).balance));
            }
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }
}
