package de.framedev.essentialsmini.api.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.api.events
 * / ClassName PlayerKillPlayerEvent
 * / Date: 16.07.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class PlayerKillPlayerEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final String playerName;
    private final Player player;
    private final World world;
    private final Player killer;
    private final EntityDamageEvent.DamageCause deathCause;

    private boolean isCancelled;

    public PlayerKillPlayerEvent(Player player, Player killer) {
        this.player = player;
        this.playerName = player.getName();
        this.world = player.getWorld();
        this.killer = killer;
        if (player.getLastDamageCause() != null) {
            this.deathCause = player.getLastDamageCause().getCause();
        } else {
            deathCause = null;
        }
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

    public Player getKiller() {
        return killer;
    }

    public EntityDamageEvent.DamageCause getDeathCause() {
        return deathCause;
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
