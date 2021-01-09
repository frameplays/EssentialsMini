package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 23.11.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class BankCMD extends CommandBase {

    private final Main plugin;

    public BankCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("bank",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("bank")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("balance")) {
                    sender.sendMessage(plugin.getPrefix() + "§cNot yeet avaible!");
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
