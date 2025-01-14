package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmini.commands.playercommands
 * ClassName FlySpeedCMD
 * Date: 14.05.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class FlySpeedCMD extends CommandBase {

    private final Main plugin;

    public FlySpeedCMD(Main plugin) {
        super(plugin, "flyspeed");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission(plugin.getPermissionName() + "flyspeed")) {
                player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                return true;
            }
            float flyspeed = Float.parseFloat(args[0]) / 10F;
            player.setFlySpeed(flyspeed);
            String flySpeed = plugin.getCustomMessagesConfig().getString("ChangeFlySpeed");
            flySpeed = new TextUtils().replaceObject("flySpeed", "%flyspeed%", String.valueOf(flyspeed * 10F));
            flySpeed = new TextUtils().replaceAndToParagraph(flySpeed);
            player.sendMessage(flySpeed);
            return true;
        } else if (args.length == 2) {
            if (!sender.hasPermission(plugin.getPermissionName() + "flyspeed.others")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                return true;
            }
            float flyspeed = Float.parseFloat(args[0]) / 10F;
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[1]));
                return true;
            }
            player.setFlySpeed(flyspeed);
            String flySpeed = plugin.getCustomMessagesConfig().getString("ChangeFlySpeed");
            flySpeed = new TextUtils().replaceObject("flySpeed", "%flyspeed%", String.valueOf(flyspeed * 10F));
            flySpeed = new TextUtils().replaceAndToParagraph(flySpeed);
            player.sendMessage(flySpeed);
            String other = plugin.getCustomMessagesConfig().getString("ChangeFlySpeedOther");
            other = new TextUtils().replaceAndToParagraph(other);
            other = new TextUtils().replaceObject(other, "%player%", player.getName());
            other = new TextUtils().replaceObject(other, "%flyspeed%", String.valueOf(flyspeed * 10F));
            sender.sendMessage(other);
            return true;
        }
        return super.onCommand(sender, command, label, args);
    }
}
