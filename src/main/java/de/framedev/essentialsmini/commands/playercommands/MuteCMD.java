package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.ReplaceCharConfig;
import de.framedev.essentialsmini.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmini.commands.playercommands
 * ClassName MuteCMD
 * Date: 15.05.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class MuteCMD extends CommandBase implements Listener {

    private final Main plugin;

    private final ArrayList<OfflinePlayer> muted;

    public MuteCMD(Main plugin) {
        super(plugin, "mute");
        this.plugin = plugin;
        setup(this);
        plugin.getListeners().add(this);
        this.muted = plugin.getVariables().getPlayers();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            if(!sender.hasPermission(plugin.getPermissionName() + "mute")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                return true;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if(muted.contains(player)) {
                muted.remove(player);
                String selfUnMute = plugin.getCustomMessagesConfig().getString("Mute.Self.Deactivate");
                selfUnMute = ReplaceCharConfig.replaceParagraph(selfUnMute);
                if(player.isOnline())
                    ((Player) player).sendMessage(plugin.getPrefix() + selfUnMute);
                String otherUnMute = plugin.getCustomMessagesConfig().getString("Mute.Other.Activate");
                otherUnMute = ReplaceCharConfig.replaceParagraph(otherUnMute);
                otherUnMute = ReplaceCharConfig.replaceObjectWithData(otherUnMute, "%Player%", player.getName());
                sender.sendMessage(plugin.getPrefix() + otherUnMute);
            } else {
                muted.add(player);
                String selfMute = plugin.getCustomMessagesConfig().getString("Mute.Self.Activate");
                selfMute = ReplaceCharConfig.replaceParagraph(selfMute);
                if(player.isOnline())
                    ((Player) player).sendMessage(plugin.getPrefix() + "§cYou has been Muted!");
                String otherMute = plugin.getCustomMessagesConfig().getString("Mute.Other.Activate");
                otherMute = ReplaceCharConfig.replaceParagraph(otherMute);
                otherMute = ReplaceCharConfig.replaceObjectWithData(otherMute, "%Player%", player.getName());
                sender.sendMessage(plugin.getPrefix() + otherMute);
            }
            return true;
        }
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    public void onChatWrite(AsyncPlayerChatEvent event) {
        if(muted.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(plugin.getPrefix() + "§cYou are Muted!");
            event.setCancelled(true);
        }
    }
}
