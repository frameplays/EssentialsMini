package de.framedev.essentialsmini.api.events;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.api.events
 * / ClassName PlayerKillEntityEvent
 * / Date: 16.07.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class PlayerKillEntityEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String playerName;
    private final Player player;
    private final World world;
    private final Entity entity;
    private double droppedExp;
    private List<ItemStack> drops;

    public PlayerKillEntityEvent(Player player, Entity entity, List<ItemStack> drops, double droppedExp) {
        this.player = player;
        this.playerName = player.getName();
        this.world = player.getWorld();
        this.entity = entity;
        this.drops = drops;
        this.droppedExp = droppedExp;
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

    public double getDroppedExp() {
        return droppedExp;
    }

    public void setDroppedExp(double droppedExp) {
        this.droppedExp = droppedExp;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }
}