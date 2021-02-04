package de.framedev.essentialsmin.commands.servercommands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils.copyFile;

public class BackUpCMD extends CommandBase {

    private final Main plugin;
    private final boolean makeBackup;

    public BackUpCMD(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        this.makeBackup = plugin.getConfig().getBoolean("WorldBackup");
        setup("backup",this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("backup")) {
            if(sender.hasPermission(plugin.getPermissionName() + "backup")) {
                backup();
                sender.sendMessage(plugin.getPrefix() + "Backup Created!");
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    public void backup() {
        for(World world : Bukkit.getWorlds()) {
            if(!world.isAutoSave())
                world.setAutoSave(true);
            world.save();
            try {
                copyDirectory(world.getWorldFolder(),new File("Backups/"+ new SimpleDateFormat("HH:mm:ss|yyyy.MM.dd").format(new Date(System.currentTimeMillis())) + "/"+world.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeBackups() {
        if(makeBackup) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    backup();
                    Bukkit.getConsoleSender().sendMessage(plugin.getPrefix() + "Backup Created!");
                }
            }.runTaskTimer(plugin, 0, 20L * 60 * plugin.getConfig().getInt("BackupWorldTime"));
        }
    }

    private static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }
        for (String f : Objects.requireNonNull(sourceDirectory.list())) {
            copyDirectoryCompatibityMode(new File(sourceDirectory, f), new File(destinationDirectory, f));
        }
    }
    public static void copyDirectoryCompatibityMode(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }
    }
}
