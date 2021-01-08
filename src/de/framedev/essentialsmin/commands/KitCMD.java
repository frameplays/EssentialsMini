package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCMD implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public KitCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("kits", this);
        plugin.getTabCompleters().put("kits",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length != 0) {
                String name = args[0];
                if (p.hasPermission(plugin.getPermissionName() + name)) {
                    if (args.length == 1) {
                        if(KitManager.getCustomConfig().contains("Items." + name)) {
                            KitManager kit = new KitManager();
                            kit.loadKits(name, p);
                        } else {
                            p.sendMessage(plugin.getPrefix() + "§cDieses Kit existiert nicht!");
                        }
                    } else {
                        p.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("§6/kits <kitname>"));
                    }
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                p.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("§6/kits <kitname>"));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kits") && args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            ConfigurationSection cs = KitManager.getCustomConfig().getConfigurationSection("Items");
            for (String s : cs.getKeys(false)) {
                if (sender.hasPermission(plugin.getPermissionName() + s)) {
                    list.add(s);
                }
            }
            return list;
        }
        return null;
    }
}
