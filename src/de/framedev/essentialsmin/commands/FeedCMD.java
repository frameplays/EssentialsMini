package de.framedev.essentialsmin.commands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 18.08.2020 19:36
 */

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCMD implements CommandExecutor {

    private final Main plugin;

    public FeedCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("feed",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                if(sender.hasPermission("essentialsmini.feed")) {
                    Player player = (Player) sender;
                    player.setFoodLevel(20);
                    player.sendMessage(plugin.getPrefix() + "§aDein Hunger wurde gestillt!");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        } else if(args.length == 1) {
            if(sender.hasPermission("essentialsmini.feed.others")) {
                Player player = Bukkit.getPlayer(args[0]);
                if(player != null) {
                    player.setFoodLevel(20);
                    player.sendMessage(plugin.getPrefix() + "§aDein Hunger wurde gestillt!");
                    sender.sendMessage(plugin.getPrefix() + "§6" + player.getName() + "'s §aHunger wurde gestillt!");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNotOnline(args[0]));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/feed §coder §6/feed <PlayerName>"));
        }
        return false;
    }
}
