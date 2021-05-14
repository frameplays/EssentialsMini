package de.framedev.essentialsmini.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 13.08.2020 20:07
 */

import com.google.gson.Gson;
import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class WorkbenchCMD extends CommandBase {

    private final Main plugin;

    public WorkbenchCMD(Main plugin) {
        super(plugin, "workbench");
        this.plugin = plugin;
        setup(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length == 0) {
                if (sender.hasPermission(plugin.getPermissionName() + "workbench")) {
                    ((Player) sender).openWorkbench(((Player) sender).getLocation(), true);
                    /*writeObject(EssentialsMiniAPI.getInstance().toJson(plugin.getVariables()), new File(plugin.getDataFolder(),"variables.json"));
                    Variables variables = (Variables) getObject(new File(plugin.getDataFolder(),"variables.json"),Variables.class);
                    String s = variables.toString().replace('§','&');
                    sender.sendMessage(s);*/
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/workbench"));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }

    private static void writeObject(String json, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getObject(File file,Class<?> s) {
        try {
            FileReader fileReader = new FileReader(file);
            return new Gson().fromJson(fileReader, s);
        } catch (Exception ignored) {

        }
        return null;
    }
}
