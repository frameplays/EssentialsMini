package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 28.01.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class EconomyCMD extends CommandBase {

    private final Main plugin;

    public EconomyCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("infoeconomy",this);
        setupTabCompleter("infoeconomy",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("account")) {
                if(!sender.hasPermission(plugin.getPermissionName() + "infoeconomy")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }
                if(!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                    return true;
                }
                if (plugin.getVaultManager() != null) {
                    boolean account = plugin.getVaultManager().getEco().hasAccount((Player) sender);
                    sender.sendMessage(plugin.getPrefix() + "§aAccount : §6" + account);
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cEconomy ist nicht Aktiviert!");
                }
                return true;
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("account")) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if(!sender.hasPermission(plugin.getPermissionName() + "infoeconomy")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }
                if (plugin.getVaultManager() != null) {
                    boolean account = plugin.getVaultManager().getEco().hasAccount(player);
                    sender.sendMessage(plugin.getPrefix() + "§aAccount : §6" + account);
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cEconomy ist nicht Aktiviert!");
                }
                return true;
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            ArrayList<String> cmds = new ArrayList<>();
            cmds.add("account");

            ArrayList<String> empty = new ArrayList<>();
            for(String s : cmds) {
                if(s.toLowerCase().startsWith(args[0]))
                    empty.add(s);
            }

            Collections.sort(empty);
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }
}
