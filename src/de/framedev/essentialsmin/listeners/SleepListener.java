package de.framedev.essentialsmin.listeners;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 10.08.2020 12:41
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.BackendManager;
import de.framedev.essentialsmin.managers.PlayerManager;
import de.framedev.essentialsmin.managers.PlayerManagerCfgLoss;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.io.FileNotFoundException;

public class SleepListener implements Listener {

    private final Main plugin;

    public SleepListener(Main plugin) {
        this.plugin = plugin;
        plugin.getListeners().add(this);
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (plugin.getConfig().getBoolean("SkipNight")) {
            if (event.getPlayer().getWorld().getTime() >= 12542 && event.getPlayer().getWorld().getTime() <= 23460 || event.getPlayer().getWorld().isThundering()) {
                Bukkit.broadcastMessage("§6" + event.getPlayer().getName() + " §ahat die Nacht übersprungen!");
                event.getPlayer().getWorld().setTime(0);
                event.getPlayer().getWorld().setThundering(false);
                event.getPlayer().getWorld().setStorm(false);
                if(plugin.isMongoDb()) {
                    int sleepTimes = (int) plugin.getBackendManager().get(event.getPlayer(),BackendManager.DATA.SLEEPTIMES.getName(),"test");
                    sleepTimes++;
                    plugin.getBackendManager().updateUser(event.getPlayer(), BackendManager.DATA.SLEEPTIMES.getName(),sleepTimes,"test");
                }
                if(plugin.getConfig().getBoolean("JsonFormat")) {
                    try {
                        PlayerManagerCfgLoss.getPlayerManagerCfgLoss(event.getPlayer()).setSleepTimes(PlayerManagerCfgLoss.getPlayerManagerCfgLoss(event.getPlayer()).getSleepTimes() + 1);
                    } catch (FileNotFoundException ignored) {

                    }
                } else {
                    new PlayerManager(event.getPlayer()).addSleepTimes();
                }
                event.setCancelled(true);
            }
        }
    }
}
