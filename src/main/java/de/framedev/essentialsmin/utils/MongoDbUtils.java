package de.framedev.essentialsmin.utils;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.BackendManager;
import de.framedev.mongodbconnections.main.MongoManager;
import org.bukkit.Bukkit;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.utils
 * ClassName MongoDbUtils
 * Date: 06.04.21
 * Project: Unknown
 * Copyrighted by FrameDev
 */

public class MongoDbUtils {

    private boolean mongoDb = false;
    private MongoManager mongoManager;
    private BackendManager backendManager;

    public MongoDbUtils() {
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
            }
            if (Main.cfgMongoDB.getBoolean("MongoDB.Boolean")) {
                this.mongoManager = new MongoManager();
                this.mongoManager.connect();
            }
            if (Main.cfgMongoDB.getBoolean("MongoDB.LocalHost")) {
                this.backendManager = new BackendManager(Main.getInstance());
            }
            if (Main.cfgMongoDB.getBoolean("MongoDB.Boolean")) {
                this.backendManager = new BackendManager(Main.getInstance());
            }
        }
    }

    public boolean isMongoDb() {
        return mongoDb;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }
}
