package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitCMD implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public KitCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("kits", this);
        plugin.getTabCompleters().put("kits", this);
        plugin.getCommands().put("createkit", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (command.getName().equalsIgnoreCase("kits")) {
                if (args.length != 0) {
                    String name = args[0];
                    if (p.hasPermission(plugin.getPermissionName() + "kits." + name)) {
                        if (args.length == 1) {
                            if (KitManager.getCustomConfig().contains("Items." + name)) {
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
            if (command.getName().equalsIgnoreCase("createkit")) {
                if (p.hasPermission(plugin.getPermissionName() + "createkit")) {
                    if (args.length == 1) {
                        ItemStack[] items = p.getInventory().getContents();
                        new KitManager().createKit(args[0], items);
                        p.sendMessage(plugin.getPrefix() + "§aKit Created §6" + args[0]);
                        p.getInventory().clear();
                    } else {
                        p.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("§6/createkit <KitName>"));
                    }
                } else {
                    p.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kits") && args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            ConfigurationSection cs = KitManager.getCustomConfig().getConfigurationSection("Items");
            for (String s : cs.getKeys(false)) {
                if (sender.hasPermission(plugin.getPermissionName() + "kits." + s)) {
                    list.add(s);
                }
            }
            return list;
        }
        if (command.getName().equalsIgnoreCase("createkit") && args.length == 1) {
            return Collections.singletonList("Kit_Name");
        }
        return null;
    }
}
