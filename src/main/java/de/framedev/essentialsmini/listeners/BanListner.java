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
            if(new BanMuteManager().isTempBan(Bukkit.getOfflinePlayer(e.getName()))) {
                new BanMuteManager().getTempBan(Bukkit.getOfflinePlayer(e.getName())).forEach((s, s2) -> {Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(e.getName(), "§aYou are Banned. Reason:§c " + s2, new Date(s), "true");});
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                e.setKickMessage("§cYou are Banned");
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
