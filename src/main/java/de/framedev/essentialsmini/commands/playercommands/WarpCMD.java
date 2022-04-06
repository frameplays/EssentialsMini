package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandListenerBase;
import de.framedev.essentialsmini.managers.InventoryManager;
import de.framedev.essentialsmini.managers.ItemBuilder;
import de.framedev.essentialsmini.managers.LocationsManager;
import de.framedev.essentialsmini.utils.TextUtils;
import de.framedev.essentialsmini.utils.Variables;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

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
public class WarpCMD extends CommandListenerBase {

    private final Main plugin;

    public WarpCMD(Main plugin) {
        super(plugin, "setwarp", "warp", "warps", "delwarp");
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (sender.hasPermission("essentialsmini.setwarp")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length == 1) {
                        String name = args[0];
                        new LocationsManager().setLocation("warps." + name.toLowerCase(), player.getLocation());
                        String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".Created");
                        if (message.contains("&"))
                            message = message.replace('&', '§');
                        if (message.contains("WarpName"))
                            message = message.replace("%WarpName%", name);
                        player.sendMessage(plugin.getPrefix() + message);
                    } else if (args.length == 2) {
                        String name = args[0];
                        double cost = Double.parseDouble(args[1]);
                        new LocationsManager().setWarp(name.toLowerCase(), player.getLocation(), cost);
                        String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".Created");
                        if (message.contains("&"))
                            message = message.replace('&', '§');
                        if (message.contains("WarpName"))
                            message = message.replace("%WarpName%", name);
                        player.sendMessage(plugin.getPrefix() + message);
                        String costMsg = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".Cost");
                        costMsg = new TextUtils().replaceAndToParagraph(costMsg);
                        costMsg = new TextUtils().replaceObject(costMsg, "%Cost%", cost + plugin.getCurrencySymbol());
                        player.sendMessage(plugin.getPrefix() + costMsg);
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
                            String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".Teleport");
                            if (message.contains("&"))
                                message = message.replace('&', '§');
                            if (message.contains("%WarpName%"))
                                message = message.replace("%WarpName%", name);
                            player.sendMessage(plugin.getPrefix() + message);
                        } catch (Exception ex) {
                            String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".NotExist");
                            if (message.contains("&"))
                                message = message.replace('&', '§');
                            player.sendMessage(plugin.getPrefix() + message);
                        }
                    } else if (args.length == 0) {
                        if (plugin.getSettingsCfg().getBoolean("WarpGUI")) {
                            InventoryManager inventoryManager = new InventoryManager();
                            inventoryManager.setTitle("§aWarps");
                            inventoryManager.setSize(3);
                            inventoryManager.create();
                            List<String> warps = new ArrayList<>();
                            ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("warps");
                            if (new LocationsManager().getCfg().contains("warps")) {
                                if (cs != null) {
                                    for (String s : cs.getKeys(false)) {
                                        if (s != null) {
                                            if (!new LocationsManager().getCfg().get("warps." + s).equals(" "))
                                                warps.add(s);
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < warps.size(); i++) {
                                if (new LocationsManager().costWarp(warps.get(i))) {
                                    inventoryManager.setItem(i, new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§6" + warps.get(i)).setLore("§aCost : §6" + new LocationsManager().getWarpCost(warps.get(i)), "§aTeleport to this Warp").build());
                                } else {
                                    inventoryManager.setItem(i, new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§6" + warps.get(i)).setLore("§aTeleport to this Warp").build());
                                }
                            }

                            inventoryManager.fillNull();
                            player.openInventory(inventoryManager.getInventory());

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
                                        TextComponent textComponent = new TextComponent("§6" + s);
                                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click me to add as Warp Command §6(/warp " + s + ")").create()));
                                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/warp " + s));
                                        sender.spigot().sendMessage(textComponent);
                                    }
                                }
                            }
                        }
                    } else {
                        String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".NotExist");
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
                    String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".Delete");
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

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§aWarps")) {
            List<String> warps = new ArrayList<>();
            ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("warps");
            if (new LocationsManager().getCfg().contains("warps")) {
                if (cs != null) {
                    for (String s : cs.getKeys(false)) {
                        if (s != null) {
                            if (!new LocationsManager().getCfg().get("warps." + s).equals(" "))
                                warps.add(s);
                        }
                    }
                }
            }
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (!event.getCurrentItem().hasItemMeta()) return;
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;
            for (String s : warps) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6" + s)) {
                    if (new LocationsManager().costWarp(s))
                        if (plugin.getVaultManager() != null)
                            if (Main.getInstance().getVaultManager().getEco().has(player, new LocationsManager().getWarpCost(s))) {
                                Main.getInstance().getVaultManager().getEco().withdrawPlayer(player, new LocationsManager().getWarpCost(s));
                            } else {
                                player.sendMessage(plugin.getPrefix() + "§cNot enought §6" + plugin.getVaultManager().getEconomy().currencyNamePlural());
                                return;
                            }
                    player.teleport(new LocationsManager().getLocation("warps." + s.toLowerCase()));
                    String message = plugin.getCustomMessagesConfig().getString(Variables.WARPMESSAGE + ".Teleport");
                    if (message.contains("&"))
                        message = message.replace('&', '§');
                    if (message.contains("%WarpName%"))
                        message = message.replace("%WarpName%", s);
                    player.sendMessage(plugin.getPrefix() + message);
                }
            }
        }
    }
}
