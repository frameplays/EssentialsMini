package de.framedev.essentialsmin.managers;


/*
 * de.framedev.essentialsmin.managers
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 23.09.2020 19:13
 */

import de.framedev.essentialsmin.main.Main;
import org.bukkit.event.Listener;

public abstract class ListenerBase implements Listener {

    private final Main plugin;

    public void setupListener(Listener listener) {
        plugin.getListeners().add(listener);
    }

    public ListenerBase(Main plugin) {
        this.plugin = plugin;
    }

    public Main getPlugin() {
        return plugin;
    }
}
