/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.NotNull;
import de.framedev.essentialsmin.api.EssentialsMiniAPI;
import de.framedev.essentialsmin.commands.playercommands.BackpackCMD;
import de.framedev.essentialsmin.commands.playercommands.EnchantCMD;
import de.framedev.essentialsmin.commands.playercommands.SaveInventoryCMD;
import de.framedev.essentialsmin.commands.playercommands.VanishCMD;
import de.framedev.essentialsmin.commands.servercommands.LagCMD;
import de.framedev.essentialsmin.managers.*;
import de.framedev.essentialsmin.utils.*;
import de.framedev.mysqlapi.api.SQL;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main extends JavaPlugin {

    private static ArrayList<String> silent;
    /* Commands, TabCompleters and Listeners List */
    Thread thread;
    private HashMap<String, CommandExecutor> commands;
    private HashMap<String, TabCompleter> tabCompleters;
    private ArrayList<Listener> listeners;

    private HashMap<OfflinePlayer, PlayerManagerCfgLoss> cfgLossHashMap = new HashMap<>();
    private Map<String, Object> limitedHomesPermission;

    /* Json Config.json */
    private JsonConfig jsonConfig;

    private boolean homeTP = false;

    /* Material Manager */
    private MaterialManager materialManager;
    private Variables variables;
    private KeyGenerator keyGenerator;

    private VaultManager vaultManager;
    /* Custom Config File */
    private File customConfigFile;
    private FileConfiguration customConfig;

    private LagCMD.SpigotTimer spigotTimer;

    public ArrayList<String> players;

    /* MongoDB */
    private MongoManager mongoManager;
    private BackendManager backendManager;

    /* Instance */
    private static Main instance;
    private RegisterManager registerManager;

    /* FileM CfgM MongoDBConnection Plugin */
    public static File filem = new File("plugins/MDBConnection/config.yml");
    public static FileConfiguration cfgm = YamlConfiguration.loadConfiguration(filem);

    private Map<String, Object> limitedHomes;

    private boolean mysql;

    private boolean mongoDb = false;

    private String currencySymbol;

    private ArrayList<String> offlinePlayers;
    private YAMLConfigurator info;

    @Override
    public void onEnable() {
        instance = this;

        this.info = new YAMLConfigurator("info");

        new EssentialsMiniAPI();
        createCustomMessagesConfig();
        try {
            reloadCustomConfig();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        Config.loadConfig();
        Config.updateConfig();
        Config.saveDefaultConfigValues();

        if (getConfig().getBoolean("HomeTP")) {
            homeTP = true;
        }

        /* Listener Initialling */
        this.commands = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.tabCompleters = new HashMap<>();

        /* MaterialManager initialling */
        this.materialManager = new MaterialManager();
        this.materialManager.saveMaterials();
        this.materialManager.saveMaterialToJson();

        /* TPS Command Timer */
        this.spigotTimer = new LagCMD.SpigotTimer();

        this.variables = new Variables();

        this.keyGenerator = new KeyGenerator();

        new KitManager().createCustomConfig();

        /* JsonConfig */
        this.jsonConfig = new JsonConfig();

        silent = new ArrayList<>();

        /*this.file = new File(getDataFolder(), "offline.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
        if (!cfg.contains("players")) {
            players = new ArrayList<>();
            cfg.set("players", players);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.players = (ArrayList<String>) cfg.getStringList("players");
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (!players.contains(offlinePlayer.getName())) {
                players.add(offlinePlayer.getName());
            }
        }
        cfg.set("players", players);
        saveCfg();*/


        /* MongoDB */
        this.mongoManager = new MongoManager();
        if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
            if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                this.mongoDb = true;
            }
        }
        if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
            if (cfgm.getBoolean("MongoDB.LocalHost")) {
                this.mongoManager = new MongoManager();
                this.mongoManager.connectLocalHost();
            }
            if (cfgm.getBoolean("MongoDB.Boolean")) {
                this.mongoManager = new MongoManager();
                this.mongoManager.connect();
            }
            if (cfgm.getBoolean("MongoDB.LocalHost")) {
                this.backendManager = new BackendManager(this);
            }
            if (cfgm.getBoolean("MongoDB.Boolean")) {
                this.backendManager = new BackendManager(this);
            }
        }

        if(isMongoDb()) {
            for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                backendManager.createUserMoney(player,"test");
            }
        }
        /* MongoDB Finish */

        // Load Enchantments
        EnchantCMD.Enchantments.load();

        if (this.getConfig().getBoolean("SaveInventory")) {
            new SaveInventoryCMD(this);
        }

        // LocationBackup
        if (getConfig().getBoolean("LocationsBackup")) {
            Bukkit.getConsoleSender().sendMessage(getPrefix() + "§aLocation Backups werden gemacht!");
        }
        /* Thread for the Schedulers for save restart and .... */
        if(!getConfig().getBoolean("OnlyEssentialsFeatures"))
            new UpdateScheduler().run();

        if (this.getConfig().getBoolean("SaveInventory")) {
            SaveInventoryCMD.restore();
        }

        HashMap<String, Integer> limitedHomes = new HashMap<>();
        ConfigurationSection cs = getConfig().getConfigurationSection("LimitedHomes");
        if (cs != null) {
            for (String s : cs.getKeys(false)) {
                limitedHomes.put(s, getConfig().getInt("LimitedHomes." + s));
            }
        }
        HashMap<String, String> limitedHomesPermissions = new HashMap<>();
        ConfigurationSection css = getConfig().getConfigurationSection("LimitedHomesPermission");
        if (css != null) {
            for (String s : css.getKeys(false)) {
                limitedHomesPermissions.put(s, getConfig().getString("LimitedHomesPermission." + s));
            }
        }
        if(variables.isJsonFormat()) {
            /* Json Config at Key's and Value's */
            HashMap<String, Object> json = new HashMap<>();
            json.put("Backpack", true);
            json.put("SpawnTP", false);
            json.put("SkipNight", false);
            json.put("LocationsBackup", false);
            json.put("BackupTime", 5);
            json.put("LocationsBackupMessage", false);
            json.put("IgnoreJoinLeave", false);
            json.put("Limited", false);
            json.put("LimitedHomes", limitedHomes);
            json.put("LimitedHomesPermission", limitedHomesPermissions);
            json.put("HomeTP", true);
            json.put("ShowItem", true);
            json.put("ShowCrafting", true);
            json.put("ShowLocation", true);
            json.put("Position", true);
            json.put("JsonFormat", true);
            json.put("BackupMessages", true);
            json.put("SendPlayerUpdateMessage", true);
            json.put("ZeitgesteuerterRestart", 60);
            json.put("ZeitGesteuerterRestartBoolean", true);
            if (!getJsonConfig().contains("Prefix")) {
                for (Map.Entry<String, Object> entry : json.entrySet()) {
                    getJsonConfig().set(entry.getKey(), entry.getValue());
                }
                getJsonConfig().set("Prefix", "§6[§aEssentials§bMini§6] §c» §f");
                getJsonConfig().set("JoinBoolean", true);
                getJsonConfig().set("LeaveBoolean", true);
                getJsonConfig().set("SaveInventory", false);
            }

            getJsonConfig().saveConfig();
        }
        new SaveLists().setVanished();
        /*KitManager kit = new KitManager();
        kit.saveKit("Stone");*/
        //EssentialsMiniAPI.getInstance().printAllHomesFromPlayers();

        this.mysql = getConfig().getBoolean("MySQL");

        if(getConfig().getBoolean("Economy.Activate")) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                this.vaultManager = new VaultManager(this);
            }
        }

        this.registerManager = new RegisterManager(this);
        registerManager.getBackup().makeBackups();
        // BackPack restore
        if (getConfig().getBoolean("Backpack")) {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                BackpackCMD.restore(offlinePlayer);
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                BackpackCMD.restore(onlinePlayer);
            }
        }

        //saveCfg();
        this.currencySymbol = (String) getConfig().get("Currency");
        saveCustomMessagesConfig();
        try {
            reloadCustomConfig();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        matchConfig(customConfig,customConfigFile);
        try {
            this.limitedHomesPermission = getJsonConfig().getHashMap("LimitedHomesPermission");
            this.limitedHomes = getJsonConfig().getHashMap("LimitedHomes");
        } catch (Exception ignored) {
            getServer().reload();
        }
        if (getConfig().getBoolean("SendPlayerUpdateMessage")) {
            Bukkit.getOnlinePlayers().forEach(this::hasNewUpdate);
        }

        this.offlinePlayers = new ArrayList<>();
        if(getJson() != null) {
            this.offlinePlayers = getJson();
        } else {
            this.offlinePlayers = new ArrayList<>();
            offlinePlayers.add("FramePlays");
            savePlayers();
        }
        if(isMysql() && getConfig().getBoolean("PlayerInfoSave")) {
            if (!SQL.isTableExists(getName().toLowerCase() + "_data")) {
                SQL.createTable(getName().toLowerCase() + "_data",
                        "playeruuid VARCHAR(1200)",
                        "playername TEXT(120)",
                        "sleeptimes INT",
                        "damage DOUBLE",
                        "playerkills INT",
                        "entitykills INT",
                        "deaths INT",
                        "blocksbroken INT",
                        "blocksplacen INT",
                        "lastlogin LONG",
                        "lastlogout LONG",
                        "commandsused INT");
                Bukkit.getConsoleSender().sendMessage(getPrefix() + "§aMySQL Table Created!");
            }
        }
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "§awurde geladen!");
        checkUpdate();
        info.set("MongoDB",isMongoDb());
        info.set("MySQL",isMysql());
        info.set("isOnlineMode",getVariables().isOnlineMode());
        info.set("PlayerDataSave",getConfig().getBoolean("PlayerInfoSave"));
        info.set("Economy", getConfig().getBoolean("Economy.Activate"));
        info.saveConfig();
    }

    @Override
    public void onDisable() {
        if (this.getConfig().getBoolean("SaveInventory")) {
            SaveInventoryCMD.save();
        }
        if (!BackpackCMD.itemsStringHashMap.isEmpty()) {
            if (getConfig().getBoolean("Backpack")) {
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    BackpackCMD.save(offlinePlayer);
                }
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    BackpackCMD.save(onlinePlayer);
                }
            }
        }
        if (getConfig().getBoolean("LocationsBackup")) {
            new LocationsManager().saveBackup();
        }
        new LocationsManager().deleteLocations();
        if(!getCfgLossHashMap().isEmpty()) {
            getCfgLossHashMap().forEach((player, playerManagerCfgLoss) -> {
                if (playerManagerCfgLoss.getName().equalsIgnoreCase(player.getName())) {
                    if(isMysql())
                        playerManagerCfgLoss.savePlayerData(player);
                    playerManagerCfgLoss.savePlayerManager();
                }
            });
        }
        new SaveLists().saveVanishList();
        if (!VanishCMD.hided.isEmpty()) {
            VanishCMD.hided.forEach(players -> {
                if (Bukkit.getPlayer(players) != null) {
                    Bukkit.getPlayer(players).sendMessage(getPrefix() + "§cNach dem Reload wirst du nicht mehr im Vanish sein!");
                }
            });
        }
        savePlayers();
    }

    /**
     *
     * @return the List of PlayerNames they are set to Silent
     */
    public static ArrayList<String> getSilent() {
        return silent;
    }

    /**
     *
     * @param data the Data to Debugging
     */
    public void debug(Object data) {
        System.out.println(data);
    }

    public ArrayList<String> getOfflinePlayers() {
        return offlinePlayers;
    }

    public void savePlayers() {
        File file = new File(getDataFolder(),"players.json");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(offlinePlayers));
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public YAMLConfigurator getInfo() {
        return info;
    }

    public void matchConfig(FileConfiguration config, File file) {
        try {
            InputStream is = getResource(file.getName());
            if (is != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(file);
                for (String key : defConfig.getConfigurationSection("").getKeys(false))
                    if (!config.contains(key)) config.set(key, defConfig.getConfigurationSection(key));

                for (String key : config.getConfigurationSection("").getKeys(false))
                    if (!defConfig.contains(key)) config.set(key, null);

                config.save(file);
            }
            if(this.info.contains("Economy"))
                System.out.println("Nein!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getJson() {
        File file = new File(getDataFolder(),"players.json");
        String[] players = null;
        try {
            if(file.exists()) {
                FileReader reader = new FileReader(file);
                players = new Gson().fromJson(reader, String[].class);
                reader.close();
                return new ArrayList<>(Arrays.asList(players));
            }
        } catch (Exception ignored) {}
        return null;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public boolean isMongoDb() {
        return mongoDb;
    }

    public boolean isMysql() {
        return mysql;
    }

    public void addOfflinePlayer(OfflinePlayer player) {
        if(!getOfflinePlayers().contains(player.getName()))
            offlinePlayers.add(player.getName());
    }

    public void removeOfflinePlayer(OfflinePlayer player) {
        if(getOfflinePlayers().contains(player.getName()))
            offlinePlayers.remove(player.getName());
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public Thread getThread() {
        return thread;
    }

    public File getCustomConfigFile() {
        return customConfigFile;
    }

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }

    public HashMap<OfflinePlayer, PlayerManagerCfgLoss> getCfgLossHashMap() {
        return cfgLossHashMap;
    }

    public FileConfiguration getCustomMessagesConfig() {
        return customConfig;
    }

    public JsonConfig getJsonConfig() {
        return jsonConfig;
    }

    public void createCustomMessagesConfig() {
        customConfigFile = new File(Main.getInstance().getDataFolder(), "messages.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("messages.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return die variablen die gespeichert wurden verfügbar mit dem Getter
     */
    public Variables getVariables() {
        return variables;
    }

    public String getOnlyPlayer() {
        String onlyPlayer = getCustomMessagesConfig().getString("OnlyPlayer");
        onlyPlayer = onlyPlayer.replace('&', '§');
        return onlyPlayer;
    }

    public void saveCustomMessagesConfig() {
        try {
            customConfig.save(customConfigFile = new File(Main.getInstance().getDataFolder(), "messages.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWrongArgs(String cmdName) {
        String wrongArgs = getCustomMessagesConfig().getString("WrongArgs");
        wrongArgs = wrongArgs.replace("%cmdUsage%", cmdName);
        wrongArgs = wrongArgs.replace('&', '§');
        return wrongArgs;
    }

    public void reloadCustomConfig() throws UnsupportedEncodingException {
        if (customConfig == null) ;

        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);


        Reader defConfigStream = new InputStreamReader(Main.getInstance().getResource("messages.yml"), StandardCharsets.UTF_8);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }


    public String getNOPERMS() {
        String NOPERMS = getCustomMessagesConfig().getString("NoPermissions");
        NOPERMS = NOPERMS.replace('&', '§');
        return NOPERMS;
    }

    public boolean isHomeTP() {
        return homeTP;
    }

    public void checkUpdate() {
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "Checking for updates...");
        try {
            int resource = 82775;
            URLConnection conn = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resource).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String oldVersion = Main.getInstance().getDescription().getVersion();
            String newVersion = br.readLine();
            if (!newVersion.equalsIgnoreCase(oldVersion)) {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + "A new update is available: version " + newVersion);
            } else {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + "You're running the newest plugin version!");
            }
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(getPrefix() + "Failed to check for updates on spigotmc.org");
        }
    }

    public MaterialManager getMaterialManager() {
        return materialManager;
    }

    public String getPermissionName() {
        return "essentialsmini.";
    }


    /**
     * @return the TabCompleters
     */
    public HashMap<String, TabCompleter> getTabCompleters() {
        return tabCompleters;
    }

    /**
     * @return the commands
     */
    public HashMap<String, CommandExecutor> getCommands() {
        return commands;
    }

    /**
     * @return the Listeners
     */
    public ArrayList<Listener> getListeners() {
        return listeners;
    }

    /**
     * @return the Prefix
     */
    public String getPrefix() {
        String prefix = getConfig().getString("Prefix");
        if (prefix == null) {
            throw new NullPointerException("Perfix cannot be Found in Config.yml");
        }
        prefix = prefix.replace('&', '§');
        prefix = prefix.replace(">>", "»");
        return prefix;
    }

    public boolean hasNewUpdate(@NotNull Player player) {
        if (getConfig().getBoolean("SendPlayerUpdateMessage")) {
            if (player.hasPermission("essentialsmini.checkupdates")) {
                try {
                    int resource = 82775;
                    URLConnection conn = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resource).openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String oldVersion = Main.getInstance().getDescription().getVersion();
                    String newVersion = br.readLine();
                    if (!newVersion.equalsIgnoreCase(oldVersion)) {
                        BaseComponent base = new TextComponent();
                        base.addExtra(getPrefix() + "§aNew Version = §6" + newVersion + " §b§l[Please Click Here to Download the newest Plugin!]");
                        base.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/essentialsmini.82775"));
                        base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Click Here to Open the Download Link").create()));
                        player.spigot().sendMessage(base);
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    player.sendMessage(getPrefix() + "Failed to check for updates on spigotmc.org");
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * @return the instance (this Plugin)
     */
    public static Main getInstance() {
        return instance;
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    public Map<String, String> getLimitedHomesPermission() {
        Map<String, String> limited = new HashMap<>();
        for (Map.Entry<String, Object> entry : limitedHomesPermission.entrySet()) {
            limited.put(entry.getKey(), (String) entry.getValue());
        }
        return limited;
    }

    public Map<String, String> getLimitedHomes() {
        Map<String, String> limited = new HashMap<>();
        for (Map.Entry<String, Object> entry : limitedHomes.entrySet()) {
            limited.put(entry.getKey(), (String) entry.getValue());
        }
        return limited;
    }

    public LagCMD.SpigotTimer getSpigotTimer() {
        return spigotTimer;
    }
}
