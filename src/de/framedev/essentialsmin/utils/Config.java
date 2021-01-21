package de.framedev.essentialsmin.utils;

import de.framedev.essentialsmin.main.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


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
}


