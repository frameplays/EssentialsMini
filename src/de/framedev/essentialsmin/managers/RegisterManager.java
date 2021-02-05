/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts �ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.managers;

import de.framedev.essentialsmin.commands.playercommands.*;
import de.framedev.essentialsmin.commands.servercommands.*;
import de.framedev.essentialsmin.commands.worldcommands.*;
import de.framedev.essentialsmin.listeners.*;
import de.framedev.essentialsmin.main.Main;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Objects;

public class RegisterManager {


    private final Main plugin;
    private BackUpCMD backup;

    public RegisterManager(Main plugin) {
        this.plugin = plugin;
        registerCommands();
        registerListeners();
        registerTabCompleters();

    }

    public void data() {
        YAMLConfigurator data = plugin.getInfo();
        HashMap<String,Object> dataYml = data.getData();
        System.out.println(dataYml);
    }

    private void registerTabCompleters() {
        plugin.getTabCompleters().forEach((key, value) -> plugin.getCommand(key).setTabCompleter(value));
    }

    private void registerListeners() {
        new DisallowCommands(plugin);
        new SleepListener(plugin);
        new PlayerListeners(plugin);
        new MoneySignListeners(plugin);
        plugin.getListeners().forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

    private ThunderCMD thunderCMD;

    /**
     * @param plugin
     */
    private void registerCommands() {
        new EssentialsMiniCMD(plugin);
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
        new MySQLCMD(plugin);
        new EconomyCMD(plugin);
        if (plugin.getConfig().getBoolean("Economy.Activate"))
            new PayCMD(plugin);
        if (plugin.getConfig().getBoolean("AFK.Boolean"))
            new AFK(plugin);
        plugin.getCommands().entrySet().stream().filter(Objects::nonNull).filter(command -> command.getKey() != null && command.getValue() != null).forEach(command -> Objects.requireNonNull(plugin.getCommand(command.getKey())).setExecutor(command.getValue()));
    }

    public BackUpCMD getBackup() {
        return backup;
    }

    public ThunderCMD getThunderCMD() {
        return thunderCMD;
    }
}
