package de.framedev.essentialsmini.api.events;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.api.events
 * / ClassName PlayerKillEntityEvent
 * / Date: 16.07.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class PlayerKillEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String playerName;
    private final Player player;
    private final World world;
    private final Entity entity;

    private boolean isCancelled;

    public PlayerKillEntityEvent(Player player, Entity entity) {
        this.player = player;
        this.playerName = player.getName();
        this.world = player.getWorld();
        this.entity = entity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public World getWorld() {
        return world;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}