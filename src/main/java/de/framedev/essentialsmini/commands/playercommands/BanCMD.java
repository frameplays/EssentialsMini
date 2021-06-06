package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanFile;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class BanCMD extends CommandBase {

    public BanCMD(Main plugin) {
        super(plugin);
        setup("eban", this);
        setupTabCompleter("eban", this);
        setup(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission(getPlugin().getPermissionName() + "ban")) {
            if (args.length == 2) {
                BanType type = BanType.valueOf(args[1].toUpperCase());
                if (getPlugin().isMysql() || getPlugin().isSQL()) {
                    if (Bukkit.getPlayer(args[0]) != null)
                        Objects.requireNonNull(Bukkit.getPlayer(args[0])).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + type.getReason());
                    new BanMuteManager().setPermaBan(Bukkit.getOfflinePlayer(args[0]), type, true);
                } else {
                    BanFile.banPlayer(args[0], type.getReason());
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Objects.requireNonNull(Bukkit.getPlayer(args[0])).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + type.getReason());
                    } else {
                        sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNotOnline());
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
        if (args.length == 2) {
            ArrayList<String> types = new ArrayList<>();
            Arrays.asList(BanType.values()).forEach(type -> types.add(type.name()));
            ArrayList<String> empty = new ArrayList<>();
            for (String s : types) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                    empty.add(s);
                }
            }
            Collections.sort(empty);
            return empty;
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
