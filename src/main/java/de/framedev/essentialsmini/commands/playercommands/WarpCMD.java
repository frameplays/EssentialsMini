package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.managers.LocationsManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 15.07.2020 19:28
 */
public class WarpCMD extends CommandBase {

    private final Main plugin;

    public WarpCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("setwarp", this);
        setup("warp", this);
        setup("warps", this);
        setupTabCompleter("warp", this);
        setup("delwarp", this);
    }

    private final LocationsManager locationsManager = new LocationsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (sender.hasPermission("essentialsmini.setwarp")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length == 1) {
                        String name = args[0];
                        new LocationsManager().setLocation("warps." + name.toLowerCase(), player.getLocation());
                        String message = plugin.getCustomMessagesConfig().getString("Warp.Created");
                        if (message.contains("&"))
                            message = message.replace('&', '§');
                        if (message.contains("WarpName"))
                            message = message.replace("%WarpName%", name);
                        player.sendMessage(plugin.getPrefix() + message);
                    } else if (args.length == 2) {
                        String name = args[0];
                        double cost = Double.parseDouble(args[1]);
                        new LocationsManager().setWarp(name.toLowerCase(), player.getLocation(), cost);
                        String message = plugin.getCustomMessagesConfig().getString("Warp.Created");
                        if (message.contains("&"))
                            message = message.replace('&', '§');
                        if (message.contains("WarpName"))
                            message = message.replace("%WarpName%", name);
                        player.sendMessage(plugin.getPrefix() + message);
                        player.sendMessage(plugin.getPrefix() + "§aThis Warp cost now §6" + cost + "§a" + plugin.getCurrencySymbol());
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/setwarp <Name> §cor §6/setwarp <Name> <Cost>"));
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        if (command.getName().equalsIgnoreCase("warp")) {
            if (sender.hasPermission("essentialsmini.warp")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length == 1) {
                        String name = args[0];
                        try {
                            if (new LocationsManager().costWarp(name))
                                if (plugin.getVaultManager() != null)
                                    if (Main.getInstance().getVaultManager().getEco().has(player, new LocationsManager().getWarpCost(name))) {
                                        Main.getInstance().getVaultManager().getEco().withdrawPlayer(player, new LocationsManager().getWarpCost(name));
                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + "§cNot enought §6" + plugin.getVaultManager().getEconomy().currencyNamePlural());
                                        return true;
                                    }
                            player.teleport(new LocationsManager().getLocation("warps." + name.toLowerCase()));
                            String message = plugin.getCustomMessagesConfig().getString("Warp.Teleport");
                            if (message.contains("&"))
                                message = message.replace('&', '§');
                            if (message.contains("%WarpName%"))
                                message = message.replace("%WarpName%", name);
                            player.sendMessage(plugin.getPrefix() + message);
                        } catch (Exception ex) {
                            String message = plugin.getCustomMessagesConfig().getString("Warp.NotExist");
                            if (message.contains("&"))
                                message = message.replace('&', '§');
                            player.sendMessage(plugin.getPrefix() + message);
                        }
                    } else {
                        if (sender.hasPermission("essentialsmini.warps")) {
                            sender.sendMessage(plugin.getPrefix() + "§a==Alle Aktuellen Warps==");
                            ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("warps");
                            if (new LocationsManager().getCfg().contains("warps")) {
                                if (cs != null) {
                                    for (String s : cs.getKeys(false)) {
                                        if (s != null) {
                                            if (!new LocationsManager().getCfg().get("warps." + s).equals(" "))
                                                sender.sendMessage(s);
                                        }
                                    }
                                }
                            } else {
                                String message = plugin.getCustomMessagesConfig().getString("Warp.NotExist");
                                if (message.contains("&"))
                                    message = message.replace('&', '§');
                                sender.sendMessage(plugin.getPrefix() + message);
                            }
                        } else {
                            player.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/warp <Name>"));
                        }
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        if (command.getName().equalsIgnoreCase("warps")) {
            if (sender.hasPermission("essentialsmini.warps")) {
                sender.sendMessage(plugin.getPrefix() + "§a==Alle Aktuellen Warps==");
                if (!plugin.getVariables().isJsonFormat()) {
                    ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("warps");
                    if (new LocationsManager().getCfg().contains("warps")) {
                        if (cs != null) {
                            for (String s : cs.getKeys(false)) {
                                if (s != null) {
                                    if (!new LocationsManager().getCfg().get("warps." + s).equals(" ")) {
                                        TextComponent textComponent = new TextComponent(s);
                                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click me to add Warp Command").create()));
                                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/warp " + s));
                                        sender.spigot().sendMessage(textComponent);
                                    }
                                }
                            }
                        }
                    } else {
                        String message = plugin.getCustomMessagesConfig().getString("Warp.NotExist");
                        if (message.contains("&"))
                            message = message.replace('&', '§');
                        sender.sendMessage(plugin.getPrefix() + message);
                    }
                } else {
                    if (new LocationsManager().getLocations() != null) {
                        ArrayList<String> warps = new ArrayList<>(new LocationsManager().getLocations().keySet());
                        for (String s : warps) {
                            if (s != null) {
                                if (s.contains("warps.")) {
                                    s = s.replace("warps.", "");
                                    sender.sendMessage(s);
                                }
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        if (command.getName().equalsIgnoreCase("delwarp")) {
            if (sender.hasPermission("essentialsmini.delwarp")) {
                if (args.length == 1) {
                    String warp = args[0].toLowerCase();
                    new LocationsManager().removeLocation("warps." + warp);
                    String message = plugin.getCustomMessagesConfig().getString("Warp.Delete");
                    if (message.contains("&"))
                        message = message.replace('&', '§');
                    if (message.contains("%WarpName%"))
                        message = message.replace("%WarpName%", warp);
                    sender.sendMessage(plugin.getPrefix() + message);
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("warp")) {
            if (args.length == 1) {
                if (sender.hasPermission("essentialsmini.warp")) {
                    ArrayList<String> empty = new ArrayList<>();
                    ArrayList<String> warps = new ArrayList<>();
                    if (!plugin.getVariables().isJsonFormat()) {
                        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("warps");
                        if (cs != null) {
                            for (String s : cs.getKeys(false)) {
                                if (s != null) {
                                    if (!new LocationsManager().getCfg().get("warps." + s).equals(" "))
                                        warps.add(s);
                                }
                            }
                        }
                        for (String s : warps) {
                            if (s.toLowerCase().startsWith(args[0])) {
                                empty.add(s);
                            }
                        }
                    } else {
                        warps.addAll(new LocationsManager().getLocations().keySet());
                        for (String s : warps) {
                            if (s.contains("warps.")) {
                                s = s.replace("warps.", "");
                                if (s.toLowerCase().startsWith(args[0])) {
                                    empty.add(s);
                                }
                            }
                        }
                    }
                    Collections.sort(empty);
                    return empty;
                }
            }
        }
        return null;
    }
}
