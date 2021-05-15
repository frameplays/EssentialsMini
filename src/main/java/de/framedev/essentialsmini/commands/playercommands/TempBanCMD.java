package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.DateUnit;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

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
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tempban")) {
            if (sender.hasPermission(getPlugin().getPermissionName() + "tempban")) {
                if (args.length == 4) {
                    Player target = Bukkit.getPlayer(args[0]);
                    String grund = args[1];
                    if (Bukkit.getOnlineMode()) {
                        DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                        long value = Long.parseLong(args[2]);
                        long current = System.currentTimeMillis();
                        long millis = value * unit.getToSec() * 1000;
                        long newValue = current + millis;
                        Date date = new Date(newValue);
                        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(target.getName(), "§aYou are Banned. Reason:§c " + grund, date, "true");
                        target.kickPlayer("§bBan while §c" + grund + "§b for §a" + value + " " + unit.getOutput() + "!");
                        return true;
                    } else {
                        DateUnit unit = DateUnit.valueOf(args[3].toUpperCase());
                        long value = Long.parseLong(args[2]);
                        long current = System.currentTimeMillis();
                        long millis = value * unit.getToSec() * 1000;
                        long newValue = current + millis;
                        Date date = new Date(newValue);
                        Bukkit.getServer().getBanList(BanList.Type.IP).addBan(target.getAddress().getHostString(), "§aYou are Banned. Reason:§c " + grund, date, "true");
                        target.kickPlayer("§bBan while §c" + grund + "§b for §a" + value + " " + unit.getOutput() + "!");
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
}
