package de.framedev.essentialsmin.listeners;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 18.08.2020 22:47
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.LocationsManager;
import de.framedev.essentialsmin.managers.PlayerManager;
import de.framedev.essentialsmin.managers.PlayerManagerCfgLoss;
import de.framedev.essentialsmin.managers.PlayerManagerMongoDB;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static de.framedev.essentialsmin.managers.BackendManager.DATA;

public class PlayerListeners implements Listener {

    private final Main plugin;
    PlayerManagerCfgLoss cfgLoss;
    private final boolean jsonFormat;
    private final String permissionBase;

    private final boolean onlyEssentialsFeatures;

    public PlayerListeners(Main plugin) {
        this.plugin = plugin;
        plugin.getListeners()
                .add(this);
        permissionBase = plugin.getPermissionName();
        jsonFormat = plugin.getConfig()
                .getBoolean("JsonFormat");
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getVaultManager() != null && plugin.getVaultManager().getEco() != null) {
            if (plugin.isMongoDb()) {
                if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                    if (plugin.getVaultManager().getEco().hasAccount(event.getPlayer())) {
                        plugin.getBackendManager().updateUser(event.getPlayer(), DATA.MONEY.getName(), plugin.getVaultManager().getEco().getBalance(event.getPlayer()), "test");
                    }
                }
            }
            plugin.getVaultManager().getEco().createPlayerAccount(event.getPlayer());
        }
        if (plugin.getConfig()
                .getBoolean("JoinBoolean")) {
            if (plugin.getConfig()
                    .getBoolean("IgnoreJoinLeave")) {
                if (event.getPlayer()
                        .hasPermission("essentialsmini.ignorejoin")) {
                    event.setJoinMessage(null);
                } else {
                    String joinMessage = plugin.getConfig()
                            .getString("JoinMessage");
                    joinMessage = joinMessage.replace('&', '§');
                    joinMessage = joinMessage.replace("%Player%", event.getPlayer()
                            .getName());
                    event.setJoinMessage(joinMessage);
                }
            } else {
                String joinMessage = plugin.getConfig()
                        .getString("JoinMessage");
                joinMessage = joinMessage.replace('&', '§');
                joinMessage = joinMessage.replace("%Player%", event.getPlayer()
                        .getName());
                event.setJoinMessage(joinMessage);
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.getConfig()
                        .getBoolean("SpawnTP")) {
                    if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("plotme1")) {
                        LocationsManager spawnLocation = new LocationsManager("spawn");
                        try {
                            event.getPlayer()
                                    .teleport(spawnLocation.getLocation());
                        } catch (IllegalArgumentException ex) {
                            event.getPlayer()
                                    .teleport(event.getPlayer()
                                            .getWorld()
                                            .getSpawnLocation());
                        }
                    }
                }
                cancel();

            }
        }.runTaskLater(plugin, 20);
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (!isJsonFormat()) {
                new PlayerManager(event.getPlayer()
                        .getUniqueId()).setLastLogin(System.currentTimeMillis());
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            if (!plugin.getCfgLossHashMap()
                                    .containsKey(event.getPlayer())) {
                                if (plugin.isMysql()) {
                                    if (PlayerManagerCfgLoss.loadPlayerData(event.getPlayer()) != null) {
                                        plugin.getCfgLossHashMap()
                                                .put(event.getPlayer(), PlayerManagerCfgLoss.loadPlayerData(event.getPlayer()));
                                        plugin.getCfgLossHashMap().get(event.getPlayer()).setLastLogin(System.currentTimeMillis());
                                    } else {
                                        plugin.getCfgLossHashMap()
                                                .put(event.getPlayer(), new PlayerManagerCfgLoss(event.getPlayer()));
                                        plugin.getCfgLossHashMap().get(event.getPlayer()).setLastLogin(System.currentTimeMillis());
                                    }
                                } else {
                                    plugin.getCfgLossHashMap()
                                            .put(event.getPlayer(), PlayerManagerCfgLoss.getPlayerManagerCfgLoss(event.getPlayer()));
                                    plugin.getCfgLossHashMap()
                                            .get(event.getPlayer())
                                            .setLastLogin(System.currentTimeMillis());
                                }
                            }
                        } catch (FileNotFoundException e) {
                            if (!plugin.getCfgLossHashMap()
                                    .containsKey(event.getPlayer())) {
                                if (plugin.isMysql()) {
                                    if (PlayerManagerCfgLoss.loadPlayerData(event.getPlayer()) != null) {
                                        plugin.getCfgLossHashMap()
                                                .put(event.getPlayer(), PlayerManagerCfgLoss.loadPlayerData(event.getPlayer()));
                                    } else {
                                        plugin.getCfgLossHashMap()
                                                .put(event.getPlayer(), new PlayerManagerCfgLoss(event.getPlayer()));
                                    }
                                } else {
                                    PlayerManagerCfgLoss cfgLoss = new PlayerManagerCfgLoss(event.getPlayer());
                                    plugin.getCfgLossHashMap()
                                            .put(event.getPlayer(), cfgLoss);
                                    plugin.getCfgLossHashMap()
                                            .get(event.getPlayer())
                                            .setLastLogin(System.currentTimeMillis());
                                }
                            }
                        }
                        cancel();
                    }
                }.runTaskLater(plugin, 20 * 6);
            }
        }

        if (isEnabled() && !onlyEssentialsFeatures) {
            if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                    plugin.getBackendManager().createUserMoney(event.getPlayer(), "test");
                    plugin.getBackendManager().updateUser(event.getPlayer(), "lastLogin", System.currentTimeMillis() + "", "test");
                    plugin.getBackendManager().updateUser(event.getPlayer(), "offline", false, "test");
                    PlayerManagerMongoDB.getPlayerManager(event.getPlayer().getUniqueId(),"test").save("test");
                }
            }

            plugin.removeOfflinePlayer(event.getPlayer());
        }

        if (!event.getPlayer().hasPlayedBefore()) {
            if (plugin.getConfig().getBoolean("StartBalance.Boolean")) {
                double startBalance = plugin.getConfig().getDouble("StartBalance.Amount");
                plugin.getVaultManager().getEco().depositPlayer(event.getPlayer(), startBalance);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isEnabled() && !onlyEssentialsFeatures) {
            plugin.addOfflinePlayer(event.getPlayer());
            plugin.savePlayers();
            if (!isJsonFormat()) {
                new PlayerManager(event.getPlayer()
                        .getUniqueId()).setLastLogout(System.currentTimeMillis());
            } else {
                if (plugin.getCfgLossHashMap()
                        .containsKey(event.getPlayer())) {
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .setLastLogout(System.currentTimeMillis());
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .saveTimePlayed();
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .savePlayerManager();
                    plugin.getCfgLossHashMap().get(event.getPlayer()).savePlayerManager();
                    if (plugin.isMysql()) {
                        plugin.getCfgLossHashMap()
                                .get(event.getPlayer())
                                .setLastLogout(System.currentTimeMillis());
                        plugin.getCfgLossHashMap()
                                .get(event.getPlayer()).savePlayerData(event.getPlayer());
                    }
                }
            }
        }
        if (plugin.getConfig()
                .getBoolean("LeaveBoolean")) {
            if (plugin.getConfig()
                    .getBoolean("IgnoreJoinLeave")) {
                if (event.getPlayer()
                        .hasPermission("essentialsmini.ignoreleave")) {
                    event.setQuitMessage(null);
                } else {
                    String joinMessage = plugin.getConfig()
                            .getString("LeaveMessage");
                    if (joinMessage.contains("&")) {
                        joinMessage = joinMessage.replace('&', '§');
                    }
                    joinMessage = joinMessage.replace("%Player%", event.getPlayer()
                            .getName());
                    event.setQuitMessage(joinMessage);
                }
            } else {
                String joinMessage = plugin.getConfig()
                        .getString("LeaveMessage");
                joinMessage = joinMessage.replace('&', '§');
                joinMessage = joinMessage.replace("%Player%", event.getPlayer()
                        .getName());
                event.setQuitMessage(joinMessage);
            }
        }
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                    plugin.getBackendManager().createUserMoney(event.getPlayer(), "test");
                    plugin.getBackendManager().updateUser(event.getPlayer(), "lastLogout", System.currentTimeMillis() + "", "test");
                    plugin.getBackendManager().updateUser(event.getPlayer(), "offline", true, "test");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        plugin.addOfflinePlayer(event.getPlayer());
        plugin.savePlayers();
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (!isJsonFormat()) {
                new PlayerManager(event.getPlayer()
                        .getUniqueId()).setLastLogout(System.currentTimeMillis());
            } else {
                if (plugin.getCfgLossHashMap()
                        .containsKey(event.getPlayer())) {
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .setLastLogout(System.currentTimeMillis());
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .saveTimePlayed();
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .savePlayerManager();
                    plugin.getCfgLossHashMap().get(event.getPlayer()).savePlayerManager();
                    if (plugin.isMysql()) {
                        plugin.getCfgLossHashMap()
                                .get(event.getPlayer())
                                .setLastLogout(System.currentTimeMillis());
                        plugin.getCfgLossHashMap()
                                .get(event.getPlayer()).savePlayerData(event.getPlayer());
                    }
                }
            }
            if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                    plugin.getBackendManager().updateUser(event.getPlayer(), DATA.LASTLOGOUT.getName(), System.currentTimeMillis() + "", "test");
                    plugin.getBackendManager().updateUser(event.getPlayer(), "offline", true, "test");
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (isEnabled() && !onlyEssentialsFeatures) {
                Player damager = (Player) event.getDamager();
                if (isJsonFormat()) {
                    if (plugin.getCfgLossHashMap()
                            .containsKey(damager)) {
                        plugin.getCfgLossHashMap()
                                .get(damager)
                                .addDamage(event.getFinalDamage());
                    }
                } else {
                    new PlayerManager(damager).addDamage(event.getFinalDamage());
                }
                if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                    if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                        double damage = (double) plugin.getBackendManager().get(damager, "damage", "test");
                        damage += event.getFinalDamage();
                        plugin.getBackendManager().updateUser(damager, "damage", damage, "test");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (livingEntity.getKiller() != null) {
                if (livingEntity.getKiller() instanceof Player) {
                    if (isJsonFormat()) {
                        if (plugin.getCfgLossHashMap()
                                .containsKey(livingEntity.getKiller())) {
                            plugin.getCfgLossHashMap().get(livingEntity.getKiller()).addEntityType(event.getEntityType());
                            plugin.getCfgLossHashMap().get(livingEntity.getKiller()).addEntityKill();
                        }
                    } else {
                        new PlayerManager(livingEntity.getKiller()).addEntityKill();
                        new PlayerManager(livingEntity.getKiller()).addEntityType(event.getEntityType());
                    }
                    if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                        if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                            int entityKill = (int) plugin.getBackendManager().get(livingEntity.getKiller(), "entityKills", "test");
                            entityKill++;
                            plugin.getBackendManager().updateUser(livingEntity.getKiller(), "entityKills", entityKill, "test");
                            ArrayList<String> materis = (ArrayList<String>) plugin.getBackendManager().get(livingEntity.getKiller(), "entityTypes", "test");
                            if (materis != null) {
                                if (!materis.contains(event.getEntityType().name())) {
                                    materis.add(event.getEntityType().name());
                                    plugin.getBackendManager().updateUser(livingEntity.getKiller(), "entityTypes", materis, "test");
                                }
                            } else {
                                ArrayList<String> types = new ArrayList<>();
                                types.add(event.getEntityType().name());
                                plugin.getBackendManager().updateUser(livingEntity.getKiller(), "entityTypes", types, "test");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity()
                .getKiller() != null) {
            if (event.getEntity().getKiller() instanceof Player) {
                if (isEnabled() && !onlyEssentialsFeatures) {
                    if (isJsonFormat()) {
                        if (plugin.getCfgLossHashMap().containsKey(event.getEntity().getKiller())) {
                            plugin.getCfgLossHashMap().get(event.getEntity().getKiller()).addPlayerKill();
                        }
                    } else {
                        new PlayerManager(event.getEntity().getKiller()).addPlayerKill();
                    }
                    if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                        if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                            int kills = (int) plugin.getBackendManager().get(event.getEntity().getKiller(), "kills", "test");
                            kills++;
                            plugin.getBackendManager().updateUser(event.getEntity().getKiller(), "kills", kills, "test");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeathByEntity(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (isEnabled() && !onlyEssentialsFeatures) {
                if (isJsonFormat()) {
                    if (plugin.getCfgLossHashMap()
                            .containsKey(player)) {
                        plugin.getCfgLossHashMap()
                                .get(player)
                                .addDeath();
                    }
                } else {
                    new PlayerManager(player).addDeath();
                }
                if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                    if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                        int deaths = (int) plugin.getBackendManager().get((Player) event.getEntity(), DATA.DEATHS.getName(), "test");
                        deaths++;
                        plugin.getBackendManager().updateUser((Player) event.getEntity(), DATA.DEATHS.getName(), deaths, "test");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (isJsonFormat()) {
                if (plugin.getCfgLossHashMap()
                        .containsKey(event.getPlayer())) {
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .addBlockBroken();
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .addBlockBrokensMaterial(event.getBlock()
                                    .getType());
                }
            } else {
                new PlayerManager(event.getPlayer()).addBlockBroken();
                new PlayerManager(event.getPlayer()).addBlocksBroken(event.getBlock()
                        .getType());
            }
            if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                    ArrayList<String> materis = (ArrayList<String>) plugin.getBackendManager().get(event.getPlayer(), "blocksBroken", "test");
                    if (!materis.contains(event.getBlock().getType().name())) {
                        materis.add(event.getBlock().getType().name());
                        plugin.getBackendManager().updateUser(event.getPlayer(), "blocksBroken", materis, "test");
                    }
                    PlayerManagerMongoDB pl = PlayerManagerMongoDB.getPlayerManager(event.getPlayer().getUniqueId(),"test");
                    pl.setBlockBroken(pl.getBlockBroken() + 1);
                    pl.save("test");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (isJsonFormat()) {
                if (plugin.getCfgLossHashMap()
                        .containsKey(event.getPlayer())) {
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .addBlockPlace();
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .addBlocksPlace(event.getBlock()
                                    .getType());
                }
            } else {
                new PlayerManager(event.getPlayer()).addBlockPlace();
                new PlayerManager(event.getPlayer()).addBlocksPlace(event.getBlock()
                        .getType());
            }
            if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                    ArrayList<String> materis = (ArrayList<String>) plugin.getBackendManager().get(event.getPlayer(), "blocksPlacen", "test");
                    if (!materis.contains(event.getBlock().getType().name())) {
                        materis.add(event.getBlock().getType().name());
                        plugin.getBackendManager().updateUser(event.getPlayer(), "blocksPlacen", materis, "test");
                    }
                    PlayerManagerMongoDB pl = PlayerManagerMongoDB.getPlayerManager(event.getPlayer().getUniqueId(),"test");
                    pl.setBlockPlacen(pl.getBlockPlacen() + 1);
                    pl.save("test");
                }
            }
        }
    }

    @EventHandler
    public void onCommandUsed(PlayerCommandPreprocessEvent event) {
        if (isEnabled() && !onlyEssentialsFeatures) {
            if (isJsonFormat()) {
                if (plugin.getCfgLossHashMap()
                        .containsKey(event.getPlayer())) {
                    plugin.getCfgLossHashMap()
                            .get(event.getPlayer())
                            .addCommandsUsed();
                }
            } else {
                new PlayerManager(event.getPlayer()).addCommandsUsed();
            }
            if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
                if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                    int commandsUsed = (int) plugin.getBackendManager().get(event.getPlayer(), "commandsUsed", "test");
                    commandsUsed++;
                    plugin.getBackendManager().updateUser(event.getPlayer(), "commandsUsed", commandsUsed, "test");
                }
            }
        }
    }
}
