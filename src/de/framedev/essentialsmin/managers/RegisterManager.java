/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts �ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.managers;

import de.framedev.essentialsmin.commands.*;
import de.framedev.essentialsmin.listeners.*;
import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.Map.Entry;
import java.util.Objects;

/**
 * @author DHZoc
 *
 */
public class RegisterManager {


    private final Main plugin;
    private BackUpCMD backup;

    public RegisterManager(Main plugin) {
        registerCommands(plugin);
        registerListeners(plugin);
        registerTabCompleters(plugin);
        this.plugin = plugin;

    }

    private void registerTabCompleters(Main plugin) {
        for (Entry<String, TabCompleter> command : plugin.getTabCompleters().entrySet()) {
            plugin.getCommand(command.getKey()).setTabCompleter(command.getValue());
        }
    }

    private void registerListeners(Main plugin) {
        new DisallowCommands(plugin);
        new SleepListener(plugin);
        new PlayerListeners(plugin);
        new MoneySignListeners(plugin);
        plugin.getListeners().forEach(listener -> {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        });
    }

    private ThunderCMD thunderCMD;

    /**
     * @param plugin
     */
    private void registerCommands(Main plugin) {
        new SpawnCMD(plugin);
        if (plugin.getConfig().getBoolean("HomeTP")) {
            new HomeCMD(plugin);
        }
        new TeleportCMD(plugin);
        new FlyCMD(plugin);
        new InvseeCMD(plugin);
        new BackCMD(plugin);
        new GameModeCMD(plugin);
        new VanishCMD(plugin);
        new WarpCMD(plugin);
        new OnlinePlayerListCMD(plugin);
        new DayNightCMD(plugin);
        new BackpackCMD(plugin);
        new SleepCMD(plugin);
        new PlayerDataCMD(plugin);
        new ItemCMD(plugin);
        new KillCMD(plugin);
        new PlayerHeadsCMD(plugin);
        new MessageCMD(plugin);
        new EnchantCMD(plugin);
        new WorkbenchCMD(plugin);
        new SunRainThunderCMD(plugin);
        new RenameItemCMD(plugin);
        new ShowLocationCMD(plugin);
        new RepairCMD(plugin);
        new HealCMD(plugin);
        new FeedCMD(plugin);
        new TrashInventory(plugin);
        new RestartCMD(plugin);
        new GenerateKeyCMD(plugin);
        new KitCMD(plugin);
        new FuckCMD(plugin);
        new LagCMD(plugin);
        if (plugin.getConfig().getBoolean("SaveInventory")) {
            new SaveInventoryCMD(plugin);
        }
        if (Bukkit.getServer().getVersion().contains("MC: 1.16.1")) {
            new ShowItemCMD(plugin);
        }
        new ShowCraftingCMD(plugin);
        new SignItemCMD(plugin);
        new WorldTPCMD(plugin);
        new GodCMD(plugin);
        new SummonCMD(plugin);
        new SetHealthCMD(plugin);
        new SpeedCMD(plugin);
        this.thunderCMD = new ThunderCMD(plugin);
        new RegisterCMD(plugin);
        new ClearChatCMD(plugin);
        this.backup = new BackUpCMD(plugin);
        if (plugin.getConfig().getBoolean("Economy.Activate"))
            new PayCMD(plugin);
        for (Entry<String, CommandExecutor> command : plugin.getCommands().entrySet()) {
            if (command != null) {
                if (command.getKey() != null && command.getValue() != null) {
                    Objects.requireNonNull(plugin.getCommand(command.getKey())).setExecutor(command.getValue());
                }
            }
        }
    }

    public BackUpCMD getBackup() {
        return backup;
    }

    public ThunderCMD getThunderCMD() {
        return thunderCMD;
    }
}
