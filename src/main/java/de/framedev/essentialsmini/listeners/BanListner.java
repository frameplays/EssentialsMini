package de.framedev.essentialsmini.listeners;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanFile;
import de.framedev.essentialsmini.managers.ListenerBase;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;


public class BanListner extends ListenerBase {

    public BanListner(Main plugin) {
        super(plugin);
        setupListener(this);
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        if (BanFile.cfg.getBoolean(e.getPlayer().getName() + ".isBanned")) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + BanFile.cfg.getString(e.getPlayer().getName() + ".reason"));
        }
    }
}
