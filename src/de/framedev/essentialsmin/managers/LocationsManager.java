package de.framedev.essentialsmin.managers;

import com.google.gson.Gson;
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private boolean jsonFormat;

    public LocationsManager(String name) {
        this.jsonFormat = Main.getInstance().getConfig().getBoolean("JsonFormat");
        jsonFormat = false;
        this.name = name;
    }

    public LocationsManager() {
        this.jsonFormat = Main.getInstance().getConfig().getBoolean("JsonFormat");
        jsonFormat = false;
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
        if(jsonFormat) {
            List<LocationJson> locs = getLocations();
            List<LocationJson> updated = new ArrayList<>(getLocations());
            final boolean[] success = {false};
            locs.forEach(locationJson -> {
                if(locationJson.getLocationName().equalsIgnoreCase(name)) {
                    updated.remove(locationJson);
                    updated.add(new LocationJson(name,location));
                    success[0] = true;
                }
            });
            if(!success[0])
                updated.add(new LocationJson(name,location));
            saveLocations(updated);
        } else {
            cfg.set(name + ".world", location.getWorld().getName());
            cfg.set(name + ".x", location.getX());
            cfg.set(name + ".y", location.getY());
            cfg.set(name + ".z", location.getZ());
            cfg.set(name + ".yaw", location.getYaw());
            cfg.set(name + ".pitch", location.getPitch());
            cfg.set(name + ".createdAt", System.currentTimeMillis());
            saveCfg();
        }
    }

    public void setLocation(Location location) {
        if(jsonFormat) {
            List<LocationJson> locs = getLocations();
            List<LocationJson> updated = new ArrayList<>(getLocations());
            final boolean[] success = {false};
            locs.forEach(locationJson -> {
                if(locationJson.getLocationName().equalsIgnoreCase(name)) {
                    updated.remove(locationJson);
                    updated.add(new LocationJson(name,location));
                    success[0] = true;
                }
            });
            if(!success[0])
                updated.add(new LocationJson(name,location));
            saveLocations(updated);
        } else {
            cfg.set(name + ".world", location.getWorld().getName());
            cfg.set(name + ".x", location.getX());
            cfg.set(name + ".y", location.getY());
            cfg.set(name + ".z", location.getZ());
            cfg.set(name + ".yaw", location.getYaw());
            cfg.set(name + ".pitch", location.getPitch());
            cfg.set(name + ".createdAt", System.currentTimeMillis());
            saveCfg();
        }
    }

    public void setLocation(String name, LocationJson loc) {
        this.name = name;
        if(jsonFormat) {
            List<LocationJson> locs = getLocations();
            List<LocationJson> updated = new ArrayList<>(getLocations());
            final boolean[] success = {false};
            locs.forEach(locationJson -> {
                if(locationJson.getLocationName().equalsIgnoreCase(name)) {
                    updated.remove(locationJson);
                    success[0] = true;
                }
            });
            if(!success[0]) {
                System.out.println("not");
            }
            saveLocations(updated);
        } else {
            cfg.set(name + ".world", loc.getWorldName());
            cfg.set(name + ".x", loc.getX());
            cfg.set(name + ".y", loc.getY());
            cfg.set(name + ".z", loc.getZ());
            cfg.set(name + ".yaw", loc.getYaw());
            cfg.set(name + ".pitch", loc.getPitch());
            cfg.set(name + ".createdAt", System.currentTimeMillis());
            saveCfg();
        }
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

    public void saveLocations(List<LocationJson> locs) {
        File file = new File(Main.getInstance().getDataFolder(),"locations.json");
        try {
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(locs));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LocationJson> getLocations() {
        File file = new File(Main.getInstance().getDataFolder(),"locations.json");
        LocationJson[] locs = new LocationJson[0];
        try {
            if(file.exists()) {
                FileReader reader = new FileReader(file);
                locs = new Gson().fromJson(reader,LocationJson[].class);
                reader.close();
            }
        } catch (Exception ignored) {

        }
        return Arrays.asList(locs);
    }

    public Location getLocation(String s) throws NullPointerException {
        if(jsonFormat) {
            LocationJson locationJson = null;
            for (LocationJson location : getLocations()) {
                if(location.getLocationName().equalsIgnoreCase(s))
                    locationJson = location;
            }
            if(locationJson == null)
                return null;
            return locationJson.getLocation();
        } else {
            if(cfg.contains(s)) {
                try {
                    World world = Bukkit.getWorld(cfg.getString(s + ".world"));
                    double x = cfg.getDouble(s + ".x");
                    double y = cfg.getDouble(s + ".y");
                    double z = cfg.getDouble(s + ".z");
                    float yaw = cfg.getInt(s + ".yaw");
                    float pitch = cfg.getInt(s + ".pitch");
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

    public Location getLocation() {
        if(jsonFormat) {
            LocationJson locationJson = null;
            for (LocationJson location : getLocations()) {
                if(location.getLocationName().equalsIgnoreCase(name))
                    locationJson = location;
            }
            if(locationJson == null)
                return null;
            return locationJson.getLocation();
        } else {
            if(cfg.contains(name)) {
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

    private final HashMap<String, String> backups = new HashMap<>();

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

                            /* Test */
                            new Locations(ss + ".home." + s,getLocation(ss + ".home." + s)).setLocation();
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

    public static class LocationJson {

        private String locationName, worldName;
        private double x,y,z;
        private float yaw, pitch;

        public LocationJson(String name, Location location) {
            this.locationName = name;
            this.worldName = location.getWorld().getName();
            this.x = location.getX();
            this.y = location.getY();
            this.z = location.getZ();
            this.yaw = location.getYaw();
            this.pitch = location.getPitch();
        }

        public LocationJson() {
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getWorldName() {
            return worldName;
        }

        public void setWorldName(String worldName) {
            this.worldName = worldName;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public float getYaw() {
            return yaw;
        }

        public void setYaw(float yaw) {
            this.yaw = yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public Location getLocation() {
            return new Location(Bukkit.getWorld(worldName),x,y,z,yaw,pitch);
        }
    }
}
