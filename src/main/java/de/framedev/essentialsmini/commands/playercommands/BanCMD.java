package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanFile;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class BanCMD extends CommandBase {

    public BanCMD(Main plugin) {
        super(plugin, "eban");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission(getPlugin().getPermissionName() + "ban")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("type")) {
                    BanType type = BanType.valueOf(args[2].toUpperCase());
                    if (getPlugin().isMysql() || getPlugin().isSQL()) {
                        if (Bukkit.getPlayer(args[1]) != null)
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + type.getReason());
                        new BanMuteManager().setPermaBan(Bukkit.getOfflinePlayer(args[1]), type, true);
                    } else {
                        BanFile.banPlayer(args[1], type.getReason());
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Objects.requireNonNull(Bukkit.getPlayer(args[1])).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + type.getReason());
                        } else {
                            sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNotOnline());
                        }
                    }
                } else {
                    if (args[0].equalsIgnoreCase("own")) {
                        if (getPlugin().isMysql() || getPlugin().isSQL()) {
                            if (Bukkit.getPlayer(args[1]) != null)
                                Objects.requireNonNull(Bukkit.getPlayer(args[1])).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + args[2]);
                            new BanMuteManager().setPermaBan(Bukkit.getOfflinePlayer(args[1]), args[2], true);
                        } else {
                            BanFile.banPlayer(args[1], args[2]);
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Objects.requireNonNull(Bukkit.getPlayer(args[1])).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + args[2]);
                            } else {
                                sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNotOnline());
                            }
                        }
                    }
                }
            } else {
                sender.sendMessage(getPlugin().getPrefix() + getPlugin().getWrongArgs("/eban <Player> <Reason>"));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> types = new ArrayList<>();
            types.add("type");
            types.add("own");
            ArrayList<String> empty = new ArrayList<>();
            for (String s : types) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                    empty.add(s);
                }
            }
            Collections.sort(empty);
            return empty;
        }
        if (args.length == 2) {
            ArrayList<String> reason = new ArrayList<>();
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                reason.add(offlinePlayer.getName());
            }
            ArrayList<String> empty = new ArrayList<>();
            for (String s : reason) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase()))
                    empty.add(s);
            }
            Collections.sort(empty);
            return empty;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("type")) {
                ArrayList<String> types = new ArrayList<>();
                Arrays.asList(BanType.values()).forEach(type -> types.add(type.name()));
                ArrayList<String> empty = new ArrayList<>();
                for (String s : types) {
                    if (s.toLowerCase().startsWith(args[2].toLowerCase())) {
                        empty.add(s);
                    }
                }
                Collections.sort(empty);
                return empty;
            }
            if (args[0].equalsIgnoreCase("own")) {
                return new ArrayList<String>(Collections.singleton("your_Message"));
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }

    public static enum BanType {

        HACKING("hacking"),
        TRY_BYPASSING_BAN("try bypassing a Ban");

        private final String reason;

        BanType(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }
}
