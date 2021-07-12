package de.framedev.essentialsmini.listeners;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.ListenerBase;
import de.framedev.essentialsmini.managers.LocationsManager;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.listeners
 * / ClassName WarpSigns
 * / Date: 12.07.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class WarpSigns extends ListenerBase {
    public WarpSigns(Main plugin) {
        super(plugin);
        setupListener(this);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(event.getLine(0).equalsIgnoreCase("warp")) {
            for(String location : new LocationsManager().getWarpNames()) {
                if(event.getLine(1).equalsIgnoreCase(location)) {
                    event.setLine(0, "§6[§bWARP§6]");
                    event.setLine(1, "§a" + location);
                }
            }
        }
    }

    @EventHandler
    public void onClickWarp(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getHand() == EquipmentSlot.OFF_HAND) {
                return;
            }
            if (event.getHand().equals(EquipmentSlot.HAND) &&
                    event.getClickedBlock().getState() instanceof Sign) {
                Sign s = (Sign) event.getClickedBlock().getState();
                String[] lines = s.getLines();
                if(lines[0].equalsIgnoreCase("§6[§bWARP§6]")) {
                    String warpName = lines[1];
                    Location location = new LocationsManager().getLocation(warpName);
                    if (event.getPlayer().hasPermission("essentialsmini.warp"))
                        event.getPlayer().teleport(location);
                    event.setCancelled(true);
                }
            }
        }
    }
}
