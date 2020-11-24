package de.framedev.essentialsmin.managers;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 21.08.2020 22:52
 */

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager {

    @NonNull
    private BarStyle barStyle;
    @NonNull
    private String title;
    @NonNull
    private BarColor barColor;
    private double progress;
    @NonNull
    private BossBar bossBar;

    /**
     * Create a new Spigot BossBar with this Class!
     * @param title the Title from the BossBar
     * @param barColor the Color from the BossBar
     * @param barStyle the Style from the BossBar
     */
    public BossBarManager(@NonNull String title, @NonNull BarColor barColor,@NonNull BarStyle barStyle) {
        this.title = title;
        this.barColor = barColor;
        this.barStyle = barStyle;
        if(title == null) {
            throw new NullPointerException("Title cannot be null!");
        }
        if(barColor == null) {
            throw  new NullPointerException("BarColor cannot be Null!");
        }
        if(barStyle == null) {
            throw new NullPointerException("BarStyle cannot be Null!");
        }
    }

    /**
     *
     * @param title the Title from the BossBar
     * BarColor is Blue
     * BarStyle is Segmented_12
     */
    public BossBarManager(@NonNull String title) {
        this.title = title;
        this.barStyle = BarStyle.SEGMENTED_12;
        this.barColor = BarColor.BLUE;
        if(title == null) {
            throw new NullPointerException("Title cannot be null!");
        }
    }

    public void setBarStyle(BarStyle barStyle) {
        this.barStyle = barStyle;
    }

    public void setBarColor(BarColor barColor) {
        this.barColor = barColor;
    }

    /**
     *
     * @param progress set the Progress of the BossBar
     */
    public BossBarManager setProgress(double progress) {
        this.progress = progress;
        return this;
    }

    /**
     *
     * @return the Progress of the BossBar
     */
    public double getProgress() {
        return progress;
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }

    public String getTitle() {
        return title;
    }

    public BossBarManager setTitle(@NonNull String title) {
        this.title = title;
        return this;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public BossBarManager setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
        return this;
    }

    /**
     *
     * @param player the Player how need to be Added to the BossBar
     * @return this(BossBarManager)
     */
    public BossBarManager addPlayer(Player player) {
        bossBar.addPlayer(player);
        return this;
    }

    public BossBarManager removePlayer(Player player) {
        bossBar.removePlayer(player);
        return this;
    }

    /**
     * This Method creates the BossBar after this you can add Players tho the BossBar!
     * @throws NullPointerException if Title,BarColor or BarStyle is Null
     */
    public BossBarManager create() throws NullPointerException {
        this.bossBar = Bukkit.createBossBar(title, barColor, barStyle);
        return this;
    }

    /**
     * This Method update the Title and the Progress!
     */
    public BossBarManager update() {
        bossBar.setTitle(title);
        bossBar.setProgress(progress);
        return this;
    }

    public BossBarManager remove() {
        this.bossBar.hide();
        this.bossBar.setVisible(false);
        return this;
    }

    /**
     * Does not working
     */
    public static void removeAll() {
        if(Bukkit.getBossBars().hasNext()) {
            Bukkit.getBossBars().remove();
        }
    }
}
