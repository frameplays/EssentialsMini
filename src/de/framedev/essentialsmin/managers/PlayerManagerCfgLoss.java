package de.framedev.essentialsmin.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 25.07.2020 22:51
 */
public class PlayerManagerCfgLoss implements Serializable {

    private String name;
    private UUID uuid;
    @SerializedName("LastLogin")
    @Expose
    private long lastlogin;
    @SerializedName("LastLogout")
    @Expose
    private long lastLogout;
    private double damage;
    private int kills;
    private int entityKills;
    private int deaths;
    private String timePlayed;
    private int sleepTimes;
    private int blockBroken;
    private int blockPlace;
    @SerializedName("CreatedAt")
    private String createdAt;
    private ArrayList<Material> blocksBroken = new ArrayList<>();
    private ArrayList<Material> blocksPlace = new ArrayList<>();
    private ArrayList<EntityType> entityTypes = new ArrayList<>();
    private int commandsUsed;

    public PlayerManagerCfgLoss(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.lastlogin = 0L;
        this.lastLogout = 0L;
        this.createdAt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date());
    }

    public PlayerManagerCfgLoss(OfflinePlayer player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.lastlogin = 0L;
        this.lastLogout = 0L;
        this.createdAt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date());
    }

    @Deprecated
    public PlayerManagerCfgLoss(String name) {
        this.name = name;
        this.uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
        this.lastlogin = 0L;
        this.lastLogout = 0L;
        this.createdAt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date());
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getCommandsUsed() {
        return commandsUsed;
    }

    public void setCommandsUsed(int commandsUsed) {
        this.commandsUsed = commandsUsed;
    }

    public void addCommandsUsed() {
        int commands = getCommandsUsed();
        commands++;
        setCommandsUsed(commands);
    }

    public ArrayList<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(ArrayList<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public void addEntityType(EntityType entityType) {
        if (entityTypes != null) {
            if (!entityTypes.contains(entityType)) {
                entityTypes.add(entityType);
                setEntityTypes(entityTypes);
            }
        } else {
            ArrayList<EntityType> entityTypess = new ArrayList<>();
            entityTypess.add(entityType);
            setEntityTypes(entityTypess);
        }
    }

    public ArrayList<Material> getBlocksPlace() {
        return blocksPlace;
    }

    public void setBlocksPlace(ArrayList<Material> blocksPlace) {
        this.blocksPlace = blocksPlace;
    }

    public void addBlocksPlace(Material material) {
        if (blocksPlace != null) {
            if (!blocksPlace.contains(material)) {
                blocksPlace.add(material);
                setBlocksPlace(blocksPlace);
            }
        } else {
            ArrayList<Material> blocksPlaces = new ArrayList<>();
            blocksPlaces.add(material);
            setBlocksPlace(blocksPlaces);
        }
    }

    public ArrayList<Material> getBlocksBroken() {
        return blocksBroken;
    }

    public void setBlocksBroken(ArrayList<Material> blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public void addBlockBrokensMaterial(Material material) {
        if (blocksBroken != null) {
            if (!blocksBroken.contains(material)) {
                blocksBroken.add(material);
                setBlocksBroken(blocksBroken);
            }
        } else {
            ArrayList<Material> blocksPlaces = new ArrayList<>();
            blocksPlaces.add(material);
            setBlocksBroken(blocksPlaces);
        }
    }

    public int getBlockPlace() {
        return blockPlace;
    }

    public void setBlockPlace(int blockPlace) {
        this.blockPlace = blockPlace;
    }

    public void addBlockPlace() {
        int blockPlace = getBlockPlace();
        blockPlace++;
        setBlockPlace(blockPlace);
    }

    public int getBlockBroken() {
        return blockBroken;
    }

    public void setBlockBroken(int blockBroken) {
        this.blockBroken = blockBroken;
    }

    public void addBlockBroken() {
        int blockbroken = getBlockBroken();
        blockbroken++;
        setBlockBroken(blockbroken);
    }

    private double round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }

    public int getSleepTimes() {
        return sleepTimes;
    }

    public void setSleepTimes(int sleepTimes) {
        this.sleepTimes = sleepTimes;

    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setLastLogin(long lastlogin) {
        this.lastlogin = lastlogin;
        setLastlogin(lastlogin);
    }

    public long getLastlogin() {
        return lastlogin;
    }

    public void setLastLogout(long lastLogout) {
        this.lastLogout = lastLogout;
    }

    public long getLastLogout() {
        return lastLogout;
    }

    public void saveTimePlayed() {
        this.timePlayed = getTimePlayed();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setLastlogin(long lastlogin) {
        this.lastlogin = lastlogin;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setTimePlayed(String timePlayed) {
        this.timePlayed = timePlayed;
        this.timePlayed = getTimePlayed();
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
        this.timePlayed = time;
        return timePlayed;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void addDamage(double amount) {
        double damage = getDamage();
        damage = damage + amount;
        setDamage(damage);
    }

    public double getDamage() {
        return round(damage, 3);
    }

    public void setPlayerKills(int kills) {
        this.kills = kills;
    }

    public int getPlayerKills() {
        return kills;
    }

    public void addPlayerKill() {
        int kill = getPlayerKills();
        kill++;
        setPlayerKills(kill);
    }

    public void setEntityKills(int entityKills) {
        this.entityKills = entityKills;
    }

    public int getEntityKills() {
        return entityKills;
    }

    public void addEntityKill() {
        int entityKills = getEntityKills();
        entityKills++;
        setEntityKills(entityKills);
    }

    public void setDeath(int death) {
        this.deaths = death;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        int deaths = getDeaths();
        deaths++;
        setDeath(deaths);
    }

    public void savePlayerManager() {
        File file = new File(Main.getInstance().getDataFolder() + "/PlayerData/JsonFiles", name + ".json");
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            String json = gson.toJson(this, PlayerManagerCfgLoss.class);
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException ignore) {
        }
    }

    public static PlayerManagerCfgLoss getPlayerManagerCfgLoss(OfflinePlayer player) throws FileNotFoundException {
        File file = new File(Main.getInstance().getDataFolder() + "/PlayerData/JsonFiles", player.getName() + ".json");
        FileReader fileReader = new FileReader(file);
        return new Gson().fromJson(fileReader, PlayerManagerCfgLoss.class);
    }

    public static PlayerManagerCfgLoss getPlayerManagerCfgLoss(String name) throws FileNotFoundException {
        File file = new File(Main.getInstance().getDataFolder() + "/PlayerData/JsonFiles", name + ".json");
        FileReader fileReader = new FileReader(file);
        return new Gson().fromJson(fileReader, PlayerManagerCfgLoss.class);
    }

    public static PlayerManagerCfgLoss getPlayerManagerCfgLoss(UUID uuid) throws FileNotFoundException {
        File file = new File(Main.getInstance().getDataFolder() + "/PlayerData/JsonFiles", Bukkit.getOfflinePlayer(uuid).getName() + ".json");
        FileReader fileReader = new FileReader(file);
        return new Gson().fromJson(fileReader, PlayerManagerCfgLoss.class);
    }

    public void save() {
        if (!Main.getInstance().getSaver().containsKey(uuid)) {
            Main.getInstance().getSaver().put(uuid, new BukkitRunnable() {
                @Override
                public void run() {
                    savePlayerManager();
                }
            }.runTaskTimer(Main.getInstance(), 0, 20 * 60 * 5));
        }
    }

    @Override
    public String toString() {
        return "PlayerManagerCfgLoss{" +
                "name=" + name +
                ",uuid=" + uuid +
                ",lastlogin=" + lastlogin +
                ",lastLogout=" + lastLogout +
                ",damage=" + damage +
                ",kills=" + kills +
                ",entityKills=" + entityKills +
                ",deaths=" + deaths +
                ",timePlayed=" + timePlayed +
                ",sleepTimes=" + sleepTimes +
                ",blockBroken=" + blockBroken +
                ",blockPlace=" + blockPlace +
                ",createdAt=" + createdAt +
                ",blocksBroken=" + blocksBroken +
                ",blocksPlace=" + blocksPlace +
                ",entityTypes=" + entityTypes +
                ",commandsUsed=" + commandsUsed +
                '}';
    }

    public PlayerManagerCfgLoss fromString(String str) {
        str = str.replace("PlayerManagerCfgLoss{", "");
        str = str.replace("}", "");
        String[] s = str.split(",");
        PlayerManagerCfgLoss playerManagerCfgLoss = new PlayerManagerCfgLoss(s[0].replace("name=", ""));
        playerManagerCfgLoss.setUuid(UUID.fromString(s[1].replace("uuid=", "")));
        playerManagerCfgLoss.setLastLogin(Long.parseLong(s[2].replace("lastlogin=", "")));
        playerManagerCfgLoss.setLastLogout(Long.parseLong(s[3].replace("lastlogout=", "")));
        playerManagerCfgLoss.setDamage(Double.parseDouble(s[4].replace("damage=", "")));
        playerManagerCfgLoss.setKills(Integer.parseInt(s[5].replace("kills=", "")));
        playerManagerCfgLoss.setEntityKills(Integer.parseInt(s[6].replace("entityKills=", "")));
        playerManagerCfgLoss.setDeath(Integer.parseInt(s[7].replace("deaths=", "")));
        playerManagerCfgLoss.setTimePlayed(s[8].replace("timePlayed=", ""));
        playerManagerCfgLoss.setSleepTimes(Integer.parseInt(s[9].replace("sleepTimes=", "")));
        playerManagerCfgLoss.createdAt = s[12].replace("createdAt=", "");
        playerManagerCfgLoss.setCommandsUsed(Integer.parseInt(s[16].replace("commandsUsed=", "")));
        return playerManagerCfgLoss;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerManagerCfgLoss)) return false;
        PlayerManagerCfgLoss that = (PlayerManagerCfgLoss) o;
        return getLastlogin() == that.getLastlogin() &&
                getLastLogout() == that.getLastLogout() &&
                Double.compare(that.getDamage(), getDamage()) == 0 &&
                getKills() == that.getKills() &&
                getEntityKills() == that.getEntityKills() &&
                getDeaths() == that.getDeaths() &&
                getSleepTimes() == that.getSleepTimes() &&
                getBlockBroken() == that.getBlockBroken() &&
                getBlockPlace() == that.getBlockPlace() &&
                getCommandsUsed() == that.getCommandsUsed() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getTimePlayed(), that.getTimePlayed()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt()) &&
                Objects.equals(getBlocksBroken(), that.getBlocksBroken()) &&
                Objects.equals(getBlocksPlace(), that.getBlocksPlace()) &&
                Objects.equals(getEntityTypes(), that.getEntityTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUuid(), getLastlogin(), getLastLogout(), getDamage(), getKills(), getEntityKills(), getDeaths(), getTimePlayed(), getSleepTimes(), getBlockBroken(), getBlockPlace(), getCreatedAt(), getBlocksBroken(), getBlocksPlace(), getEntityTypes(), getCommandsUsed());
    }
}
