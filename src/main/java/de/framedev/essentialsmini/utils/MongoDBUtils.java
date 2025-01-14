package de.framedev.essentialsmini.utils;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BackendManager;
import de.framedev.mongodbconnections.main.MongoManager;
import org.bukkit.Bukkit;

import java.util.logging.Level;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.utils
 * ClassName MongoDbUtils
 * Date: 06.04.21
 * Project: Unknown
 * Copyrighted by FrameDev
 */

public class MongoDBUtils {

    private boolean mongoDb = false;
    private MongoManager mongoManager;
    private de.framedev.essentialsmini.managers.BackendManager backendManager;

    public MongoDBUtils() {
        /* MongoDB */
        if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
            if (Main.cfgMongoDB.getBoolean("MongoDB.LocalHost") || Main.cfgMongoDB.getBoolean("MongoDB.Boolean")) {
                this.mongoDb = true;
            }
        }
        if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
            if (Main.cfgMongoDB.getBoolean("MongoDB.LocalHost")) {
                this.mongoManager = new MongoManager();
                this.mongoManager.connectLocalHost();
                Main.getInstance().getLogger().log(Level.INFO, "MongoDB Enabled");
            }
            if (Main.cfgMongoDB.getBoolean("MongoDB.Boolean")) {
                this.mongoManager = new MongoManager();
                this.mongoManager.connect();
                Main.getInstance().getLogger().log(Level.INFO, "MongoDB Enabled");
            }
            if (Main.cfgMongoDB.getBoolean("MongoDB.LocalHost")) {
                this.backendManager = new BackendManager(Main.getInstance());
            }
            if (Main.cfgMongoDB.getBoolean("MongoDB.Boolean")) {
                this.backendManager = new BackendManager(Main.getInstance());
            }
        }
    }

    /**
     * This Method returns if MongoDB is enabled or not
     *
     * @return return if MongoDB is Enabled or not
     */
    public boolean isMongoDb() {
        return mongoDb;
    }

    /**
     * This Method returns the MongoManager class
     *
     * @return return MongoManager class
     */
    public MongoManager getMongoManager() {
        return mongoManager;
    }

    /**
     * This Method returns the BackendManager class
     *
     * @return return BackendManager class
     */
    public BackendManager getBackendManager() {
        return backendManager;
    }
}
