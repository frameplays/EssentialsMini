package de.framedev.essentialsmin.managers;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import de.framedev.essentialsmin.main.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.UUID;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.managers
 * Date: 23.11.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class PlayerManagerMongoDB {

    private UUID uuid;
    private String name;
    private double money;
    private double bank;
    private int kills;
    private double damage;
    private int entityKills;
    private int deaths;
    private List<Material> blocksBroken;
    private List<Material> blocksPlacen;
    private int commandsUsed;
    private String key;
    private List<EntityType> entityTypes;
    private boolean offline;
    private String createDate;
    private String lastLogin;
    private String lastLogout;

    public PlayerManagerMongoDB(UUID uuid, String name, double money, double bank, int kills, double damage, int entityKills, int deaths, List<Material> blocksBroken, List<Material> blocksPlacen, int commandsUsed, String key, List<EntityType> entityTypes, boolean offline, String createDate, String lastLogin, String lastLogout) {
        this.uuid = uuid;
        this.name = name;
        this.money = money;
        this.bank = bank;
        this.kills = kills;
        this.damage = damage;
        this.entityKills = entityKills;
        this.deaths = deaths;
        this.blocksBroken = blocksBroken;
        this.blocksPlacen = blocksPlacen;
        this.commandsUsed = commandsUsed;
        this.key = key;
        this.entityTypes = entityTypes;
        this.offline = offline;
        this.createDate = createDate;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
    }

    public PlayerManagerMongoDB(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
    }

    @Deprecated
    public PlayerManagerMongoDB(String name) {
        this.name = name;
        this.uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setBank(double bank) {
        this.bank = bank;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setEntityKills(int entityKills) {
        this.entityKills = entityKills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setBlocksBroken(List<Material> blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public void setBlocksPlacen(List<Material> blocksPlacen) {
        this.blocksPlacen = blocksPlacen;
    }

    public void setCommandsUsed(int commandsUsed) {
        this.commandsUsed = commandsUsed;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLastLogout(String lastLogout) {
        this.lastLogout = lastLogout;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public double getBank() {
        return bank;
    }

    public int getKills() {
        return kills;
    }

    public double getDamage() {
        return damage;
    }

    public int getEntityKills() {
        return entityKills;
    }

    public int getDeaths() {
        return deaths;
    }

    public List<Material> getBlocksBroken() {
        return blocksBroken;
    }

    public List<Material> getBlocksPlacen() {
        return blocksPlacen;
    }

    public int getCommandsUsed() {
        return commandsUsed;
    }

    public String getKey() {
        return key;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public boolean isOffline() {
        return offline;
    }

    public long getCreateDate() {
        return Long.parseLong(createDate);
    }

    public long getLastLogin() {
        return Long.parseLong(lastLogin);
    }

    public long getLastLogout() {
        return Long.parseLong(lastLogout);
    }

    public static PlayerManagerMongoDB getPlayerManager(String playerName, String collection) {
        MongoCollection<Document> collections = Main.getInstance().getMongoManager().getDatabase().getCollection(collection);
        Document result = collections.find(new Document("name", playerName)).first();
        if (result != null)
            return new Gson().fromJson(result.toJson(), PlayerManagerMongoDB.class);
        return new PlayerManagerMongoDB(playerName);
    }

    public void save(String collection) {
        MongoCollection<Document> collections = Main.getInstance().getMongoManager().getDatabase().getCollection(collection);
        String json = new Gson().toJson(this);
        Document document = Document.parse(json);
        Document result = collections.find(new Document("name", name)).first();
        if (result == null) {
            collections.insertOne(document);
        } else {
            result.replace("deaths",getDeaths());
            result.replace("money",getMoney());
            result.replace("bank",getBank());
            result.replace("kills",getKills());
            result.replace("entityKills",entityKills);
            result.replace("entityTypes",entityTypes);
            result.replace("damage",getDamage());
            result.replace("blocksBroken",getBlocksBroken());
            result.replace("blocksPlacen",getBlocksPlacen());
            result.replace("commandsUsed",getCommandsUsed());
            result.replace("key",getKey());
            result.replace(BackendManager.DATA.LASTLOGIN.getName(),getLastLogin());
            result.replace(BackendManager.DATA.LASTLOGOUT.getName(),getLastLogout());
            result.replace(BackendManager.DATA.OFFLINE.getName(),isOffline());
        }
    }
}
