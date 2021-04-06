package de.framedev.essentialsmin.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 08.08.2020 20:56
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.utils.InventoryStringDeSerializer;
import de.framedev.essentialsmin.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackpackCMD implements CommandExecutor, TabCompleter, Listener {

    static File file;
    static FileConfiguration cfg;
    public static HashMap<String, String> itemsStringHashMap = new HashMap<>();
    private final Main plugin;

    public BackpackCMD(Main plugin) {
        this.plugin = plugin;
        if (plugin.getConfig().getBoolean("Backpack")) {
            plugin.getCommands().put("backpack", this);
            plugin.getTabCompleters().put("backpack", this);
            plugin.getListeners().add(this);
        }
        file = new File(Main.getInstance().getDataFolder(), "backpack.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }


    @EventHandler
    public void onCloseGui(InventoryCloseEvent event) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (event.getView().getTitle().equalsIgnoreCase(offlinePlayer.getName() + "'s Inventory")) {
                itemsStringHashMap.put(offlinePlayer.getUniqueId().toString(), InventoryStringDeSerializer.itemStackArrayToBase64(event.getInventory().getContents()));
            }
        }
    }

    // Restore BackPack into HashMap
    public static void restore(OfflinePlayer player) {
        if (cfg.contains(player.getUniqueId().toString() + ".Inventory")) {
                String content = cfg.getString(player.getUniqueId().toString() + ".Inventory");
                if (content != null)
                    itemsStringHashMap.put(player.getUniqueId().toString(), content);
        }
    }

    // Save Backpack
    public static void save(OfflinePlayer player) {
        if (!itemsStringHashMap.isEmpty()) {
            if (itemsStringHashMap.containsKey(player.getUniqueId().toString())) {
                for (Map.Entry<String, String> entry : itemsStringHashMap.entrySet()) {
                    cfg.set(entry.getKey() + ".Inventory", entry.getValue());
                }
                try {
                    cfg.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                Player player = (Player) sender;
                if (plugin.getConfig().getBoolean("Backpack")) {
                    Inventory inventory = Bukkit.createInventory(null, 3 * 9, player.getName() + "'s Inventory");
                    if (itemsStringHashMap.containsKey(player.getUniqueId().toString()) && !(itemsStringHashMap.get(player.getUniqueId().toString()) == null)) {
                        try {
                            inventory.setContents(InventoryStringDeSerializer.itemStackArrayFromBase64(itemsStringHashMap.get(player.getUniqueId().toString())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.openInventory(inventory);
                    } else {
                        player.openInventory(inventory);
                    }
                }
            } else if (args.length == 1) {
                Player player = (Player) sender;
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                if (args[0].equalsIgnoreCase(targetPlayer.getName()) && !args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("essentialsmini.backpack.see")) {
                        if (plugin.getConfig().getBoolean("Backpack")) {
                            Inventory inventory = Bukkit.createInventory(null, 3 * 9, targetPlayer.getName() + "'s Inventory");
                            if (itemsStringHashMap.containsKey(targetPlayer.getUniqueId().toString()) && !(itemsStringHashMap.get(targetPlayer.getUniqueId().toString()) == null)) {
                                try {
                                    inventory.setContents(InventoryStringDeSerializer.itemStackArrayFromBase64(itemsStringHashMap.get(targetPlayer.getUniqueId().toString())));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.openInventory(inventory);
                            } else {
                                String message = plugin.getCustomMessagesConfig().getString("NoBackPackFound");
                                if (message != null) {
                                    message = new TextUtils().replaceAndToParagraph(message);
                                }
                                player.sendMessage(plugin.getPrefix() + message);
                            }
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("essentialsmini.backpack.delete")) {
                        itemsStringHashMap.clear();
                        if (file.exists()) {
                            file.delete();
                            player.sendMessage(plugin.getPrefix() + "§6BackPacks gelöscht!");
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§cError beim Löschen der Backpacks");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    }
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("essentialsmini.backpack.delete")) {
                cmds.add("delete");
                return cmds;
            }
        }
        return null;
    }
}
