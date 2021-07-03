package de.framedev.essentialsmini.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BanFile {

    public static File file = new File("plugins/SpigotTest/Banned.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static void saveCFG(String playername) {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned(String playername) {
        return cfg.getBoolean(playername + ".isBanned");
    }

    public static String getBannedReason(String playername) {
        if (cfg.getBoolean(playername + ".isBanned")) {
            return cfg.getString(playername + ".reason");
        }
        return "";
    }

    public static void banPlayer(String playername, String reason) {
        if (cfg.getBoolean(playername + ".isBanned")) {
            Bukkit.getConsoleSender().sendMessage(playername + " ist schon gebannt!");
        } else {
            cfg.set(playername + ".isBanned", true);
            cfg.set(playername + ".reason", reason);
            saveCFG(playername);
            if (!file.exists()) {
                try {
                    file.mkdir();
                } catch (Exception ignored) {

                }

            }
        }
    }

    public static void unBanPlayer(String playername) {
        if (!cfg.getBoolean(playername + ".isBanned")) {
            Bukkit.getConsoleSender().sendMessage(playername + " ist nicht gebannt!");
        } else {
            cfg.set(playername + ".isBanned", false);
            saveCFG(playername);
        }
    }

}
