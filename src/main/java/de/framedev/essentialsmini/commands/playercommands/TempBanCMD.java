package de.framedev.essentialsmini.commands.playercommands;

import com.sun.org.apache.xerces.internal.xs.StringList;
import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.DateUnit;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        setupTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (sender.hasPermission(getPlugin().getPermissionName() + "tempban")) {
                if (args.length == 4) {
                    Player target = Bukkit.getPlayer(args[0]);
                    Ban grund = Ban.valueOf(args[1].toUpperCase());
                    if (Bukkit.getOnlineMode()) {
                        DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                        long value = Long.parseLong(args[2]);
                        long current = System.currentTimeMillis();
                        long millis = value * unit.getToSec() * 1000;
                        long newValue = current + millis;
                        Date date = new Date(newValue);
                        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), "§aYou are Banned. Reason:§c " + grund.getReason(), date, "true");
                        target.kickPlayer("§bBan while §c" + grund.getReason() + "§b for §a" + value + " " + unit.getOutput() + "!");
                        return true;
                    } else {
                        DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                        long value = Long.parseLong(args[2]);
                        long current = System.currentTimeMillis();
                        long millis = value * unit.getToSec() * 1000;
                        long newValue = current + millis;
                        Date date = new Date(newValue);
                        Bukkit.getServer().getBanList(BanList.Type.IP).addBan(target.getAddress().getHostString(), "§aYou are Banned. Reason:§c " + grund.getReason(), date, "true");
                        target.kickPlayer("§bBan while §c" + grund.getReason() + "§b for §a" + value + " " + unit.getOutput() + "!");
                        return true;
                    }
                } else {
                    sender.sendMessage(getPlugin().getPrefix() + getPlugin().getWrongArgs("/tempban <Player> <Reason> <time> <SEC or MIN or DAY or WEEK or MONTH or YEAR>"));
                    return true;
                }
            }
        }
        return super.onCommand(sender, cmd, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 2) {
            List<String> reasons = new ArrayList<>();
            for(Ban s : TempBanCMD.Ban.values()) {
                reasons.add(s.name());
            }
            ArrayList<String> empt = new ArrayList<>();
            for(String s : reasons) {
                if(args[1].toLowerCase().startsWith(s)) {
                    empt.add(s);
                }
            }
            Collections.sort(empt);
            return empt;
        }
        if(args.length == 3) {
            return new ArrayList<String>(Collections.singletonList("Time"));
        }
        if(args.length == 4) {
            ArrayList<String> dateFormat = new ArrayList<>();
            for(DateUnit unit : DateUnit.values()) {
                dateFormat.add(unit.name());
            }
            ArrayList<String> empty = new ArrayList<>();
            for(String s : dateFormat) {
                if(args[3].toLowerCase().startsWith(s))
                    empty.add(s);
            }
            Collections.sort(empty);
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }

    public static enum Ban {

        CLIENT_MODIFICATIONS("client modifications"),
        BUG_USING("exploit bugs"),
        FORBIDDEN_SKIN("forbidden skin/name"),
        DESTROY_BUILDINGS("destroy other buildings"),
        TROLLING("trolling"),
        TEAMING("teaming"),
        GRIEFING("griefing");

        private final String reason;

        Ban(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }
}
