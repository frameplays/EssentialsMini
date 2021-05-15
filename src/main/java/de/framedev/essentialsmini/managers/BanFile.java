/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts �ndern, @Copyright by FrameDev 
 */
package de.framedev.essentialsmini.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Darryl
 *
 */
public class BanFile {
	
	public static File file = new File("plugins/SpigotTest/Banned.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static void saveCFG(String playername) {
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void banPlayer(String playername, String reason) {
		if(cfg.getBoolean(playername + ".isBanned")) {
			Bukkit.getConsoleSender().sendMessage(playername + " ist schon gebannt!");
		} else {
			cfg.set(playername + ".isBanned", true);
			cfg.set(playername + ".reason", reason);
			saveCFG(playername);
			if(!file.exists()) {
				try {
				file.mkdir();
				} catch (Exception e) {
					
				}
				
			}
		}
	}
	public static void UnBanned(String playername) {
		if(cfg.getBoolean(playername + ".isBanned") == false) {
			Bukkit.getConsoleSender().sendMessage(playername + " ist nicht gebannt!");
		} else {
			cfg.set(playername + ".isBanned", false);
			saveCFG(playername);
		}
	}

}
