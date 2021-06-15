package de.framedev.essentialsmin.managers;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 15.08.2020 13:48
 */

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class UpdateScheduler {

    public void run() {
        if(Main.getInstance().getConfig().getBoolean("LocationsBackup")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Main.getInstance().getConfig().getBoolean("LocationsBackup")) {
                        new LocationsManager().saveBackup();
                        //Main.getInstance().savePlayerHomes();
                        if (Main.getInstance().getConfig().getBoolean("LocationsBackupMessage")) {
                            Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§a" + new LocationsManager().getFileBackup().getName() + " §6LocationBackup gespeichert!");
                            Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§aDas Backup befindet sich in §6" + new LocationsManager().getFileBackup().getPath());
                        }
                    }
                    Main.getInstance().getCfgLossHashMap().forEach((player, playerManagerCfgLoss) -> {
                        if (playerManagerCfgLoss.getName().equalsIgnoreCase(player.getName())) {
                            playerManagerCfgLoss.savePlayerManager();
                        }
                    });
                    if (Main.getInstance().getConfig().getBoolean("BackupMessages")) {
                        Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§6User Data §aSaved!");
                    }
                }
            }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20 * 60 * Main.getInstance().getConfig().getInt("BackupTime"));
        }
    }
}
