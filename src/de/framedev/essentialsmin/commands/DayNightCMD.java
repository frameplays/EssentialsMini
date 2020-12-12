package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.soap.Text;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 25.07.2020 15:30
 */
public class DayNightCMD implements CommandExecutor {

    private final Main plugin;

    public DayNightCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("day", this);
        plugin.getCommands().put("night", this);
        plugin.getCommands().put("pltime",this);
        plugin.getCommands().put("resetpltime",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("day")) {
                if (args.length == 0) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.day")) {
                        player.getWorld().setTime(1000);
                        String message = plugin.getCustomMessagesConfig().getString("Day");
                        if (message != null) {
                            message = new TextUtils().replaceAndToParagraph(message);
                        }
                        player.sendMessage(plugin.getPrefix() + message);
                        return true;
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                        return true;
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/day"));
                    return true;
                }
            }
            if (command.getName().equalsIgnoreCase("night")) {
                if (args.length == 0) {
                    Player player = (Player) sender;
                    if (player.hasPermission("essentialsmini.night")) {
                        String message = plugin.getCustomMessagesConfig().getString("Night");
                        if (message != null) {
                            message = new TextUtils().replaceAndToParagraph(message);
                        }
                        player.sendMessage(plugin.getPrefix() + message);
                        player.getWorld().setTime(13000);
                        return true;
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                        return true;
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/night"));
                    return true;
                }
            }
            if(command.getName().equalsIgnoreCase("pltime")) {
                Player player = (Player) sender;
                if (player.hasPermission("essentialsmini.playertimer")) {
                    try {
                        player.setPlayerTime(Integer.parseInt(args[0]),false);
                    } catch (Exception ex) {
                        switch (args[0]) {
                            case "day":
                                player.setPlayerTime(0,false);
                                break;
                            case "night":
                                player.setPlayerTime(13000,false);
                                break;
                            default:
                                player.setPlayerTime(1,false);
                        }
                    }
                }
            }
            if(command.getName().equalsIgnoreCase("resetpltime")) {
                Player player = (Player) sender;
                if (player.hasPermission("essentialsmini.playertimer")) {
                    player.resetPlayerTime();
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            return true;
        }
        return false;
    }
}
