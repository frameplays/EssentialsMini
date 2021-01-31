package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.utils.ReplaceCharConfig;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 31.01.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class AFK implements CommandExecutor {
    private static HashMap<String, String> afkPlayerMap;
    private static HashMap<String, Long> afkTimeMap;
    private final Main plugin;
    private HashMap<String, Location> locationMap;

    public AFK(Main plugin) {
        this.plugin = plugin;
        this.locationMap = new HashMap<>();
        afkPlayerMap = new HashMap<>();
        afkTimeMap = new HashMap<>();
        int afktime = plugin.getConfig().getInt("AFK.Time");
        plugin.getCommands().put("afk",this);
        plugin.getCommand("afk").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new Events(), plugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new IdleTimer(plugin), (afktime * 20), (afktime * 20));
    }

    public static void putPlayerToAfkMap(String PlayerName) {
        afkPlayerMap.put(PlayerName, Bukkit.getPlayer(PlayerName).getPlayerListName());
    }

    public static void removePlayerFromAfkMap(String playerName) {
        afkPlayerMap.remove(playerName);
    }

    public static String getAfkPlayerName(String playerName) {
        return afkPlayerMap.get(playerName);
    }

    public static void putPlayerToTimeMap(String playerName) {
        afkTimeMap.put(playerName, Bukkit.getPlayer(playerName).getPlayerTime());
        Bukkit.getPlayer(playerName).resetPlayerTime();
    }

    public static void removePlayerFromTimeMap(String playerName) {
        afkTimeMap.remove(playerName);
    }

    public static Long getPlayerAfkTime(String playerName) {
        return afkTimeMap.get(playerName);
    }

    public static boolean isPlayerAfk(String playerName) {
        return afkPlayerMap.containsKey(playerName);
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(plugin.getPermissionName() + "afk")) {
                if (command.getName().equalsIgnoreCase("afk")) {
                    if (isPlayerAfk(player.getName())) {
                        afk(player, true, "");
                        return true;
                    }
                    afk(player, false, "");
                    return true;
                }
            } else {
                player.sendMessage(plugin.getPrefix() + " " + plugin.getNOPERMS());
                return true;
            }
        }
        return false;
    }

    public void afk(Player player, boolean afk, String reason) {
        String isafkMessage = this.plugin.getConfig().getString("AFK.IsAFK");
        String notafkMessage = this.plugin.getConfig().getString("AFK.IsNotAFK");


        if (afk) {
            if (notafkMessage.matches(".*\\[Time].*")) {
                String afktime = returnAfkTime(player.getName());
                notafkMessage = notafkMessage.replace("[Time]", "");
                Bukkit.getPlayer(player.getName()).setPlayerListName(getAfkPlayerName(player.getName()));
                removePlayerFromAfkMap(player.getName());
                notafkMessage = ReplaceCharConfig.replaceParagraph(notafkMessage);
                TextComponent tc = new TextComponent();
                tc.setText(player.getName());
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(player.getName())).create()));
                notafkMessage = ReplaceCharConfig.replaceObjectWithData(notafkMessage, "[Player]", player.getName());
                Bukkit.broadcastMessage(notafkMessage);
                Bukkit.getConsoleSender().sendMessage("§7" + player.getName() + " is no longer AFK " + afktime);
            } else {
                TextComponent tc = new TextComponent();
                tc.setText(player.getName());
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(player.getName())).create()));
                Bukkit.getPlayer(player.getName()).setPlayerListName(getAfkPlayerName(player.getName()));
                removePlayerFromAfkMap(player.getName());
                notafkMessage = ReplaceCharConfig.replaceParagraph(notafkMessage);
                notafkMessage = ReplaceCharConfig.replaceObjectWithData(notafkMessage, "[Player]", player.getName());
                Bukkit.broadcastMessage(notafkMessage);
            }
        } else {

            TextComponent tc = new TextComponent();
            tc.setText(player.getName());
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(player.getName())).create()));
            putPlayerToAfkMap(player.getName());
            putPlayerToTimeMap(player.getName());
            isafkMessage = ReplaceCharConfig.replaceParagraph(isafkMessage);
            isafkMessage = ReplaceCharConfig.replaceObjectWithData(isafkMessage, "[Player]", player.getName());
            Bukkit.broadcastMessage(isafkMessage);
        }
    }


    String returnAfkTime(String playerName) {
        long oldTime = getPlayerAfkTime(playerName).longValue();
        long currentTime = Bukkit.getPlayer(playerName).getPlayerTime();
        long minutes = (currentTime - oldTime) / 20L / 60L;
        long seconds = (currentTime - oldTime) / 20L % 60L;
        return (minutes == 0L) ? (seconds + "s") : (minutes + "m" + seconds + "s");
    }

    HashMap<String, Location> getLocationMap() {
        return this.locationMap;
    }

    public class Events implements Listener {
        @EventHandler
        private void onPlayerMove(PlayerMoveEvent event) {
            if (AFK.isPlayerAfk(event.getPlayer().getName())) {
                int movX = event.getFrom().getBlockX() - event.getTo().getBlockX();
                int movZ = event.getFrom().getBlockZ() - event.getTo().getBlockZ();
                if (Math.abs(movX) > 0 || Math.abs(movZ) > 0) {
                    AFK.this.afk(event.getPlayer(), true, "");
                }
            }
        }

        @EventHandler
        public void onPlayerDamage(EntityDamageEvent event) {
            if(event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (AFK.isPlayerAfk(player.getName())) {
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void onBlockPlace(BlockPlaceEvent event) {
            if (AFK.isPlayerAfk(event.getPlayer().getName())) {
                String playerName = event.getPlayer().getName();
                AFK.removePlayerFromAfkMap(event.getPlayer().getName());
                AFK.removePlayerFromTimeMap(event.getPlayer().getName());
                AFK.this.returnAfkTime(event.getPlayer().getName());
                AFK.this.getLocationMap().remove(playerName);
            }
        }

        @EventHandler
        public void onBlockBreak(BlockBreakEvent event) {
            if (AFK.isPlayerAfk(event.getPlayer().getName())) {
                String playerName = event.getPlayer().getName();
                AFK.removePlayerFromAfkMap(event.getPlayer().getName());
                AFK.removePlayerFromTimeMap(event.getPlayer().getName());
                AFK.this.returnAfkTime(event.getPlayer().getName());
                AFK.this.getLocationMap().remove(playerName);
            }
        }

        @EventHandler
        public void onChat(AsyncPlayerChatEvent event) {
            if (AFK.isPlayerAfk(event.getPlayer().getName())) {
                String playerName = event.getPlayer().getName();
                AFK.removePlayerFromAfkMap(event.getPlayer().getName());
                AFK.removePlayerFromTimeMap(event.getPlayer().getName());
                AFK.this.returnAfkTime(event.getPlayer().getName());
                AFK.this.getLocationMap().remove(playerName);
            }
        }

        @EventHandler( priority = EventPriority.MONITOR )
        private void onPlayerQuit(PlayerQuitEvent event) {
            String playerName = event.getPlayer().getName();
            AFK.removePlayerFromAfkMap(playerName);
            AFK.removePlayerFromTimeMap(playerName);
            AFK.this.getLocationMap().remove(playerName);
        }

        @EventHandler( priority = EventPriority.MONITOR )
        private void onPlayerKicked(PlayerKickEvent event) {
            String playerName = event.getPlayer().getName();
            AFK.removePlayerFromAfkMap(playerName);
            AFK.removePlayerFromTimeMap(playerName);
            AFK.this.getLocationMap().remove(playerName);
        }
    }

    class IdleTimer implements Runnable {
        private final Main plugin;
        int afktime = Main.getInstance().getConfig().getInt("AFK.Time");

        IdleTimer(Main plugin) {
            this.plugin = plugin;
        }

        public void run() {
            for (Player player : this.plugin.getServer().getOnlinePlayers()) {
                if (AFK.this.getLocationMap().containsKey(player.getName()) && ((Location) AFK.this.getLocationMap().get(player.getName())).equals(player.getLocation()) && !AFK.isPlayerAfk(player.getName())) {
                    long idleTime = (this.afktime * 20);
                    player.setPlayerTime(-idleTime, true);
                    AFK.this.afk(player, false, "");
                }
                AFK.this.getLocationMap().put(player.getName(), player.getLocation());
            }
        }
    }
}
