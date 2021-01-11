/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.internal.NotNull;
import de.framedev.essentialsmin.api.EssentialsMiniAPI;
import de.framedev.essentialsmin.commands.*;
import de.framedev.essentialsmin.managers.*;
import de.framedev.essentialsmin.utils.*;
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
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main extends JavaPlugin {

    /* Commands, TabCompleters and Listeners List */
    Thread thread;
    private HashMap<String, CommandExecutor> commands;
    private HashMap<String, TabCompleter> tabCompleters;
    private ArrayList<Listener> listeners;

    private HashMap<OfflinePlayer, PlayerManagerCfgLoss> cfgLossHashMap = new HashMap<>();
    private Map<String, Object> limitedHomesPermission;
    public static HashMap<String, HashMap<String, Home>> homes;

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

    private JsonHandler playerJson;
    private LagCMD.SpigotTimer spigotTimer;

    private File file;
    private FileConfiguration cfg;

    public ArrayList<String> players;

    private final HashMap<UUID, BukkitTask> saver = new HashMap<>();

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

    @Override
    public void onEnable() {
        instance = this;
        setupEconomy();
        homes = new HashMap<>();

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

        this.spigotTimer = new LagCMD.SpigotTimer();

        this.keyGenerator = new KeyGenerator();

        new KitManager().createCustomConfig();

        /* JsonConfig */
        this.jsonConfig = new JsonConfig();

        this.file = new File(getDataFolder(), "offline.yml");
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
        saveCfg();

        /* Variables */
        this.variables = new Variables();

        /* MongoDB */
        this.mongoManager = new MongoManager();
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
        new UpdateScheduler().run();

        if (this.getConfig().getBoolean("SaveInventory")) {
            SaveInventoryCMD.restore();
        }

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
        HashMap<String, Integer> limitedHomes = new HashMap<>();
        ConfigurationSection cs = getConfig().getConfigurationSection("LimitedHomes");
        if (cs != null) {
            for (String s : cs.getKeys(false)) {
                limitedHomes.put(s, getConfig().getInt("LimitedHomes." + s));
            }
        }
        json.put("LimitedHomes", limitedHomes);
        HashMap<String, String> limitedHomesPermissions = new HashMap<>();
        ConfigurationSection css = getConfig().getConfigurationSection("LimitedHomesPermission");
        if (css != null) {
            for (String s : css.getKeys(false)) {
                limitedHomesPermissions.put(s, getConfig().getString("LimitedHomesPermission." + s));
            }
        }
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
        new SaveLists().setVanished();
        /*KitManager kit = new KitManager();
        kit.saveKit("Stone");*/
        //EssentialsMiniAPI.getInstance().printAllHomesFromPlayers();
        this.mysql = getConfig().getBoolean("MySQL");
        if (Bukkit.getPluginManager().getPlugin("MDBConnection") != null) {
            if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                this.mongoDb = true;
            }
        }
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
        saveCfg();
        this.currencySymbol = (String) getConfig().get("Currency");
        try {
            reloadCustomConfig();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        saveCustomMessagesConfig();
        this.limitedHomesPermission = getJsonConfig().getHashMap("LimitedHomesPermission");
        this.limitedHomes = getJsonConfig().getHashMap("LimitedHomes");
        if (getConfig().getBoolean("SendPlayerUpdateMessage")) {
            Bukkit.getOnlinePlayers().forEach(this::hasNewUpdate);
        }
        Bukkit.getConsoleSender().sendMessage(getPrefix() + "§awurde geladen!");
        checkUpdate();
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

    private boolean setupEconomy() {
        /*if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        */
        //
        return true;
    }

    public FileConfiguration getCfg() {
        return cfg;
    }

    public void saveCfg() {
        try {
            this.cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public JsonHandler getPlayerJson() {
        return playerJson;
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

    public HashMap<UUID, BukkitTask> getSaver() {
        return saver;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("essentialsmini")) {
            if (sender.hasPermission("essentialsmini.utils")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        this.getServer().getPluginManager().disablePlugin(this);
                        this.getServer().getPluginManager().enablePlugin(this);
                        reloadConfig();
                        Config.updateConfig();
                        Config.loadConfig();
                        sender.sendMessage(getPrefix() + "§aConfig wurde reloaded!");
                    }
                    if (args[0].equalsIgnoreCase("restart")) {
                        thread.stop();
                        thread.start();
                        sender.sendMessage(getPrefix() + "§aScheduler Restartet!");
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("spawntp")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("SpawnTP", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6SpawnTP §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("backpack")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("Backpack", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6Backpack §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("saveinventory")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("SaveInventory", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6SaveInventory §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("back")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("Back", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6Back §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("skipnight")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("SkipNight", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6SkipNight §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("showlocation")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("ShowLocation", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6ShowLocation §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("showcrafting")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("ShowCrafting", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6ShowCrafting §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("showitem")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("ShowItem", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6ShowItem §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("position")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("Position", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6Position §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("jsonformat")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("JsonFormat", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6JsonFormat §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("backupmessages")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("BackupMessages", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6BackupMessages §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if (args[0].equalsIgnoreCase("autorestart")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("ZeitGesteuerterRestartBoolean", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6Auto Restart §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if(args[0].equalsIgnoreCase("worldbackup")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("WorldBackup", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6WorldBackup §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if(args[0].equalsIgnoreCase("economy")) {
                        boolean isSet = Boolean.parseBoolean(args[1]);
                        getConfig().set("Economy.Activate", isSet);
                        saveConfig();
                        sender.sendMessage(getPrefix() + "§6Economy §awurde auf §6" + isSet + " §agesetzt!");
                    }
                    if(args[0].equalsIgnoreCase("info")) {
                        boolean jsonFormat = getConfig().getBoolean("JsonFormat");
                        boolean economyEnabled = getConfig().getBoolean("Economy.Activate");
                        sender.sendMessage(getPrefix());
                        sender.sendMessage("§ais JsonFormat Enabled §6: " + jsonFormat);
                        sender.sendMessage("§ais Economy Enabled §6: " + economyEnabled);
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("essentialsmini")) {
            if (args.length == 1) {
                if (sender.hasPermission("essentialsmini.utils")) {
                    ArrayList<String> cmds = new ArrayList<>();
                    ArrayList<String> empty = new ArrayList<>();
                    cmds.add("backupmessages");
                    cmds.add("autorestart");
                    cmds.add("reload");
                    cmds.add("back");
                    cmds.add("backpack");
                    cmds.add("saveinventory");
                    cmds.add("skipnight");
                    cmds.add("spawntp");
                    cmds.add("showlocation");
                    cmds.add("showcrafting");
                    cmds.add("showitem");
                    cmds.add("position");
                    cmds.add("jsonformat");
                    cmds.add("worldbackup");
                    cmds.add("economy");
                    for (String s : cmds) {
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            empty.add(s);
                        }
                    }
                    Collections.sort(empty);
                    return empty;
                }
            } else if (args.length == 2) {
                if (!args[0].equalsIgnoreCase("reload")) {
                    ArrayList<String> cmds = new ArrayList<>();
                    ArrayList<String> empty = new ArrayList<>();
                    cmds.add("true");
                    cmds.add("false");
                    for (String s : cmds) {
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                            empty.add(s);
                        }
                    }
                    Collections.sort(empty);
                    return empty;
                }
            }
        }
        return super.onTabComplete(sender, command, alias, args);
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
        getCfgLossHashMap().forEach((player, playerManagerCfgLoss) -> {
            if (playerManagerCfgLoss.getName().equalsIgnoreCase(player.getName())) {
                playerManagerCfgLoss.savePlayerManager();
            }
        });
        new SaveLists().saveVanishList();
        if (!VanishCMD.hided.isEmpty()) {
            VanishCMD.hided.forEach(players -> {
                if (Bukkit.getPlayer(players) != null) {
                    Bukkit.getPlayer(players).sendMessage(getPrefix() + "§cNach dem Reload wirst du nicht mehr im Vanish sein!");
                }
            });
        }
        savePlayerHomes();
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

    public void savePlayerHomes() {
        File file = new File(getDataFolder(), "homes.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(homes));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LagCMD.SpigotTimer getSpigotTimer() {
        return spigotTimer;
    }

    public HashMap<String, HashMap<String, Home>> getPlayerHomes() {
        File file = new File(getDataFolder(), "homes.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HashMap<String, HashMap<String, Home>> homes = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(file);
            Type type = new TypeToken<HashMap<String, HashMap<String, Home>>>() {
            }.getType();
            homes = new Gson().fromJson(fileReader, type);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return homes;
    }
}
