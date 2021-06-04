package de.framedev.essentialsmini.managers;

import de.framedev.essentialsmini.commands.playercommands.BanCMD;
import de.framedev.essentialsmini.commands.playercommands.MuteCMD;
import de.framedev.essentialsmini.commands.playercommands.TempBanCMD;
import de.framedev.mysqlapi.api.SQL;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.managers
 * / ClassName BanMuteManager
 * / Date: 03.06.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class BanMuteManager {

    // Database for MySQl / SQLite
    private final String table = "essentialsmini_banmute";

    /**
     * Set TempMute to Database for Syncing
     *
     * @param player the Player
     * @param reason Reason {@link MuteCMD.MuteReason}
     * @param date the actual date
     */
    public void setTempMute(OfflinePlayer player, MuteCMD.MuteReason reason, String date) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                SQL.updateData(table, "TempMute", "'" + date + "'", "Player = '" + player.getName() + "'");
                SQL.updateData(table, "TempMuteReason", "'" + reason.getReason() + "'", "Player = '" + player.getName() + "'");
            } else {
                SQL.insertData(table, "'" + player.getName() + "','" + date + "','" + reason.getReason() + "'", "Player", "TempMute", "TempMuteReason");
            }
        } else {
            SQL.createTable(table, "Player VARCHAR(1255)", "TempMute TEXT", "TempMuteReason TEXT", "TempBan TEXT", "TempBanReason TEXT", "Ban BOOLEAN", "BanReason TEXT");
            SQL.insertData(table, "'" + player.getName() + "','" + date + "','" + reason.getReason() + "'", "Player", "TempMute", "TempMuteReason");
        }
    }

    public void removeTempMute(OfflinePlayer player) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                SQL.updateData(table, "TempMute", "'" + " " + "'", "Player = '" + player.getName() + "'");
                SQL.updateData(table, "TempMuteReason", "'" + " " + "'", "Player = '" + player.getName() + "'");
            }
        }
    }

    public HashMap<String, String> getTempMute(OfflinePlayer player) {
        HashMap<String, String> tempMute = new HashMap<>();
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                if (SQL.get(table, "TempMute", "Player", player.getName()) != null) {
                    tempMute.put((String) SQL.get(table, "TempMute", "Player", player.getName()), (String) SQL.get(table, "TempMuteReason", "Player", player.getName()));
                    return tempMute;
                }
            }
        }
        return null;
    }

    public boolean isTempMute(OfflinePlayer player) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                if (SQL.get(table, "TempMute", "Player", player.getName()) != null) {
                    return !((String) SQL.get(table, "TempMute", "Player", player.getName())).equalsIgnoreCase(" ");
                }
            }
        }
        return false;
    }

    public void setTempBan(OfflinePlayer player, TempBanCMD.Ban reason, String date) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                SQL.updateData(table, "TempBan", "'" + date + "'", "Player = '" + player.getName() + "'");
                SQL.updateData(table, "TempBanReason", "'" + reason.getReason() + "'", "Player = '" + player.getName() + "'");
            } else {
                SQL.insertData(table, "'" + player.getName() + "','" + date + "','" + reason.getReason() + "'", "Player", "TempBan", "TempBanReason");
            }
        } else {
            SQL.createTable(table, "Player VARCHAR(1255)", "TempMute TEXT", "TempMuteReason TEXT", "TempBan TEXT", "TempBanReason TEXT", "Ban BOOLEAN", "BanReason TEXT");
            SQL.insertData(table, "'" + player.getName() + "','" + date + "','" + reason.getReason() + "'", "Player", "TempBan", "TempBanReason");
        }
    }

    public void removeTempBan(OfflinePlayer player) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(player.getName());
                SQL.updateData(table, "TempBan", "'" + " " + "'", "Player = '" + player.getName() + "'");
                SQL.updateData(table, "TempBanReason", "'" + " " + "'", "Player = '" + player.getName() + "'");
            }
        }
    }

    public HashMap<String, String> getTempBan(OfflinePlayer player) {
        HashMap<String, String> tempBan = new HashMap<>();
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                if (SQL.get(table, "TempBan", "Player", player.getName()) != null) {
                    tempBan.put((String) SQL.get(table, "TempBan", "Player", player.getName()), (String) SQL.get(table, "TempBanReason", "Player", player.getName()));
                    return tempBan;
                }
            }
        }
        return null;
    }

    public boolean isTempBan(OfflinePlayer player) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                if (SQL.get(table, "TempBan", "Player", player.getName()) != null) {
                    return !((String) SQL.get(table, "TempBan", "Player", player.getName())).equalsIgnoreCase(" ");
                }
            }
        }
        return false;
    }

    public void setPermaBan(OfflinePlayer player, BanCMD.BanType reason, boolean permaBan) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                SQL.updateData(table, "Ban", "'" + permaBan + "'", "Player = '" + player.getName() + "'");
                SQL.updateData(table, "BanReason", "'" + reason.getReason() + "'", "Player = '" + player.getName() + "'");
            } else {
                SQL.insertData(table, "'" + player.getName() + "','" + permaBan + "','" + reason.getReason() + "'", "Player", "Ban", "BanReason");
            }
        } else {
            SQL.createTable(table, "Player VARCHAR(1255)", "TempMute TEXT", "TempMuteReason TEXT", "TempBan TEXT", "TempBanReason TEXT", "Ban BOOLEAN", "BanReason TEXT");
            SQL.insertData(table, "'" + player.getName() + "','" + permaBan + "','" + reason.getReason() + "'", "Player", "Ban", "BanReason");
        }
    }

    public boolean isPermaBan(OfflinePlayer player) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                if (SQL.get(table, "Ban", "Player", player.getName()) != null)
                    return (boolean) SQL.get(table, "Ban", "Player", player.getName());
            }
        }
        return false;
    }

    public String getPermaBanReason(OfflinePlayer player) {
        if (SQL.isTableExists(table)) {
            if (SQL.exists(table, "Player", player.getName())) {
                if (SQL.get(table, "BanReason", "Player", player.getName()) != null)
                    return (String) SQL.get(table, "BanReason", "Player", player.getName());
            }
        }
        return "";
    }
}
