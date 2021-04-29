package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 14.07.2020 16:35
 */
public class InvseeCMD implements CommandExecutor {

    private final Main plugin;

    public InvseeCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("invsee", this);
        plugin.getCommands().put("enderchest", this);
        //plugin.getCommands().put("resethealth", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("invsee")) {
            if (args.length == 1) {
                if (sender.hasPermission("essentialsmini.invsee")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            if(!target.hasPermission(plugin.getPermissionName() + "invsee.owner")) {
                                player.openInventory(target.getInventory());
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/invsee <PlayerName>"));
            }
        }
        if (command.getName().equalsIgnoreCase("enderchest")) {
            if (args.length == 0) {
                if (sender.hasPermission("essentialsmini.enderchest")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        player.openInventory(player.getEnderChest());
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else if (args.length == 1) {
                if (sender instanceof Player) {
                    if (sender.hasPermission("essentialsmini.enderchest.others")) {
                        Player player = (Player) sender;
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            if(!target.hasPermission(plugin.getPermissionName() + "enderchest.owner")) {
                                player.openInventory(target.getEnderChest());
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/ec"));
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/ec <PlayerName>"));
            }
        }
        /*if (command.getName().equalsIgnoreCase("resethealth")) {
            if (sender.hasPermission("essentialsmini.resethealth")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.resetMaxHealth();
                }
            }
        }*/
        return false;
    }
}
