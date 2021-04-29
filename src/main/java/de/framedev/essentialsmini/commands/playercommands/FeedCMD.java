package de.framedev.essentialsmini.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 18.08.2020 19:36
 */

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCMD implements CommandExecutor {

    private final Main plugin;

    public FeedCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("feed", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                if (sender.hasPermission("essentialsmini.feed")) {
                    Player player = (Player) sender;
                    player.setFoodLevel(20);
                    String feedSet = plugin.getCustomMessagesConfig().getString("FeedSet");
                    if(feedSet.contains("&"))
                        feedSet = feedSet.replace('&', '§');
                    player.sendMessage(plugin.getPrefix() + feedSet);
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        } else if (args.length == 1) {
            if (sender.hasPermission("essentialsmini.feed.others")) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    player.setFoodLevel(20);
                    if (!Main.getSilent().contains(sender.getName())) {
                        String feedSet = plugin.getCustomMessagesConfig().getString("FeedSet");
                        if (feedSet.contains("&"))
                            feedSet = feedSet.replace('&', '§');
                        player.sendMessage(plugin.getPrefix() + feedSet);
                    }
                    String feedOther = plugin.getCustomMessagesConfig().getString("FeedOtherSet");
                    if(feedOther.contains("&"))
                        feedOther = feedOther.replace('&', '§');
                    if(feedOther.contains("%Player%"))
                        feedOther = feedOther.replace("%Player%", player.getName());
                    sender.sendMessage(plugin.getPrefix() + feedOther);
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/feed §cor §6/feed <PlayerName>"));
        }
        return false;
    }
}
