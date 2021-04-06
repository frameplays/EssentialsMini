package de.framedev.essentialsmin.utils;

import de.framedev.essentialsmin.main.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class Config {
    public static void loadConfig() {
        Main.getInstance().getConfig().options().copyHeader(true);
        Main.getInstance().getConfig().getDefaults().options().copyDefaults(true);
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().saveDefaultConfig();
    }

    public static void updateConfig() {
        try {
            if ((new File(Main.getInstance().getDataFolder() + "/config.yml")).exists()) {
                boolean changesMade = false;
                YamlConfiguration tmp = new YamlConfiguration();
                tmp.load(Main.getInstance().getDataFolder() + "/config.yml");
                for (String str : Main.getInstance().getConfig().getKeys(true)) {
                    if (!tmp.getKeys(true).contains(str)) {
                        tmp.set(str, Main.getInstance().getConfig().get(str));
                        changesMade = true;
                        tmp.save(Main.getInstance().getDataFolder() + "/config.yml");
                        tmp.load(Main.getInstance().getDataFolder() + "/config.yml");
                    }
                }
                if (changesMade) {

                    tmp.save(Main.getInstance().getDataFolder() + "/config.yml");
                    tmp.load(Main.getInstance().getDataFolder() + "/config.yml");
                }
            }
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void saveDefaultConfigValues() {
        File file = new File(Main.getInstance().getDataFolder() + "config.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        //Defaults in jar
        Reader defConfigStream = null;
        defConfigStream = new InputStreamReader(Main.getInstance().getResource("config.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            cfg.setDefaults(defConfig);
            //Copy default values
            cfg.options().copyDefaults(true);
            Main.getInstance().saveConfig();
            //OR use this to copy default values
            //this.saveDefaultConfig();
        }
    }
}


