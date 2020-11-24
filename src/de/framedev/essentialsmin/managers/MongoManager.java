package de.framedev.essentialsmin.managers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;

public class MongoManager {
    File file = new File("plugins/MDBConnection/config.yml");
    FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(this.file);
    String databasestring = this.cfg.getString("MongoDB.Database");
    String username = this.cfg.getString("MongoDB.User");
    String password = this.cfg.getString("MongoDB.Password");
    private String hostname = this.cfg.getString("MongoDB.Host");
    private int port = this.cfg.getInt("MongoDB.Port");

    private MongoClient client;

    private MongoDatabase database;


    public void connectLocalHost() {
        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress(this.hostname, this.port)))).build());
        this.database = this.client.getDatabase(this.databasestring);
    }

    public void connect() {
        MongoCredential credential = MongoCredential.createCredential(this.username, this.databasestring, this.password.toCharArray());
        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .credential(credential)
                        .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(new ServerAddress(this.hostname, this.port)))).build());
        this.database = this.client.getDatabase(this.databasestring);
    }


    public MongoClient getClient() {
        return this.client;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }
}

