package de.framedev.essentialsmini.managers;

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BanFile {

    public static File file = new File(Main.getInstance().getDataFolder(), "Banned.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static void saveCFG() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned(String playername) {
        return cfg.getBoolean("Ban." + playername + ".isBanned");
    }

    public static String getBannedReason(String playername) {
        if (cfg.getBoolean("Ban." + playername + ".isBanned")) {
            return cfg.getString("Ban." + playername + ".reason");
        }
        return "";
    }

    public static void banPlayer(String playername, String reason) {
        if (cfg.getBoolean("Ban." + playername + ".isBanned")) {
            Bukkit.getConsoleSender().sendMessage(playername + " ist schon gebannt!");
        } else {
            cfg.set("Ban." + playername + ".isBanned", true);
            cfg.set("Ban." + playername + ".reason", reason);
            saveCFG();
            if (!file.exists()) {
                try {
                    if (!file.mkdir())
                        System.err.println("File cannot be created!");
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static void unBanPlayer(String playername) {
        if (!cfg.getBoolean("Ban." + playername + ".isBanned")) {
            Bukkit.getConsoleSender().sendMessage("Ban." + playername + " ist nicht gebannt!");
        } else {
            cfg.set("Ban." + playername + ".isBanned", false);
            saveCFG();
        }
    }

    public List<String> getAllBannedPlayers() {
        return cfg.getStringList("Ban");
    }

}
