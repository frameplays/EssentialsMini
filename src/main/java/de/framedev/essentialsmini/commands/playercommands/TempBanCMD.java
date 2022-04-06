package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanMuteManager;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.DateUnit;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmini.commands.playercommands
 * ClassName TempBanCMD
 * Date: 15.05.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class TempBanCMD extends CommandBase {
    public TempBanCMD(Main plugin) {
        super(plugin, "tempban");
        setup(this);
        setup("removetempban", this);
        setupTabCompleter(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (sender.hasPermission(getPlugin().getPermissionName() + "tempban")) {
                if (args.length == 5) {
                    if (args[0].equalsIgnoreCase("type")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        Ban reason = Ban.valueOf(args[2].toUpperCase());
                        DateUnit unit = DateUnit.valueOf(args[4].toUpperCase());
                        long value = Long.parseLong(args[3]);
                        long current = System.currentTimeMillis();
                        long millis = value * unit.getToSec() * 1000;
                        long newValue = current + millis;
                        Date date = new Date(newValue);
                        if (getPlugin().isMysql() || getPlugin().isSQL()) {
                            new BanMuteManager().setTempBan(target, reason, new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss").format(date));
                        } else {
                            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(Objects.requireNonNull(target.getName()), "§aYou are Banned. Reason:§c " + reason.getReason(), date, "true");
                        }
                        if (target.isOnline())
                            ((Player) target).kickPlayer("§bBan while §c" + reason.getReason() + "§b for §a" + value + " " + unit.getOutput() + "!");
                        sender.sendMessage("§6" + target.getName() + " §ahas been banned while §6" + reason.getReason() + " §afor §6" + value + " " + unit.getOutput() + "!");
                        return true;
                    } else if (args[0].equalsIgnoreCase("own")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        String reason = args[2];
                        DateUnit unit = DateUnit.valueOf(args[4].toUpperCase());
                        long value = Long.parseLong(args[3]);
                        long current = System.currentTimeMillis();
                        long millis = value * unit.getToSec() * 1000;
                        long newValue = current + millis;
                        Date date = new Date(newValue);
                        if (getPlugin().isMysql() || getPlugin().isSQL()) {
                            new BanMuteManager().setTempBan(target, reason, new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss").format(date));
                        } else {
                            Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(Objects.requireNonNull(target.getName()), "§aYou are Banned. Reason:§c " + reason, date, "true");
                        }
                        if (target.isOnline())
                            ((Player) target).kickPlayer("§bBan while §c" + reason + "§b for §a" + value + " " + unit.getOutput() + "!");
                        sender.sendMessage("§6" + target.getName() + " §ahas been banned while §6" + reason + " §afor §6" + value + " " + unit.getOutput() + "!");
                        return true;
                    }
                } else {
                    sender.sendMessage(getPlugin().getPrefix() + getPlugin().getWrongArgs("/tempban <Player> <Reason> <time> <SEC or MIN or DAY or WEEK or MONTH or YEAR>"));
                    return true;
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("removetempban")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (sender.hasPermission(getPlugin().getPermissionName() + "tempban")) {
                if (getPlugin().isMysql() || getPlugin().isSQL()) {
                    new BanMuteManager().removeTempBan(target);
                    Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
                } else {
                    Bukkit.getServer().getBanList(BanList.Type.NAME).pardon(target.getName());
                }
                sender.sendMessage("§6" + target.getName() + " §ahas been unbanned!");
            }
        }
        return super.onCommand(sender, cmd, label, args);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tempban")) {
            if (args.length == 1) {
                List<String> reasons = new ArrayList<>();
                reasons.add("type");
                reasons.add("own");
                ArrayList<String> empty = new ArrayList<>();
                for (String s : reasons) {
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
                    List<String> reasons = new ArrayList<>();
                    Arrays.asList(Ban.values()).forEach(ban -> reasons.add(ban.name()));
                    ArrayList<String> empty = new ArrayList<>();
                    for (String s : reasons) {
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
            if (args.length == 4) {
                return new ArrayList<>(Collections.singletonList("Time"));
            }
            if (args.length == 5) {
                ArrayList<String> dateFormat = new ArrayList<>();
                Arrays.asList(DateUnit.values()).forEach(dateUnit -> dateFormat.add(dateUnit.name()));
                ArrayList<String> empty = new ArrayList<>();
                for (String s : dateFormat) {
                    if (s.toLowerCase().startsWith(args[4].toLowerCase())) {
                        empty.add(s);
                    }
                }
                Collections.sort(empty);
                return empty;
            }
        }
        if (command.getName().equalsIgnoreCase("removetempban")) {
            if (args.length == 1) {
                List<String> playerNames = new ArrayList<>();
                List<String> empty = new ArrayList<>();
                if (getPlugin().isMysql() || getPlugin().isSQL()) {
                    playerNames = new BanMuteManager().getAllTempBannedPlayers();
                } else {
                    List<OfflinePlayer> players = (List<OfflinePlayer>) getPlugin().getServer().getBannedPlayers();
                    for (OfflinePlayer player : players) {
                        playerNames.add(player.getName());
                    }
                }
                for (String s : playerNames) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase()))
                        empty.add(s);
                }
                Collections.sort(empty);
                return empty;
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }

    public enum Ban {

        CLIENT_MODIFICATIONS("client modifications"),
        BUG_USING("exploit bugs"),
        FORBIDDEN_SKIN("forbidden skin/name"),
        DESTROY_BUILDINGS("destroy other buildings"),
        TROLLING("trolling"),
        TEAMING("teaming"),
        GRIEFING("griefing"),
        OFFENSIVE_INAPPROPROATE_BUILDING("Offensive / inappropriate building");

        private final String reason;

        Ban(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }
}
