package de.framedev.essentialsmin.managers;

import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 25.07.2020 22:51
 */
public class PlayerManager implements Serializable {

    private File file;
    FileConfiguration cfg;
    private String name;
    private UUID uuid;
    private OfflinePlayer player;
    private Long lastLogin;
    private Long lastLogout;
    private double damage;
    private int kills;
    private int entityKills;
    private int deaths;
    private int sleepTimes;
    private int blockBroken;
    private int blockPlace;
    private ArrayList<String> blocksBroken = new ArrayList<>();
    private ArrayList<String> blocksPlace = new ArrayList<>();
    private ArrayList<String> entityTypes = new ArrayList<>();
    private int commandsUsed;

    public PlayerManager(UUID uuid) {
        this.uuid = uuid;
        file = new File(Main.getInstance().getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                cfg.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        cfg.set("Name", Bukkit.getOfflinePlayer(uuid).getName());
        cfg.set("UUID", uuid.toString());
        this.player = Bukkit.getOfflinePlayer(uuid);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerManager(OfflinePlayer player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        file = new File(Main.getInstance().getDataFolder() + "/PlayerData", uuid.toString() + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                cfg.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        cfg.set("Name", name);
        cfg.set("UUID", uuid.toString());
        this.player = player;
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public PlayerManager(String name) {
        this.name = name;
        this.uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
        file = new File(Main.getInstance().getDataFolder() + "/PlayerData", Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            try {
                cfg.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        this.player = Bukkit.getOfflinePlayer(uuid);
        cfg.set("Name", name);
        cfg.set("UUID", uuid.toString());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean kill() {
        if(player.isOnline()) {
            Player onPlayer = (Player) player;
            onPlayer.setHealth(0);
            return true;
        }
        return false;
    }


    public void setStatistic(Statistic statistic, int i) {
        player.setStatistic(statistic, i);
    }

    public void setStatistic(Statistic statistic, EntityType type, int i) {
        player.setStatistic(statistic, type, i);
    }

    public void setStatistic(Statistic statistic, Material material, int i) {
        player.setStatistic(statistic, material, i);
    }


    public void setEntityTypes(ArrayList<String> entityTypes) {
        this.entityTypes = entityTypes;
        cfg.set("EntityTypesKilled",entityTypes);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getEntityTypes() {
        if(cfg.contains("EntityTypesKilled")) {
            this.entityTypes = (ArrayList<String>) cfg.getStringList("EntityTypesKilled");
            return (ArrayList<String>) cfg.getStringList("EntityTypesKilled");
        }
        return entityTypes;
    }

    public void addEntityType(EntityType entityType) {
        for(String type : getEntityTypes()) {
            if(!type.equalsIgnoreCase(entityType.name())) {
                entityTypes.add(entityType.name());
            }
        }
        setEntityTypes(entityTypes);
    }

    public int getCommandsUsed() {
        if(cfg.contains("CommandsUsed") && cfg.isInt("CommandsUsed")) {
            return cfg.getInt("CommandsUsed");
        }
        return commandsUsed;
    }

    public void setCommandsUsed(int commandsUsed) {
        this.commandsUsed = commandsUsed;
        cfg.set("CommandsUsed",commandsUsed);
        saveConfig();
    }

    public void addCommandsUsed() {
        int commands = getCommandsUsed();
        commands++;
        setCommandsUsed(commands);
    }

    public ArrayList<String> getBlocksPlace() {
        return blocksPlace;
    }

    public void setBlocksPlace(ArrayList<String> blocksPlace) {
        this.blocksPlace = blocksPlace;
        cfg.set("BlocksPlace",blocksPlace);
        saveConfig();
    }

    public ArrayList<String> getBlocksBroken() {
        return blocksBroken;
    }

    public void addBlocksPlace(Material material) {
        if(getBlocksPlace() != null) {
            if(!getBlocksPlace().contains(material.name())) {
                getBlocksPlace().add(material.name());
                setBlocksPlace(getBlocksPlace());
            }
        } else {
            ArrayList<String> blocksBrocken = new ArrayList<>();
            blocksBrocken.add(material.name());
            setBlocksPlace(blocksBrocken);
        }
    }

    public void setBlocksBroken(ArrayList<String> blocksBroken) {
        this.blocksBroken = blocksBroken;
        cfg.set("BlocksBroken",blocksBroken);
        saveConfig();
    }

    public void addBlocksBroken(Material material) {
        if(getBlocksBroken() != null) {
            if(!getBlocksBroken().contains(material.name())) {
                getBlocksBroken().add(material.name());
                setBlocksBroken(getBlocksBroken());
            }
        } else {
            ArrayList<String> blocksBrocken = new ArrayList<>();
            blocksBrocken.add(material.name());
            setBlocksBroken(blocksBrocken);
        }
    }

    public int getBlockPlace() {
        if(cfg.contains("BlockPlace") && cfg.isInt("BlockPlace")) {
            return cfg.getInt("BlockPlace");
        }
        return blockPlace;
    }

    public void setBlockPlace(int blockPlace) {
        this.blockPlace = blockPlace;
        cfg.set("BlockPlace",blockPlace);
        saveConfig();
    }

    public void addBlockPlace() {
        int blockPlace = getBlockPlace();
        blockPlace++;
        setBlockPlace(blockPlace);
    }

    public int getBlockBroken() {
        if(cfg.contains("BlockBroken") && cfg.isInt("BlockBroken")) {
            return cfg.getInt("BlockBroken");
        }
        return blockBroken;
    }

    public void setBlockBroken(int blockBroken) {
        this.blockBroken = blockBroken;
        cfg.set("BlockBroken",blockBroken);
        saveConfig();
    }
    public void addBlockBroken() {
        int blockBroken = getBlockBroken();
        blockBroken++;
        setBlockBroken(blockBroken);
    }

    public int getSleepTimes() {
        if(cfg.contains("SleepTimes") && cfg.isInt("SleepTimes")) {
            return cfg.getInt("SleepTimes");
        }
        return sleepTimes;
    }

    public void setSleepTimes(int sleepTimes) {
        this.sleepTimes = sleepTimes;
        cfg.set("SleepTimes",sleepTimes);
        saveConfig();
    }

    public void addSleepTimes() {
        int sleep = getSleepTimes();
        sleep++;
        setSleepTimes(sleep);
    }

    private double round(double value, int decimalPoints) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(decimalPoints);
        return Double.parseDouble(formatter.format(value));
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return cfg.getString("Name");
        }
    }

    public UUID getUuid() {
        if (uuid != null) {
            return uuid;
        } else {
            return UUID.fromString(Objects.requireNonNull(cfg.getString("UUID")));
        }
    }

    public void setLastLogin(Long lastlogin) {
        this.lastLogin = lastlogin;
        cfg.set("LastLogin", lastlogin);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLastLogout(Long lastLogout) {
        this.lastLogout = lastLogout;
        cfg.set("LastLogout", lastLogout);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getLastLogin() {
        if (cfg.contains("LastLogin") && cfg.isLong("LastLogin")) {
            return cfg.getLong("LastLogin");
        } else {
            if (lastLogin != null) {
                return lastLogin;
            } else {
                return 0L;
            }
        }
    }

    public Long getLastLogout() {
        if (cfg.contains("LastLogout") && cfg.isLong("LastLogout")) {
            return cfg.getLong("LastLogout");
        } else {
            if (lastLogin != null) {
                return lastLogout;
            } else {
                return 0L;
            }
        }
    }

    public void saveTimePlayed() {
        cfg.set("TimePlayed", getTimePlayed());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public String getHoursPlayed() {
        long ticks = Bukkit.getOfflinePlayer(uuid).getStatistic(Statistic.PLAY_ONE_MINUTE);
        long seconds = ticks * 20;
        Date date = new Date(seconds);
        String time = new SimpleDateFormat("HH").format(date);
        return time;
    }

    @Deprecated
    public String getTimePlayed() {
        long ticks = Bukkit.getOfflinePlayer(uuid).getStatistic(Statistic.PLAY_ONE_MINUTE);
        long seconds = ticks * 20;
        Date date = new Date(seconds);
        String time = new SimpleDateFormat("D | HH:mm:ss").format(date);
        return time;
    }

    public void setDamage(double damage) {
        this.damage = damage;
        cfg.set("Damage", damage);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDamage(double amount) {
        double damage = getDamage();
        damage = damage + amount;
        setDamage(damage);
    }

    public double getDamage() {
        if (cfg.contains("Damage") && cfg.isDouble("Damage")) {
            return round(cfg.getDouble("Damage"), 3);
        } else {
            return damage;
        }
    }

    public void setPlayerKills(int kills) {
        this.kills = kills;
        cfg.set("PlayerKills", kills);
        if (!saveConfig()) {
            System.err.println("Cannot Save PlayerData");
        }
    }

    public int getPlayerKills() {
        if (cfg.contains("PlayerKills") || cfg.isInt("PlayerKills")) {
            return cfg.getInt("PlayerKills");
        }
        return kills;
    }

    public void addPlayerKill() {
        int kill = getPlayerKills();
        kill++;
        setPlayerKills(kill);
    }

    public void setEntityKills(int entityKills) {
        this.entityKills = entityKills;
        cfg.set("EntityKills", entityKills);
        if (!saveConfig()) {
            System.err.println("Cannot Save PlayerData");
        }
    }

    public int getEntityKills() {
        if (cfg.contains("EntityKills") || cfg.isInt("EntityKills")) {
            return cfg.getInt("EntityKills");
        }
        return entityKills;
    }

    public void addEntityKill() {
        int entityKills = getEntityKills();
        entityKills++;
        setEntityKills(entityKills);
    }

    public void setDeath(int death) {
        this.deaths = death;
        cfg.set("Deaths", death);
        if (!saveConfig()) {
            System.err.println("Cannot Save PlayerData");
        }
    }

    public int getDeaths() {
        if (cfg.contains("Deaths") || cfg.isInt("Deaths")) {
            return cfg.getInt("Deaths");
        }
        return deaths;
    }

    public void addDeath() {
        int deaths = getDeaths();
        deaths++;
        setDeath(deaths);
    }

    private boolean saveConfig() {
        try {
            cfg.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
