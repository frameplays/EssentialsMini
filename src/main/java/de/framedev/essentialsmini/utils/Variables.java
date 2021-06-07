package de.framedev.essentialsmini.utils;


/*
 * de.framedev.essentialsmin.utils
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 25.08.2020 20:09
 */

import de.framedev.essentialsmini.main.Main;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;

public class Variables {

    transient private final Main instance;
    private final String prefix;
    private final String onlyPlayer;
    private final String noPermission;
    private final String permissionBase;
    private final String author;
    private final String version;
    private final String apiVersion;
    private final boolean onlineMode;
    private final boolean jsonFormat;
    private String playerNameNotOnline;
    private String playerNotOnline;

    private ArrayList<OfflinePlayer> players = new ArrayList<>();

    public Variables() {
        this.instance = Main.getInstance();
        this.prefix = instance.getPrefix();
        this.onlyPlayer = instance.getOnlyPlayer();
        this.noPermission = instance.getNOPERMS();
        this.permissionBase = instance.getPermissionName();
        this.author = instance.getDescription().getAuthors().get(0);
        this.version = instance.getDescription().getVersion();
        this.apiVersion = instance.getDescription().getAPIVersion();
        this.onlineMode = instance.getConfig().getBoolean("OnlineMode");
        this.jsonFormat = instance.getConfig().getBoolean("JsonFormat");
        this.playerNameNotOnline = instance.getCustomMessagesConfig().getString("PlayerNameNotOnline");
        this.playerNotOnline = instance.getCustomMessagesConfig().getString("PlayerNotOnline");
    }

    public String getPlayerNotOnline() {
        playerNotOnline = ReplaceCharConfig.replaceParagraph(playerNotOnline);
        return playerNotOnline;
    }

    public String getPlayerNameNotOnline(String playerName) {
        if (playerNameNotOnline.contains("&"))
            playerNameNotOnline = playerNameNotOnline.replace('&', '§');
        if (playerNameNotOnline.contains("%Player%"))
            playerNameNotOnline = playerNameNotOnline.replace("%Player%", playerName);
        return playerNameNotOnline;
    }

    public boolean isJsonFormat() {
        return jsonFormat;
    }

    public String getWrongArgs(String cmdName) {
        return instance.getWrongArgs(cmdName);
    }

    public String getVersion() {
        return version;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getAuthor() {
        return author;
    }

    public String getPermissionBase() {
        return permissionBase;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getOnlyPlayer() {
        return onlyPlayer;
    }

    public String getNoPermission() {
        return noPermission;
    }

    @Override
    public String toString() {
        return "Variables{" +
                "instance=" + instance +
                ", prefix='" + prefix + '\'' +
                ", onlyPlayer='" + onlyPlayer + '\'' +
                ", noPermission='" + noPermission + '\'' +
                ", permissionBase='" + permissionBase + '\'' +
                ", author='" + author + '\'' +
                ", version='" + version + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                ", onlineMode=" + onlineMode +
                ", jsonFormat=" + jsonFormat +
                ", playerNameNotOnline='" + playerNameNotOnline + '\'' +
                ", playerNotOnline='" + playerNotOnline + '\'' +
                '}';
    }

    public ArrayList<OfflinePlayer> getPlayers() {
        return players;
    }
}



