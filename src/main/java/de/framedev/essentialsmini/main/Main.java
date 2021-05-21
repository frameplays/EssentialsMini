/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmini.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.framedev.essentialsmini.api.EssentialsMiniAPI;
import de.framedev.essentialsmini.commands.playercommands.BackpackCMD;
import de.framedev.essentialsmini.commands.playercommands.EnchantCMD;
import de.framedev.essentialsmini.commands.playercommands.SaveInventoryCMD;
import de.framedev.essentialsmini.commands.playercommands.VanishCMD;
import de.framedev.essentialsmini.commands.servercommands.LagCMD;
import de.framedev.essentialsmini.managers.*;
import de.framedev.essentialsmini.utils.*;
import de.framedev.mongodbconnections.main.MongoManager;
import de.framedev.mysqlapi.api.SQL;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main extends JavaPlugin {

    private static ArrayList<String> silent;
    /* Commands, TabCompleters and Listeners List */
    private Thread thread;
    private HashMap<String, CommandExecutor> commands;
    private HashMap<String, TabCompleter> tabCompleters;
    private ArrayList<Listener> listeners;

    private final HashMap<OfflinePlayer, PlayerManagerCfgLoss> cfgLossHashMap = new HashMap<>();
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

    /* Instance */
    private static Main instance;

    // RegisterManager
    private RegisterManager registerManager;

    /* FileM CfgM MongoDBConnection Plugin */
    public static File fileMongoDB = new File("plugins/MDBConnection/config.yml");
    public static FileConfiguration cfgMongoDB = YamlConfiguration.loadConfiguration(fileMongoDB);

    private Map<String, Object> limitedHomes;

    private boolean mysql;
    private boolean sql;


    private String currencySymbol;

    private ArrayList<String> offlinePlayers;
    private File infoFile;
    private FileConfiguration info;

    private MongoDbUtils mongoDbUtils;

    @Override
    public void onEnable() {
        instance = this;

        this.infoFile = new File(getDataFolder(), "info.yml");
        this.info = YamlConfiguration.loadConfiguration(infoFile);

        new EssentialsMiniAPI();
        createCustomMessagesConfig();
        Config.saveDefaultConfigValues("messages");
        try {
            reloadCustomConfig();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getConfig().options().header("MySQL and SQLite uses MySQLAPI[https://framedev.stream/sites/downloads/mysqlapi] \n" +
                "Position activates /position Command \n" +
                "SkipNight activates skipnight \n" +
                "LocationsBackup Activates creating Backup from all Homes \n" +
                "OnlyEssentialsFeatures activates the PlayerData saving \n" +
                "Economy.Activate activates the integration of the Vault API use for Economy");
        getConfig().options().copyHeader(true);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        Config.updateConfig();
        Config.loadConfig();
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

        // Variables
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

        if (Bukkit.getServer().getPluginManager().getPlugin("MDBConnection") != null) {
            this.mongoDbUtils = new MongoDbUtils();
            if (isMongoDb()) {
                for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                    getBackendManager().createUserMoney(player, "essentialsmini_data");
                }
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
        if (!getConfig().getBoolean("OnlyEssentialsFeatures"))
            thread = new Thread(new UpdateScheduler());
        if (thread != null)
            thread.start();

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
        /* Json Config add Key's and Value's */
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

            getJsonConfig().saveConfig();
        }
        new SaveLists().setVanished();
        /*KitManager kit = new KitManager();
        kit.saveKit("Stone");*/
        //EssentialsMiniAPI.getInstance().printAllHomesFromPlayers();

        this.mysql = getConfig().getBoolean("MySQL");
        this.sql = getConfig().getBoolean("SQLite");

        if (getConfig().getBoolean("Economy.Activate")) {
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
        matchConfig(customConfig, customConfigFile);
        try {
            this.limitedHomesPermission = getJsonConfig().getHashMap("LimitedHomesPermission");
            this.limitedHomes = getJsonConfig().getHashMap("LimitedHomes");
        } catch (Exception ignored) {
            getServer().reload();
        }
        if (getConfig().getBoolean("SendPlayerUpdateMessage")) {
            Bukkit.getOnlinePlayers().forEach(this::hasNewUpdate);
        }

        /* OfflinePlayer Register */
        this.offlinePlayers = new ArrayList<>();
        if (getJson() != null) {
            this.offlinePlayers = getJson();
        } else {
            this.offlinePlayers = new ArrayList<>();
            offlinePlayers.add("FramePlays");
            savePlayers();
        }

        if (isMysql() || isSQL() && getConfig().getBoolean("PlayerInfoSave")) {
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

        checkUpdate(getConfig().getBoolean("AutoDownload"));

        info.set("MongoDB", isMongoDb());
        info.set("MySQL", isMysql());
        info.set("isOnlineMode", getVariables().isOnlineMode());
        info.set("PlayerDataSave", getConfig().getBoolean("PlayerInfoSave"));
        info.set("Economy", getConfig().getBoolean("Economy.Activate"));
        try {
            info.save(infoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (!getCfgLossHashMap().isEmpty()) {
            getCfgLossHashMap().forEach((player, playerManagerCfgLoss) -> {
                if (playerManagerCfgLoss.getName().equalsIgnoreCase(player.getName())) {
                    if (isMysql())
                        playerManagerCfgLoss.savePlayerData(player);
                    playerManagerCfgLoss.savePlayerManager();
                }
            });
        }
        new SaveLists().saveVanishList();
        if (!VanishCMD.hided.isEmpty()) VanishCMD.hided.forEach(players -> {
            if (Bukkit.getPlayer(players) != null) {
                Objects.requireNonNull(Bukkit.getPlayer(players)).sendMessage(getPrefix() + "§cNach dem Reload wirst du nicht mehr im Vanish sein!");
            }
        });
        savePlayers();
        if (thread != null && thread.isAlive())
            thread.getThreadGroup().destroy();
    }

    public boolean isSQL() {
        return sql;
    }

    /**
     * @return the List of PlayerNames they are set to Silent
     */
    public static ArrayList<String> getSilent() {
        return silent;
    }

    /**
     * @param data the Data to Debugging
     */
    public void debug(Object data) {
        System.out.println(data);
    }

    /**
     *
     * @return returns all OfflinePlayers
     */
    public ArrayList<String> getOfflinePlayers() {
        return offlinePlayers;
    }

    public void savePlayers() {
        File file = new File(getDataFolder(), "players.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(offlinePlayers));
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        info.set("OfflinePlayers", offlinePlayers);
        try {
            info.save(infoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return returns the Info Config
     */
    public FileConfiguration getInfo() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getJson() {
        File file = new File(getDataFolder(), "players.json");
        String[] players = null;
        try {
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                players = new Gson().fromJson(reader, String[].class);
                reader.close();
                return new ArrayList<>(Arrays.asList(players));
            }
        } catch (Exception ignored) {
        }
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
        if (mongoDbUtils == null) return false;
        return this.mongoDbUtils.isMongoDb();
    }

    public boolean isMysql() {
        return mysql;
    }

    public void addOfflinePlayer(OfflinePlayer player) {
        if (!getOfflinePlayers().contains(player.getName()))
            offlinePlayers.add(player.getName());
    }

    public void removeOfflinePlayer(OfflinePlayer player) {
        if (getOfflinePlayers().contains(player.getName()))
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
        return mongoDbUtils.getMongoManager();
    }

    public BackendManager getBackendManager() {
        return mongoDbUtils.getBackendManager();
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
     * @return die Variablen die gespeichert wurden verfügbar mit dem Getter
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


        Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(Main.getInstance().getResource("messages.yml")), StandardCharsets.UTF_8);
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

    public boolean checkUpdate(boolean download) {
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "Checking for updates...");
        try {
            URLConnection conn = new URL("https://framedev.stream/sites/downloads/essentialsminiversion.txt").openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String oldVersion = Main.getInstance().getDescription().getVersion();
            String newVersion = br.readLine();
            if (!newVersion.equalsIgnoreCase(oldVersion)) {
                if(!oldVersion.contains("PRE-RELEASE")) {
                    if (download) {
                        downloadLatest();
                        Bukkit.getConsoleSender().sendMessage(getPrefix() + "Latest Version will be Downloaded");
                    }
                    Bukkit.getConsoleSender().sendMessage(getPrefix() + "A new update is available: version " + newVersion);
                    return true;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(getPrefix() + "You're running the newest plugin version!");
            }
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(getPrefix() + "Failed to check for updates on framedev.stream");
        }
        return false;
    }

    public void downloadLatest() {
        final File pluginFile = getDataFolder().getParentFile();
        final File updaterFile = new File(pluginFile, "update");
        if (!updaterFile.exists())
            updaterFile.mkdir();
        new UpdateChecker().download("https://framedev.stream/downloads/EssentialsMini-Latest.jar", getServer().getUpdateFolder(), "EssentialsMini.jar");
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
        if (prefix.contains("&"))
            prefix = prefix.replace('&', '§');
        if (prefix.contains(">>"))
            prefix = prefix.replace(">>", "»");
        return prefix;
    }

    public void hasNewUpdate(Player player) {
        if (getConfig().getBoolean("SendPlayerUpdateMessage")) {
            if (player.hasPermission("essentialsmini.checkupdates")) {
                try {
                    URLConnection conn = new URL("https://framedev.stream/sites/downloads/essentialsminiversion.txt").openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String oldVersion = Main.getInstance().getDescription().getVersion();
                    String newVersion = br.readLine();
                    if (!newVersion.equalsIgnoreCase(oldVersion)) {
                        if(!oldVersion.endsWith("PRE-RELEASE")) {
                            BaseComponent base = new TextComponent();
                            base.addExtra(getPrefix() + "§aNew Version = §6" + newVersion + " §b§l[Please Click Here to Download the newest Plugin!]");
                            base.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://framedev.stream/sites/downloads/essentialsmini"));
                            base.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Click Here to Open the Download Link").create()));
                            player.spigot().sendMessage(base);
                        }
                    }
                } catch (IOException e) {
                    player.sendMessage(getPrefix() + "Failed to check for updates on framedev.stream");
                }
            }
        }
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

    /**
     * This is used for returning the SpigotTimer for the Lag Command
     * @return returns the Lag Timer
     */
    public LagCMD.SpigotTimer getSpigotTimer() {
        return spigotTimer;
    }
}
