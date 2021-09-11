package de.framedev.essentialsmini.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 10.08.2020 16:38
 */

import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SleepCMD implements CommandExecutor {

    private final Main plugin;

    private final ArrayList<Location> bedLoc = new ArrayList<>();
    private final ArrayList<Material> block = new ArrayList<>();

    public SleepCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("sleep", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("essentialsmini.sleep")) {
                Player player = (Player) sender;
                Location location = player.getLocation().subtract(0, 0, 0);
                bedLoc.add(location);
                block.add(location.getBlock().getType());
                DyeColor dyeColor = DyeColor.valueOf(plugin.getConfig().getString("BedColor").toUpperCase());
                setBed(location.getBlock(), BlockFace.NORTH, Material.getMaterial(dyeColor.name().toUpperCase() + "_BED"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        location.getBlock().setType(Material.AIR);
                    }
                }.runTaskLater(plugin, 7 * 20);
                player.sleep(player.getLocation(), false);
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }

    public void setBed(Block start, BlockFace facing, Material material) {
        for (Bed.Part part : Bed.Part.values()) {
            start.setBlockData(Bukkit.createBlockData(material, (data) -> {
                ((Bed) data).setPart(part);
                ((Bed) data).setFacing(facing);
            }));
            start = start.getRelative(facing.getOppositeFace());
        }
    }
}
