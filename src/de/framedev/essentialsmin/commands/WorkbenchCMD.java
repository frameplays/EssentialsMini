package de.framedev.essentialsmin.commands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 13.08.2020 20:07
 */

import de.framedev.essentialsmin.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorkbenchCMD implements CommandExecutor {

    private final Main plugin;

    public WorkbenchCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("workbench",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0) {
                if (sender.hasPermission(plugin.getPermissionName() + "workbench")) {
                    ((Player) sender).openWorkbench(((Player) sender).getLocation(), true);
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/workbench"));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }
}
