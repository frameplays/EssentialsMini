package de.framedev.essentialsmin.managers;

import com.google.gson.GsonBuilder;
import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.utils.NotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.naming.spi.ObjectFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 03.07.2020 20:00
 */
public class LocationsManager {

    private String name;
    private final File fileBackup = new File(Main.getInstance().getDataFolder(), "locationsBackup.yml");
    private final File fileBackupJson = new File(Main.getInstance().getDataFolder(), "locationsBackupJson.json");
    private final File file = new File(Main.getInstance().getDataFolder(), "locations.yml");
    private final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    private final FileConfiguration cfgBackup = YamlConfiguration.loadConfiguration(fileBackup);

    private final Main instance = Main.getInstance();

    public LocationsManager(String name) {
        this.name = name;
    }

    public LocationsManager() {
    }

    public void saveCfg() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCfg() {
        return cfg;
    }

    public void setLocation(String name, Location location) {
        cfg.set(name + ".world", location.getWorld().getName());
        cfg.set(name + ".x", location.getX());
        cfg.set(name + ".y", location.getY());
        cfg.set(name + ".z", location.getZ());
        cfg.set(name + ".yaw", location.getYaw());
        cfg.set(name + ".pitch", location.getPitch());
        cfg.set(name + ".createdAt", System.currentTimeMillis());
        saveCfg();
    }

    public void setLocation(Location location) {
        cfg.set(name + ".world", location.getWorld().getName());
        cfg.set(name + ".x", location.getX());
        cfg.set(name + ".y", location.getY());
        cfg.set(name + ".z", location.getZ());
        cfg.set(name + ".yaw", location.getYaw());
        cfg.set(name + ".pitch", location.getPitch());
        cfg.set(name + ".createdAt", System.currentTimeMillis());
        saveCfg();
    }

    public String getCreatedAt(String name) {
        if (cfg.contains(name + ".createdAt")) {
            Date date = new Date(cfg.getLong(name + ".createdAt"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            String time = simpleDateFormat.format(date);
            return time;
        }
        return "";
    }

    public Location getLocation(String s) throws NullPointerException, IllegalArgumentException {
        try {
            World world = Bukkit.getWorld(cfg.getString(s + ".world"));
            double x = cfg.getDouble(s + ".x");
            double y = cfg.getDouble(s + ".y");
            double z = cfg.getDouble(s + ".z");
            float yaw = cfg.getInt(s + ".yaw");
            float pitch = cfg.getInt(s + ".pitch");
            if (world != null) {

            } else {
                System.out.println("world is null");
                return null;
            }
            Location location = new Location(world, x, y, z, yaw, pitch);
            if (location != null) {
                return location;
            }

            return null;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public void setWarp(String warpName, Location location) {
        cfg.set("warps." + warpName, locationToString(location));
        saveCfg();
    }

    public Location getWarp(String warpName) throws NotFoundException {
        if (cfg.contains("warps." + warpName)) {
            return locationFromString(cfg.getString("warps." + warpName));
        } else {
            throw new NotFoundException("This Location cannot be found!");
        }
    }

    public FileConfiguration getCfgBackup() {
        return cfgBackup;
    }

    public File getFileBackup() {
        return fileBackup;
    }

    public File getFile() {
        return file;
    }

    public Location getLocation() throws NullPointerException, IllegalArgumentException {
        try {
            World world = Bukkit.getWorld(cfg.getString(name + ".world"));
            double x = cfg.getDouble(name + ".x");
            double y = cfg.getDouble(name + ".y");
            double z = cfg.getDouble(name + ".z");
            float yaw = cfg.getInt(name + ".yaw");
            float pitch = cfg.getInt(name + ".pitch");
            if (world != null) {

            } else {
                throw new NullPointerException("World is Null");
            }
            Location location = new Location(world, x, y, z, yaw, pitch);
            if (location != null) {
                return location;
            }

            throw new NullPointerException("Location is Null");
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String locationToString(Location location) {
        String s = "";
        if (location.getWorld() == null) {
            return null;
        }
        s += location.getWorld().getName() + ";";
        s += location.getX() + ";";
        s += location.getY() + ";";
        s += location.getZ() + ";";
        s += location.getYaw() + ";";
        s += location.getPitch();
        /* World;10;63;12;160;-24 */
        return s;
    }

    public Location locationFromString(String string) {
        String[] s = string.split(";");
        return new Location(Bukkit.getWorld(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]), Double.parseDouble(s[3]), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
    }

    public void saveBackupCfg() {
        try {
            cfgBackup.save(fileBackup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> backups = new HashMap<>();

    public void saveBackup() {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            String ss = offlinePlayer.getName();
            if (ss != null) {
                ConfigurationSection cs = getCfg().getConfigurationSection(ss + ".home");
                if (cs != null) {
                    for (String s : cs.getKeys(false)) {
                        if (getCfg().get(ss + ".home." + s) != null && !getCfg().get(ss + ".home." + s).equals(" ")) {
                            cfgBackup.set(ss + ".home." + s, locationToString(getLocation(ss + ".home." + s)));
                            backups.put(ss + ".home." + s,locationToString(getLocation(ss + ".home." + s)));
                        }
                    }
                }
            }
        }
        saveBackupCfg();
        saveBackUps();
    }

    public void deleteLocations() {
        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("position");
        if (cs != null) {
            for (String s : cs.getKeys(false)) {
                if (s != null) {
                    getCfg().set("position." + s, " ");
                    try {
                        getCfg().save(getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void saveBackUps() {
        try {
            FileWriter fileWriter = new FileWriter(fileBackupJson);
            fileWriter.write(new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(backups));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
