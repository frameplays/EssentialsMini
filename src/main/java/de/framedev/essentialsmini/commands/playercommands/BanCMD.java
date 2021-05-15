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
			if(args.length >= 1) {
				StringBuilder message = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					message.append(args[i]).append(" ");
				}
				BanFile.banPlayer(args[0], message.toString());
				if (Bukkit.getPlayer(args[0]) != null) {
					Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.RED + "You are Banned while " + ChatColor.GOLD + BanFile.cfg.getString(args[0] + ".reason"));
				} else {
					sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNotOnline());
				}
			} else {
				sender.sendMessage(getPlugin().getPrefix() + getPlugin().getWrongArgs("/eban <Player> <Reason>"));
			}
		}
		return false;
	}
}
