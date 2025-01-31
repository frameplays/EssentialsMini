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
import java.util.List;

public class Variables {

    transient private final Main instance;
    private final String prefix;
    private final String onlyPlayer;
    private final String noPermission;
    private final String permissionBase;
    private final List<String> authors;
    private final String version;
    private final String apiVersion;
    private final boolean onlineMode;
    private final boolean jsonFormat;
    private String playerNameNotOnline;
    private String playerNotOnline;

    public static final String TPMESSAGES = "TpaMessages";
    public static final String MONEYMESSAGE = "Money";
    public static final String WARPMESSAGE = "Warp";
    public static final String EXPERIENCE = "Experience";
    public static final String ADMINBROADCAST = "AdminCommandBroadCast";
    public static final String BANK = "Bank";

    private final ArrayList<OfflinePlayer> players = new ArrayList<>();
    private final ArrayList<String> offlinePlayers;

    public Variables() {
        this.instance = Main.getInstance();
        this.prefix = instance.getPrefix();
        this.onlyPlayer = instance.getOnlyPlayer();
        this.noPermission = instance.getNOPERMS();
        this.permissionBase = instance.getPermissionName();
        this.authors = instance.getDescription().getAuthors();
        this.version = instance.getDescription().getVersion();
        this.apiVersion = instance.getDescription().getAPIVersion();
        this.onlineMode = instance.getConfig().getBoolean("OnlineMode");
        this.jsonFormat = instance.getConfig().getBoolean("JsonFormat");
        this.playerNameNotOnline = instance.getCustomMessagesConfig().getString("PlayerNameNotOnline");
        this.playerNotOnline = instance.getCustomMessagesConfig().getString("PlayerNotOnline");
        this.offlinePlayers = instance.getOfflinePlayers();
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

    public List<String> getAuthors() {
        return authors;
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

    public ArrayList<OfflinePlayer> getPlayers() {
        return players;
    }

    public ArrayList<String> getOfflinePlayers() {
        return offlinePlayers;
    }
}



