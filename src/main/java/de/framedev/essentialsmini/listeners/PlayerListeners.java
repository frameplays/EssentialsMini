package de.framedev.essentialsmini.listeners;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 18.08.2020 22:47
 */

import com.google.gson.GsonBuilder;
import de.framedev.essentialsmini.commands.playercommands.KillCMD;
import de.framedev.essentialsmini.commands.playercommands.SpawnCMD;
import de.framedev.essentialsmini.commands.playercommands.VanishCMD;
import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.LocationsManager;
import de.framedev.essentialsmini.managers.PlayerManager;
import de.framedev.essentialsmini.managers.PlayerManagerCfgLoss;
import de.framedev.essentialsmini.managers.PlayerManagerMongoDB;
import de.framedev.mysqlapi.api.SQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static de.framedev.essentialsmini.managers.BackendManager.DATA;

public class PlayerListeners implements Listener {

    private final Main plugin;
    PlayerManagerCfgLoss cfgLoss;
    private boolean jsonFormat;
    private final String collection = "essentialsmini_data";

    private boolean onlyEssentialsFeatures;

    public PlayerListeners(Main plugin) {
        this.plugin = plugin;
        plugin.getListeners().add(this);
        String permissionBase = plugin.getPermissionName();
        jsonFormat = plugin.getConfig().getBoolean("JsonFormat");
        this.onlyEssentialsFeatures = plugin.getConfig().getBoolean("OnlyEssentialsFeatures");
    }

    public boolean isOnlyEssentialsFeatures() {
        return onlyEssentialsFeatures;
    }

    public boolean isEnabled() {
        return plugin.getConfig().getBoolean("PlayerInfoSave");
    }

    public boolean isJsonFormat() {
        return jsonFormat;
    }

    @EventHandler
    public void onColorChat(AsyncPlayerChatEvent event) {
        if (plugin.getConfig().getBoolean("ColoredChat")) {
            String message = event.getMessage();
            if (message.contains("&"))
                message = message.replace('&', '§');
            event.setMessage(message);
        }
    }

    @EventHandler
    public void onSignColo(SignChangeEvent event) {
        if (plugin.getConfig().getBoolean("ColoredSigns")) {
            for (int i = 0; i < event.getLines().length; i++) {
                if (event.getLines()[i].contains("&")) {
                    String line = event.getLines()[i];
                    line = line.replace('&', '§');
                    event.setLine(i, line);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getVaultManager() != null && plugin.getVaultManager().getEco() != null) {
            if (plugin.isMongoDB()) {
                if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                    if (plugin.getVaultManager().getEco().hasAccount(event.getPlayer())) {
                        plugin.getBackendManager().updateUser(event.getPlayer(), DATA.MONEY.getName(), plugin.getVaultManager().getEco().getBalance(event.getPlayer()), collection);
                    }
                }
            }
            plugin.getVaultManager().getEco().createPlayerAccount(event.getPlayer());
        }
        if (!VanishCMD.hided.contains(event.getPlayer().getName())) {
            if (plugin.getConfig().getBoolean("JoinBoolean")) {
                if (plugin.getConfig().getBoolean("IgnoreJoinLeave")) {
                    if (event.getPlayer().hasPermission("essentialsmini.ignorejoin")) {
                        event.setJoinMessage(null);
                    } else {
                        String joinMessage = plugin.getConfig().getString("JoinMessage");
                        joinMessage = joinMessage.replace('&', '§');
                        joinMessage = joinMessage.replace("%Player%", event.getPlayer().getName());
                        event.setJoinMessage(joinMessage);
                    }
                } else {
                    String joinMessage = plugin.getConfig().getString("JoinMessage");
                    joinMessage = joinMessage.replace('&', '§');
                    joinMessage = joinMessage.replace("%Player%", event.getPlayer().getName());
                    event.setJoinMessage(joinMessage);
                }
            }
        } else {
            event.setJoinMessage(null);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.getConfig().getBoolean("SpawnTP")) {
                    if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("plotme1")) {
                        LocationsManager spawnLocation = new LocationsManager("spawn");
                        try {
                            event.getPlayer().teleport(spawnLocation.getLocation());
                        } catch (IllegalArgumentException ex) {
                            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
                        }
                    }
                }
                cancel();

            }
        }.runTaskLater(plugin, 20);
        if (!event.getPlayer().hasPlayedBefore()) {
            if (plugin.getConfig().getBoolean("StartBalance.Boolean")) {
                double startBalance = plugin.getConfig().getDouble("StartBalance.Amount");
                plugin.getVaultManager().getEco().depositPlayer(event.getPlayer(), startBalance);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!VanishCMD.hided.contains(event.getPlayer().getName())) {
            if (plugin.getConfig().getBoolean("LeaveBoolean")) {
                if (plugin.getConfig().getBoolean("IgnoreJoinLeave")) {
                    if (event.getPlayer().hasPermission("essentialsmini.ignoreleave")) {
                        event.setQuitMessage(null);
                    } else {
                        String joinMessage = plugin.getConfig().getString("LeaveMessage");
                        if (joinMessage.contains("&")) {
                            joinMessage = joinMessage.replace('&', '§');
                        }
                        joinMessage = joinMessage.replace("%Player%", event.getPlayer().getName());
                        event.setQuitMessage(joinMessage);
                    }
                } else {
                    String joinMessage = plugin.getConfig().getString("LeaveMessage");
                    joinMessage = joinMessage.replace('&', '§');
                    joinMessage = joinMessage.replace("%Player%", event.getPlayer().getName());
                    event.setQuitMessage(joinMessage);
                }
            }
        } else {
            event.setQuitMessage(null);
        }
    }

    /**
     * @param event Respawn event {@link SpawnCMD}
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        try {
            event.setRespawnLocation(new LocationsManager("spawn").getLocation());
        } catch (Exception ignored) {
            event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (KillCMD.suicid) {
            event.setDeathMessage(null);
            KillCMD.suicid = false;
        }
    }
}