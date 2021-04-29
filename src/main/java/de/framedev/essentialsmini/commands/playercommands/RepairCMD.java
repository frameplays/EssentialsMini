package de.framedev.essentialsmini.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 18.08.2020 19:02
 */

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import static org.bukkit.Material.AIR;

public class RepairCMD implements CommandExecutor {

    private final Main plugin;

    public RepairCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("repair", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("repair")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission(new Permission("essentialsmini.repair", PermissionDefault.OP))) {
                        if (player.getInventory().getItemInMainHand().getType() != AIR) {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            if (item.hasItemMeta()) {
                                if (item.getItemMeta() instanceof Damageable) {
                                    Damageable damageable = (Damageable) item.getItemMeta();
                                    if (damageable.hasDamage()) {
                                        damageable.setDamage(0);
                                        item.setItemMeta((ItemMeta) damageable);
                                        player.sendMessage(plugin.getPrefix() + "§aDas Item §6: " + item.getType().name() + " §awurde repariert!");
                                    } else {
                                        player.sendMessage(plugin.getPrefix() + "§cDas Item §6: " + item.getType().name() + " §cmuss nicht Repariert werden!");
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cLuft kann nicht Repariert werden!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player player = Bukkit.getPlayer(args[0]);
                    if (sender.hasPermission(new Permission("essentialsmini.repair.others", PermissionDefault.OP))) {
                        if (player.getInventory().getItemInMainHand().getType() != AIR) {
                            ItemStack item = player.getInventory().getItemInMainHand();
                            if (item.hasItemMeta()) {
                                if (item.getItemMeta() instanceof Damageable) {
                                    Damageable damageable = (Damageable) item.getItemMeta();
                                    if (damageable.hasDamage()) {
                                        damageable.setDamage(0);
                                        item.setItemMeta((ItemMeta) damageable);
                                        if (!Main.getSilent().contains(sender.getName()))
                                            player.sendMessage(plugin.getPrefix() + "§aDas Item §6: " + item.getType().name() + " §awurde repariert!");
                                        sender.sendMessage(plugin.getPrefix() + "§aDas Item §6: " + item.getType().name() + " §avon §6: " + player.getName() + " §awurde repariert!");
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + "§cDas Item §6: " + item.getType().name() + " §cvon §6: " + player.getName() + " §cmuss nicht Repariert werden!");
                                    }
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + "§cDas Item §6: " + item.getType().name() + " §cvon §6: " + player.getName() + "§c kann nicht Repariert werden!");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§cDas Item §6: " + item.getType().name() + " §cvon §6: " + player.getName() + "§c kann nicht Repariert werden!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§cDas Item §6: " + AIR.name() + " §cvon §6: " + player.getName() + "§c kann nicht Repariert werden!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/repair §coder §6/repair <PlayerName>"));
            }
        }
        return false;
    }
}
