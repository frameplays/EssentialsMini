package de.framedev.essentialsmini.listeners;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanFile;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.ListenerBase;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class BanListner extends ListenerBase {

    public BanListner(Main plugin) {
        super(plugin);
        setupListener(this);
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        if(getPlugin().isMysql() || getPlugin().isSQL()) {
            if(!new BanMuteManager().isExpiredTempBan(Bukkit.getOfflinePlayer(e.getName()))) {
                final String[] reason = new String[1];
                if(new BanMuteManager().getTempBan(Bukkit.getOfflinePlayer(e.getName())) != null) {
                    new BanMuteManager().getTempBan(Bukkit.getOfflinePlayer(e.getName())).forEach((s, s2) -> {
                        try {
                            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(e.getName(), "§aYou are Banned. Reason:§c " + s2, new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss").parse(s), "true");
                            reason[0] = "§aYou are Banned. Reason:§c " + s2 + " §aExpired at §6: " + s;
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    });
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                    e.setKickMessage(reason[0]);
                }
            } else {
                new BanMuteManager().removeTempBan(Bukkit.getOfflinePlayer(e.getName()));
            }
        }
        if(getPlugin().isMysql() || getPlugin().isSQL()) {
            if(new BanMuteManager().isPermaBan(Bukkit.getOfflinePlayer(e.getName()))) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + new BanMuteManager().getPermaBanReason(Bukkit.getOfflinePlayer(e.getName())));
            }
        } else {
            if (BanFile.cfg.getBoolean(e.getName() + ".isBanned")) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + BanFile.cfg.getString(e.getName() + ".reason"));
            }
        }
    }
}
