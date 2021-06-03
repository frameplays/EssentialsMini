package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.DateUnit;
import de.framedev.essentialsmini.utils.ReplaceCharConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmini.commands.playercommands
 * ClassName MuteCMD
 * Date: 15.05.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class MuteCMD extends CommandBase implements Listener {

    private final Main plugin;

    private final ArrayList<OfflinePlayer> muted;

    private File file;
    private FileConfiguration cfg;

    public MuteCMD(Main plugin) {
        super(plugin);
        setup("mute", this);
        setup("tempmute", this);
        setup("muteinfo", this);
        setup("removetempmute", this);
        setupTabCompleter("tempmute", this);
        this.plugin = plugin;
        plugin.getListeners().add(this);
        this.muted = plugin.getVariables().getPlayers();
        this.file = new File(plugin.getDataFolder(), "tempmutes.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mute")) {
            if (args.length == 1) {
                if (!sender.hasPermission(plugin.getPermissionName() + "mute")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }

                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if (muted.contains(player)) {
                    muted.remove(player);
                    String selfUnMute = plugin.getCustomMessagesConfig().getString("Mute.Self.Deactivate");
                    selfUnMute = ReplaceCharConfig.replaceParagraph(selfUnMute);
                    if (player.isOnline())
                        ((Player) player).sendMessage(plugin.getPrefix() + selfUnMute);
                    String otherUnMute = plugin.getCustomMessagesConfig().getString("Mute.Other.Deactivate");
                    otherUnMute = ReplaceCharConfig.replaceParagraph(otherUnMute);
                    otherUnMute = ReplaceCharConfig.replaceObjectWithData(otherUnMute, "%Player%", player.getName());
                    sender.sendMessage(plugin.getPrefix() + otherUnMute);
                } else {
                    muted.add(player);
                    String selfMute = plugin.getCustomMessagesConfig().getString("Mute.Self.Activate");
                    selfMute = ReplaceCharConfig.replaceParagraph(selfMute);
                    if (player.isOnline())
                        ((Player) player).sendMessage(plugin.getPrefix() + selfMute);
                    String otherMute = plugin.getCustomMessagesConfig().getString("Mute.Other.Activate");
                    otherMute = ReplaceCharConfig.replaceParagraph(otherMute);
                    otherMute = ReplaceCharConfig.replaceObjectWithData(otherMute, "%Player%", player.getName());
                    sender.sendMessage(plugin.getPrefix() + otherMute);
                }
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("tempmute")) {
            if (args.length == 4) {
                if (!sender.hasPermission(plugin.getPermissionName() + "tempmute")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }

                MuteReason muteReason = MuteReason.valueOf(args[1].toLowerCase());
                DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                long value = Long.parseLong(args[2]);
                long current = System.currentTimeMillis();
                long millis = value * unit.getToSec() * 1000;
                long newValue = current + millis;
                Date date = new Date(newValue);
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if (plugin.isMysql() || plugin.isSQL()) {
                    new BanMuteManager().setTempMute(player, muteReason, date.toString());
                } else {
                    cfg.set(player.getName() + ".reason", muteReason.getReason());
                    cfg.set(player.getName() + ".expire", date);
                    try {
                        cfg.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String selfMute = plugin.getCustomMessagesConfig().getString("Mute.Self.Activate");
                    selfMute = ReplaceCharConfig.replaceParagraph(selfMute);
                    if (player.isOnline())
                        ((Player) player).sendMessage(plugin.getPrefix() + selfMute);
                    String otherMute = plugin.getCustomMessagesConfig().getString("Mute.Other.Activate");
                    otherMute = ReplaceCharConfig.replaceParagraph(otherMute);
                    otherMute = ReplaceCharConfig.replaceObjectWithData(otherMute, "%Player%", player.getName());
                    sender.sendMessage(plugin.getPrefix() + otherMute);
                }
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("removetempmute")) {
            if (args.length == 1) {
                if (!sender.hasPermission(plugin.getPermissionName() + "removetempmute")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }

                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if (plugin.isMysql() || plugin.isSQL()) {
                    new BanMuteManager().removeTempMute(player);
                } else {
                    if (cfg.contains(player.getName())) {
                        cfg.set(player.getName(), null);
                        try {
                            cfg.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String selfUnMute = plugin.getCustomMessagesConfig().getString("Mute.Self.Deactivate");
                        selfUnMute = ReplaceCharConfig.replaceParagraph(selfUnMute);
                        if (player.isOnline())
                            ((Player) player).sendMessage(plugin.getPrefix() + selfUnMute);
                        String otherUnMute = plugin.getCustomMessagesConfig().getString("Mute.Other.Deactivate");
                        otherUnMute = ReplaceCharConfig.replaceParagraph(otherUnMute);
                        otherUnMute = ReplaceCharConfig.replaceObjectWithData(otherUnMute, "%Player%", player.getName());
                        sender.sendMessage(plugin.getPrefix() + otherUnMute);
                    }
                }
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("muteinfo")) {
            if (!sender.hasPermission(plugin.getPermissionName() + "muteinfo")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                return true;
            }

            ArrayList<OfflinePlayer> players = new ArrayList<>();
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (cfg.contains(offlinePlayer.getName())) {
                    players.add(offlinePlayer);
                }
            }

            players.forEach(player -> {
                sender.sendMessage("§6" + player.getName() + " §ais Muted while : §6" + cfg.getString(player.getName() + ".reason"));
                sender.sendMessage("§aExpired at §6: " + cfg.getString(player.getName() + ".expire"));
            });
        }
        return super.onCommand(sender, command, label, args);
    }

    private boolean isExpired(Player player) {
        if (cfg.contains(player.getName() + ".reason")) {
            Date date = (Date) cfg.get(player.getName() + ".expire");
            if (date != null)
                return date.getTime() < System.currentTimeMillis();
        }
        return true;
    }

    @EventHandler
    public void onChatWrite(AsyncPlayerChatEvent event) {
        if (!isExpired(event.getPlayer())) {
            if (plugin.isMysql() || plugin.isSQL()) {
                final Date[] date = {new Date()};
                new BanMuteManager().getTempMute(event.getPlayer()).forEach((s, s2) -> date[0] = new Date(s));
                event.getPlayer().sendMessage(plugin.getPrefix() + "§cYou are Muted! While §6" + cfg.getString(event.getPlayer().getName() + ".reason") + " | §aExpired at : §6" + date[0].toString());
            } else {
                Date date = (Date) cfg.get(event.getPlayer().getName() + ".expire");
                event.getPlayer().sendMessage(plugin.getPrefix() + "§cYou are Muted! While §6" + cfg.getString(event.getPlayer().getName() + ".reason") + " | §aExpired at : §6" + date.toString());
            }
            event.setCancelled(true);
        } else {
            Player player = event.getPlayer();
            if (plugin.isMysql() || plugin.isSQL()) {
                new BanMuteManager().removeTempBan(player);
            } else {
                if (cfg.contains(player.getName() + ".reason")) {
                    cfg.set(player.getName(), null);
                    try {
                        cfg.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (muted.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(plugin.getPrefix() + "§cYou are Muted!");
            event.setCancelled(true);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            ArrayList<String> reason = new ArrayList<>();
            Arrays.asList(MuteReason.values()).forEach(reasons -> reason.add(reasons.name()));
            ArrayList<String> empty = new ArrayList<>();
            for (String s : reason) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase()))
                    empty.add(s);
            }
            Collections.sort(empty);
            return empty;
        }
        if (args.length == 3) {
            return new ArrayList<String>(Collections.singletonList("Time"));
        }
        if (args.length == 4) {
            ArrayList<String> dateFormat = new ArrayList<>();
            Arrays.asList(DateUnit.values()).forEach(dateUnit -> dateFormat.add(dateUnit.name()));
            ArrayList<String> empty = new ArrayList<>();
            for (String s : dateFormat) {
                if (s.toLowerCase().startsWith(args[3].toLowerCase())) {
                    empty.add(s);
                }
            }
            Collections.sort(empty);
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }

    public static enum MuteReason {
        advertising("advertising"),
        caps("caps");

        private final String reason;

        MuteReason(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }
}
