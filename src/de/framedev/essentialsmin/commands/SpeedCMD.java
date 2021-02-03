package de.framedev.essentialsmin.commands;


/*
 * EssentialsMini
 * de.framedev.essentialsmin.commands
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 15.10.2020 20:02
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCMD extends CommandBase implements CommandExecutor {

    private final Main plugin;

    public SpeedCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("speed",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            if(sender instanceof Player) {
                if(sender.hasPermission(plugin.getPermissionName() + "speed")) {
                    ((Player) sender).setWalkSpeed(Integer.parseInt(args[0]) / 10F);
                    sender.sendMessage(plugin.getPrefix() + "§aDeine Geh Geschwindigkeit wurde geändert auf §6" + Integer.parseInt(args[0]) / 10F);
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        }
        return false;
    }
}
