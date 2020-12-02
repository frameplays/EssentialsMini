package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.commands
 * Date: 24.10.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class ClearChatCMD extends CommandBase {

    private final Main plugin;

    public ClearChatCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        setup("chatclear",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(sender.hasPermission(plugin.getPermissionName() + "chatclear")) {
            clearChat(sender);
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
        }
        return super.onCommand(sender, command, label, args);
    }

    public void clearChat(CommandSender sender) {
        for(int i = 0; i <= 500; i++) {
            Bukkit.broadcastMessage(" ");
        }
        Bukkit.broadcastMessage(plugin.getPrefix() + "§aDer Chat wurde von §6" + sender.getName() + " §ageleert!");
    }
}
