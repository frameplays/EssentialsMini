package de.framedev.essentialsmin.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 11.08.2020 23:04
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.MaterialManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemCMD implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public ItemCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("item", this);
        plugin.getTabCompleters().put("item", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(new Permission("essentialsmini.item", PermissionDefault.OP))) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission(new Permission("essentialsmini.item", PermissionDefault.OP))) {
                        String name = args[0];
                        if (new MaterialManager().existsMaterial(Material.getMaterial(name.toUpperCase()))) {
                            player.getInventory().addItem(new ItemStack(new MaterialManager().getMaterial(name.toUpperCase())));
                            player.sendMessage(plugin.getPrefix() + "§aDu hast das Item §6" + name + " §abekommen! Menge §6" + 1);
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cDieses Item existiert nicht! §6" + name);
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else if (args.length == 2) {
                try {
                    if (args[1].equalsIgnoreCase(String.valueOf(Integer.parseInt(args[1])))) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            if (player.hasPermission(new Permission("essentialsmini.item", PermissionDefault.OP))) {
                                String name = args[0];
                                if (new MaterialManager().existsMaterial(Material.getMaterial(name.toUpperCase()))) {
                                    int amount = Integer.parseInt(args[1]);
                                    player.getInventory().addItem(new ItemStack(new MaterialManager().getMaterial(name.toUpperCase()), amount));
                                    if (!Main.getSilent().contains(sender.getName()))
                                        player.sendMessage(plugin.getPrefix() + "§aDu hast das Item §6" + name + " §abekommen! Menge §6" + amount);
                                } else {
                                    player.sendMessage(plugin.getPrefix() + "§cDieses Item existiert nicht! §6" + name);
                                }
                            } else {
                                player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                        }
                    }
                } catch (NumberFormatException ignored) {
                    if (sender.hasPermission(new Permission("essentialsmini.item", PermissionDefault.OP))) {
                        String name = args[0];
                        if (new MaterialManager().existsMaterial(Material.getMaterial(name.toUpperCase()))) {
                            Player player1 = Bukkit.getPlayer(args[1]);
                            if (player1 != null) {
                                player1.getInventory().addItem(new ItemStack(new MaterialManager().getMaterial(name.toUpperCase())));
                                sender.sendMessage(plugin.getPrefix() + "§6" + player1.getName() + " §ahat das Item §6" + name + " §abekommen!");
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht! §6" + args[1]);
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§cDieses Item existiert nicht! §6" + name);
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                }
            } else if (args.length == 3) {
                if (sender.hasPermission(new Permission("essentialsmini.item", PermissionDefault.OP))) {
                    String name = args[0];
                    if (new MaterialManager().existsMaterial(Material.getMaterial(name.toUpperCase()))) {
                        int amount = Integer.parseInt(args[1]);
                        Player player1 = Bukkit.getPlayer(args[2]);
                        if (player1 != null) {
                            player1.getInventory().addItem(new ItemStack(new MaterialManager().getMaterial(name.toUpperCase()), amount));
                            sender.sendMessage(plugin.getPrefix() + "§6" + player1.getName() + " §ahat das Item §6" + name + " §abekommen! Menge §6" + amount);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNotOnline(args[2]));
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cDieses Item existiert nicht! §6" + name);
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/item <Item>"));
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/item <Item> <SpielerName>"));
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/item <Item> <Anzahl>"));
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/item <Item> <Anzahl> <SpielerName>"));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("essentialsmini.item")) {
                ArrayList<String> empty = new ArrayList<>();
                ArrayList<Material> materials = new MaterialManager().getMaterials();
                for (Material material : materials) {
                    if (material.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                        empty.add(material.name());
                    }
                }
                Collections.sort(empty);
                return empty;
            }
        }
        return null;
    }
}
