/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.commands.playercommands;

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author DHZoc
 */
public class FlyCMD implements CommandExecutor {

    private final Main plugin;

    public FlyCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("fly", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("essentialsmini.fly")) {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage(plugin.getPrefix() + "§aDu kannst nun Fliegen!");
                    } else {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.sendMessage(plugin.getPrefix() + "§cDu kannst nun nicht mehr Fliegen!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        } else if (args.length == 1) {
            if (sender.hasPermission("essentialsmini.fly")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (!target.getAllowFlight()) {
                        target.setAllowFlight(true);
                        target.setFlying(true);
                        if (!Main.getSilent().contains(sender.getName()))
                            target.sendMessage(plugin.getPrefix() + "§aDu kannst nun Fliegen!");
                        sender.sendMessage(plugin.getPrefix() + "§6" + target.getName() + " §akann nun Fliegen!");
                    } else {
                        target.setAllowFlight(false);
                        target.setFlying(false);
                        if (!Main.getSilent().contains(sender.getName()))
                            target.sendMessage(plugin.getPrefix() + "§cDu kannst nun nicht mehr Fliegen!");
                        sender.sendMessage(plugin.getPrefix() + "§6" + target.getName() + " §ckann nun nicht mehr Fliegen!");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        return false;
    }
}
