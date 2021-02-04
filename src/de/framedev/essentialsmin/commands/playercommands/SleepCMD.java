package de.framedev.essentialsmin.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 10.08.2020 16:38
 */

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.Locations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SleepCMD implements CommandExecutor {

    private final Main plugin;

    private ArrayList<Location> bedLoc = new ArrayList<>();
    private ArrayList<Material> block = new ArrayList<>();

    public SleepCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("sleep",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(sender.hasPermission("essentialsmini.sleep")) {
                Player player = (Player) sender;
                Location location = player.getLocation().subtract(0,0,0);
                bedLoc.add(location);
                block.add(location.getBlock().getType());
                location.getBlock().setType(Material.BLUE_BED);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Location location : bedLoc) {
                            for(Material mat : block) {
                                location.getBlock().setType(mat);
                            }
                        }
                        bedLoc.clear();
                        block.clear();
                    }
                }.runTaskLater(plugin,7*20);
                player.sleep(player.getLocation(),false);
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }
}
