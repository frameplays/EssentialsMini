/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.commands.playercommands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.LocationsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author DHZoc
 */
public class SpawnCMD implements CommandExecutor {

    private final Main plugin;

    public SpawnCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("spawn", this);
        plugin.getCommands().put("setspawn", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender.hasPermission("essentialsmini.setspawn")) {
                if (sender instanceof Player) {
                    new LocationsManager("spawn").setLocation(((Player) sender).getLocation());
                    sender.sendMessage(plugin.getPrefix() + "§6Spawn §awurde gesetzt!");
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                try {
                    ((Player) sender).teleport(new LocationsManager("spawn").getLocation());
                    sender.sendMessage(plugin.getPrefix() + "§aDu wurdest zum §6Spawn §aTeleportiert!");
                } catch (IllegalArgumentException ignored) {
                    ((Player) sender).teleport(((Player) sender).getWorld().getSpawnLocation());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        return false;
    }

}
