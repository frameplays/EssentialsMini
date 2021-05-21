package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
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
import java.util.ArrayList;
import java.util.Date;

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
        this.plugin = plugin;
        setup(this);
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
                if (!sender.hasPermission(plugin.getPermissionName() + "mute")) {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                    return true;
                }

                MuteReason muteReason = MuteReason.valueOf(args[1].toUpperCase());
                DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                long value = Long.parseLong(args[2]);
                long current = System.currentTimeMillis();
                long millis = value * unit.getToSec() * 1000;
                long newValue = current + millis;
                Date date = new Date(newValue);
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
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
                return true;
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    private boolean isExpired(Player player) {
        if (cfg.contains(player.getName())) {
            Date date = (Date) cfg.get(player.getName() + ".expire");
            if (date != null)
                return date.getTime() < System.currentTimeMillis();
        }
        return true;
    }

    @EventHandler
    public void onChatWrite(AsyncPlayerChatEvent event) {
        if(!isExpired(event.getPlayer())) {
            event.getPlayer().sendMessage(plugin.getPrefix() + "§cYou are Muted! While §6" + cfg.getString(event.getPlayer().getName() + ".reason"));
            event.setCancelled(true);
        }
        if (muted.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(plugin.getPrefix() + "§cYou are Muted!");
            event.setCancelled(true);
        }
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
