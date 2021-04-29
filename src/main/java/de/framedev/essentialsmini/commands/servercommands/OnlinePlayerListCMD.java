package de.framedev.essentialsmini.commands.servercommands;

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 17.07.2020 22:52
 */
public class OnlinePlayerListCMD implements CommandExecutor {

    private final Main plugin;

    private final ArrayList<String> players = new ArrayList<>();

    public OnlinePlayerListCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("online",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("online")) {
            if(sender.hasPermission("essentialsmini.online")) {
                /* Online Spieler */
                for(Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName());
                }
                sender.sendMessage("§6==§cPlayers§6==");
                sender.sendMessage(Arrays.toString(players.toArray()));
                players.clear();
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        return false;
    }
}
