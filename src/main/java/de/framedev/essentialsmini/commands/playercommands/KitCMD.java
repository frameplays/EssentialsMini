package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.managers.KitManager;
import de.framedev.essentialsmini.utils.Cooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class KitCMD extends CommandBase {

    private final Main plugin;
    public HashMap<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();

    public KitCMD(Main plugin) {
        super(plugin, "kits", "createkit");
        this.plugin = plugin;
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
                                if (kit.getCooldown(name) == 0) {
                                    kit.loadKits(name, p);
                                } else {
                                    if (cooldowns.containsKey(sender.getName())) {
                                        if (!cooldowns.get(sender.getName()).check()) {
                                            long secondsLeft = cooldowns.get(sender.getName()).getSecondsLeft();
                                            long millis = cooldowns.get(sender.getName()).getMilliSeconds();
                                            String format = new SimpleDateFormat("mm:ss").format(new Date(millis));
                                            if (secondsLeft > 0) {
                                                // Still cooling down
                                                sender.sendMessage(getPrefix() + "§cYou cant use that commands for another " + format + "!");
                                                return true;
                                            }
                                        }
                                    }
                                    // No cooldown found or cooldown has expired, save new cooldown
                                    cooldowns.put(sender.getName(), new Cooldown(60 * 5, System.currentTimeMillis()));
                                    p.getInventory().addItem(kit.getKit(name).getContents());
                                }
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
                    } else if (args.length == 2) {
                        ItemStack[] items = p.getInventory().getContents();
                        new KitManager().createKit(args[0], items, Integer.parseInt(args[1]));
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
