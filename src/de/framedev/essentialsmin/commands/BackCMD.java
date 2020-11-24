package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 14.07.2020 16:47
 */
public class BackCMD implements Listener, CommandExecutor {

    // This Plugin
    private final Main plugin;

    // death HashMap
    private HashMap<Player, Location> deaths = new HashMap<>();

    public BackCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("back",this);
        plugin.getListeners().add(this);
    }

    //Test
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("back")) {
            if(plugin.getConfig().getBoolean("Back")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (deaths.containsKey(player)) {

                        /*  Player Teleports to the Death Location */
                        player.teleport(deaths.get(player));
                        player.sendMessage(plugin.getPrefix() + "§aDu wurdest zu deinem Todespunkt Teleportiert!");
                        /* Death Point remove */
                        deaths.remove(player);
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cEs ist kein Todespunkt gespeichert!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(plugin.getConfig().getBoolean("Back")) {
            Player player = event.getEntity();
            deaths.put(player, player.getLocation());
            player.sendMessage(plugin.getPrefix() + "§aBitte gib §6/back §aein damit du zu deinem Todespunkt zurückkehren kannst!");
        }
    }

    public HashMap<Player, Location> getDeaths() {
        return deaths;
    }
}
