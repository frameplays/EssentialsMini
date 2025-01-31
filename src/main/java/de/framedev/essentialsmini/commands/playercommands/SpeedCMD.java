package de.framedev.essentialsmini.commands.playercommands;


/*
 * EssentialsMini
 * de.framedev.essentialsmin.commands
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 15.10.2020 20:02
 */

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCMD extends CommandBase {

    private final Main plugin;

    public SpeedCMD(Main plugin) {
        super(plugin, "speed");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player) {
                if (sender.hasPermission(plugin.getPermissionName() + "speed")) {
                    ((Player) sender).setWalkSpeed(Integer.parseInt(args[0]) / 10F);
                    int walkSpeed = Integer.parseInt(args[0]);
                    String message = plugin.getCustomMessagesConfig().getString("WalkSpeed");
                    message = new TextUtils().replaceAndToParagraph(message);
                    message = new TextUtils().replaceObject(message, "%WalkSpeed%", String.valueOf(walkSpeed));
                    sender.sendMessage(plugin.getPrefix() + message);
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
