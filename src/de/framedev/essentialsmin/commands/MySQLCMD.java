package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import de.framedev.mysqlapi.api.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MySQLCMD extends CommandBase {

    private final Main plugin;
    private boolean enabled;

    public MySQLCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("mysql",this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0].equalsIgnoreCase("status")) {
            if(sender.hasPermission(plugin.getPermissionName() + "status")) {
                sender.sendMessage(plugin.getPrefix() + "§6" + Bukkit.getPluginManager().isPluginEnabled("MySQLAPI"));
                enabled = MySQL.con != null;
                sender.sendMessage(plugin.getPrefix() + "§6Enabled : §a" + isEnabled());
            } else
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
        }
        return super.onCommand(sender, command, label, args);
    }
}
