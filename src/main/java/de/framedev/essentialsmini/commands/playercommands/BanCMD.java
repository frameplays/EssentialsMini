package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.BanFile;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BanCMD extends CommandBase {

	public BanCMD(Main plugin) {
		super(plugin, "eban");
		setup(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("essentialsmini.ban")) {
				String message = "";
				for(int i = 1; i < args.length; i++) {
					message = message + args[i] + " ";
				}
			BanFile.banPlayer(args[0], message);
			if(Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD+ BanFile.cfg.getString( args[0] + ".reason"));
			}
		}
		return false;
	}
}
