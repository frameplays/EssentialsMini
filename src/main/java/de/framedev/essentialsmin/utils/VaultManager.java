package de.framedev.essentialsmin.utils;

import de.framedev.essentialsmin.api.VaultAPI;
import de.framedev.essentialsmin.main.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VaultManager {

    private final Economy eco;
    public VaultManager(Main plugin) {
        File filedata = new File(Main.getInstance().getDataFolder() + "/money","eco.yml");
        FileConfiguration cfgdata = YamlConfiguration.loadConfiguration(filedata);
        if(!filedata.exists()) {
            filedata.getParentFile().mkdirs();
            try {
                filedata.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Bukkit.getServer().getOnlineMode()) {
            if(!cfgdata.contains("accounts")) {
                ArrayList<String> accounts = new ArrayList<>();
                accounts.add("14555508-6819-4434-aa6a-e5ce1509ea35");
                cfgdata.set("accounts",accounts);
                try {
                    cfgdata.save(filedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if(!cfgdata.contains("accounts")) {
                ArrayList<String> accounts = new ArrayList<>();
                accounts.add("sambakuchen");
                cfgdata.set("accounts",accounts);
                try {
                    cfgdata.save(filedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        plugin.getServer().getServicesManager().register(Economy.class,new VaultAPI(),plugin, ServicePriority.High);
        eco = new VaultAPI();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!eco.hasAccount(p.getName()))
                eco.createPlayerAccount(p.getName());
        }
    }

    public Economy getEco() {
        return eco;
    }
}
