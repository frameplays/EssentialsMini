package de.framedev.essentialsmini.commands.servercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.mysqlapi.api.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MySQLCMD extends CommandBase {

    private final Main plugin;
    private boolean enabled;

    public MySQLCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("mysql", this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("status")) {
            //EssentialsMiniAPI.getInstance().villagerCreate((Player) sender);
            if (sender.hasPermission(plugin.getPermissionName() + "status")) {
                if(plugin.getServer().getPluginManager().getPlugin("MySQLAPI") != null) {
                    enabled = plugin.getServer().getPluginManager().getPlugin("MySQLAPI") != null || MySQL.con != null;
                } else {
                    enabled = false;
                }
                sender.sendMessage(plugin.getPrefix() + "§6Enabled : §a" + isEnabled());
            } else
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
        }
        return super.onCommand(sender, command, label, args);
    }
}
