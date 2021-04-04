package de.framedev.essentialsmin.api;

import com.google.gson.GsonBuilder;
import de.framedev.essentialsmin.commands.playercommands.BackpackCMD;
import de.framedev.essentialsmin.commands.playercommands.GameModeCMD;
import de.framedev.essentialsmin.commands.playercommands.RegisterCMD;
import de.framedev.essentialsmin.commands.worldcommands.WorldTPCMD;
import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.*;
import de.framedev.essentialsmin.utils.InventoryStringDeSerializer;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class EssentialsMiniAPI {

    private final Main plugin;
    private static  EssentialsMiniAPI essentialsMiniAPI;
    private final boolean jsonFormat;
    private final boolean economy;

    public EssentialsMiniAPI() {
        this.plugin = Main.getInstance();
        essentialsMiniAPI = this;
        jsonFormat = plugin.getConfig().getBoolean("JsonFormat");
        economy = plugin.getVaultManager() != null;
    }

    public boolean isEconomy() {
        return economy;
    }

    public boolean isJsonFormat() {
        return jsonFormat;
    }

    public static EssentialsMiniAPI getInstance() {
        return essentialsMiniAPI;
    }

    public boolean hasPlayerKey(OfflinePlayer player) {
        if(plugin.isMongoDb()) {
            if (Main.cfgm.getBoolean("MongoDB.LocalHost") || Main.cfgm.getBoolean("MongoDB.Boolean")) {
                return plugin.getBackendManager().exists(player, "key", "test");
            }
        }
        plugin.getKeyGenerator().loadCfg();
        return plugin.getKeyGenerator().hasPlayerKey(player);
    }

    public boolean canPlayerFly(Player player) {
        return player.getAllowFlight();
    }

    public HashMap<String,Location> getPlayerHomes(OfflinePlayer player) throws NullPointerException {
        HashMap<String,Location> homes = new HashMap<>();
        if (new LocationsManager().getCfg().contains(player.getName() + ".home")) {
            ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection(player.getName() + ".home");
            if (cs != null) {
                for (String s : cs.getKeys(false)) {
                    if (new LocationsManager().getCfg().get(player.getName() + ".home." + s) != null) {
                        if (!new LocationsManager().getCfg().get(player.getName() + ".home." + s).equals(" ")) {
                            homes.put(s,new LocationsManager().getLocation(player.getName() + ".home." + s));
                        }
                    }
                }
                return homes;
            }
        }
        return null;
    }

    public HashMap<String,Location> getPositions() {
        HashMap<String,Location> positions = new HashMap<>();
        ConfigurationSection cs = new LocationsManager().getCfg().getConfigurationSection("position");
        if (cs != null) {
            for (String s : cs.getKeys(false)) {
                if (new LocationsManager().getCfg().get("position." + s) != null) {
                    if (!new LocationsManager().getCfg().get("position." + s).equals(" ")) {
                        positions.put(s,new LocationsManager().getLocation("position." + s));
                    }
                }
            }
            return positions;
        }
        return positions;
    }

    public SkullBuilder createSkullBuilder(OfflinePlayer player) {
        return new SkullBuilder(player);
    }

    @Deprecated
    public SkullBuilder createSkullBuilder(String playerName) {
        return new SkullBuilder(playerName);
    }

    public BossBarManager createNewBossBarManager(String title, BarColor barColor, BarStyle barStyle) {
        return new BossBarManager(title,barColor,barStyle);
    }

    public BossBarManager createNewBossBarManager(String title) {
        return new BossBarManager(title);
    }

    public PlayerManager getPlayerManagerCfg(OfflinePlayer player) {
        if(!isJsonFormat()) {
            return new PlayerManager(player);
        }
        return null;
    }

    public Location getLocation(String locationName) {
        return new LocationsManager(locationName).getLocation();
    }

    public PlayerManagerCfgLoss getPlayerManagerJson(OfflinePlayer player) {
        if(isJsonFormat()) {
            if (!plugin.getCfgLossHashMap().isEmpty()) {
                if (plugin.getCfgLossHashMap().containsKey(player)) {
                    return plugin.getCfgLossHashMap().get(player);
                } else {
                    try {
                        return PlayerManagerCfgLoss.getPlayerManagerCfgLoss(player);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    return PlayerManagerCfgLoss.getPlayerManagerCfgLoss(player);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public PlayerManagerMongoDB getPlayerManagerMongoDb(OfflinePlayer player) {
        if(plugin.isMongoDb()) {
            return PlayerManagerMongoDB.getPlayerManager(player.getName(),"test");
        }
        return null;
    }

    public MaterialManager getMaterialManager() {
        return new MaterialManager();
    }

    public void printAllHomesFromPlayers() {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            getPlayerHomes(offlinePlayer).forEach((s, location) -> Bukkit.getConsoleSender().sendMessage(plugin.getPrefix() + "§7" + offlinePlayer.getName() + " Homes : " + s + " = " +  new LocationsManager().locationToString(location)));
        }
    }

    @Deprecated
    public void printPlayerDataFromPlayer(ConsoleCommandSender sender, OfflinePlayer player) {
        if(!isJsonFormat()) {
            final PlayerManager playerManager = new PlayerManager(player);
            long login = playerManager.getLastLogin();
            sender.sendMessage("§6Info About §a" + player.getName());
            sender.sendMessage(
                    "§aLast Login : §6" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date(login)));
            long logout = playerManager.getLastLogout();
            sender.sendMessage("§aLast Logout : §6"
                    + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date(logout)));
            sender.sendMessage("§aTime Played : §6" + playerManager.getTimePlayed());
            sender.sendMessage("§aOnline : §6" + player.isOnline());
            sender.sendMessage("§aPlayerKills : §6" + playerManager.getPlayerKills());
            sender.sendMessage("§aEntityKills : §6" + playerManager.getEntityKills());
            sender.sendMessage("§aDamage : §6" + playerManager.getDamage());
            sender.sendMessage("§aDeaths : §6" + playerManager.getDeaths());sender.sendMessage("§aCommandsUsed : §6" + playerManager.getCommandsUsed());
            sender.sendMessage("§aBlocksBroken : §6" + playerManager.getBlockBroken());
            sender.sendMessage("§aBlocksPlaced : §6" + playerManager.getBlockPlace());
        } else {
            PlayerManagerCfgLoss playerManager = null;
            if(player.isOnline()) {
                playerManager = plugin.getCfgLossHashMap().get(player);
            } else {
                try {
                    playerManager = PlayerManagerCfgLoss.getPlayerManagerCfgLoss(player);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            long login = playerManager.getLastlogin();
            sender.sendMessage("§6Info About §a" + player.getName());
            sender.sendMessage(
                    "§aLast Login : §6" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date(login)));
            long logout = playerManager.getLastLogout();
            sender.sendMessage("§aLast Logout : §6"
                    + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date(logout)));
            sender.sendMessage("§aTime Played : §6" + playerManager.getTimePlayed());
            sender.sendMessage("§aOnline : §6" + player.isOnline());
            sender.sendMessage("§aPlayerKills : §6" + playerManager.getPlayerKills());
            sender.sendMessage("§aEntityKills : §6" + playerManager.getEntityKills());
            sender.sendMessage("§aDamage : §6" + playerManager.getDamage());
            sender.sendMessage("§aDeaths : §6" + playerManager.getDeaths());
            sender.sendMessage("§aCommandsUsed : §6" + playerManager.getCommandsUsed());
            sender.sendMessage("§aBlocksBroken : §6" + playerManager.getBlockBroken());
            sender.sendMessage("§aBlocksPlaced : §6" + playerManager.getBlockPlace());

        }
    }

    public void openPlayersBackpack(Player player,OfflinePlayer target) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, target.getName() + "'s Inventory");
        if (BackpackCMD.itemsStringHashMap.containsKey(target.getUniqueId().toString())) {
            try {
                inventory.setContents(InventoryStringDeSerializer.itemStackArrayFromBase64(BackpackCMD.itemsStringHashMap.get(target.getUniqueId().toString())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.openInventory(inventory);
        } else {
            player.sendMessage(plugin.getPrefix() + "§cDieser Spieler hat noch kein BackPack!");
        }
    }

    public Inventory getPlayersBackPack(OfflinePlayer target) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, target.getName() + "'s Inventory");
        if (BackpackCMD.itemsStringHashMap.containsKey(target.getUniqueId().toString())) {
            try {
                inventory.setContents(InventoryStringDeSerializer.itemStackArrayFromBase64(BackpackCMD.itemsStringHashMap.get(target.getUniqueId().toString())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inventory;
    }

    public ArrayList<String> getWarps() {
        if (new LocationsManager().getCfg().contains("warps")) {
            return (ArrayList<String>) new LocationsManager().getCfg().getStringList("warps");
        }
        return new ArrayList<>();
    }

    private int time = 100;
    private double progress = 1.0;

    /**
     * Restarting the Server
     */
    public void restartServer() {
        // Restarting the Server
        BossBarManager.removeAll();
        BossBarManager bossBarManager = new BossBarManager("§4Server Restart", BarColor.RED, BarStyle.SEGMENTED_6);
        bossBarManager.create();
        bossBarManager.setProgress(progress);
        Bukkit.getConsoleSender().sendMessage(Main.getInstance().getPrefix() + "§cServer Restart wird eingeführt!");
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            if (time <= 0) {
                Bukkit.spigot().restart();
            } else {
                switch (time) {
                    case 60:
                        Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§cServer Restartet in §61 Minute§4§l!");
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            bossBarManager.addPlayer(players);
                            bossBarManager.update();
                        });
                        break;
                    case 30:
                    case 3:
                    case 15:
                    case 10:
                    case 5:
                    case 2:
                        Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§cServer Restartet in §6" + time + " Sekunden§4§l!");
                        bossBarManager.setProgress(1.0);
                        break;
                    case 1:
                        Bukkit.broadcastMessage(Main.getInstance().getPrefix() + "§cServer Restartet in §6einer Sekunde§4§l!");
                        bossBarManager.setProgress(1.0);
                        break;
                }
                time--;
                if (progress != 0.00) {
                    progress = progress - 0.01;
                    bossBarManager.setProgress(progress);
                    bossBarManager.update();
                }
            }
        }, 0, 20);
    }

    public boolean isWorldUsingKey(World world) {
        return WorldTPCMD.worldWithKeys.contains(world.getName());
    }

    public InventoryManager createNewInventoryManager() {
        return new InventoryManager();
    }

    public ItemBuilder createNewItemBuilder(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder createNewItemBuilder(Material material) {
        return new ItemBuilder(material);
    }

    public boolean setJsonFormat(boolean jsonFormat) {
        try {
            plugin.getConfig().set("JsonFormat",jsonFormat);
            Bukkit.getPluginManager().disablePlugin(plugin);
            Bukkit.getPluginManager().enablePlugin(plugin);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isPlayerRegistered(OfflinePlayer player) {
        if(!plugin.getVariables().isOnlineMode()) {
            return RegisterCMD.getCfg().contains(Objects.requireNonNull(player.getName()));
        }
        return true;
    }

    public BackupManager getBackupManager() {
        return new BackupManager();
    }

    public GameMode getGameModeById(int id) {
        return GameModeCMD.getGameModeById(id);
    }

    public String toJson(Object obj) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }

    public Villager villagerCreate(Player player) {
        Villager villager = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        ArrayList<MerchantRecipe> recipes = new ArrayList<>();
        MerchantRecipe recipe = new MerchantRecipe(new ItemBuilder(Material.STICK).setAmount(1).setDisplayName("§aParty!").addEnchantment(Enchantment.KNOCKBACK, 10, true).build(), Integer.MAX_VALUE);
        ItemStack stack = new ItemStack(Material.STICK);
        stack.setAmount(2);
        recipe.addIngredient(stack);
        villager.setProfession(Villager.Profession.ARMORER);
        villager.setVillagerExperience(1);
        recipes.add(recipe);
        villager.setRecipes(recipes);
        villager.setVillagerLevel(1);
        villager.setVillagerType(Villager.Type.DESERT);
        return villager;
    }
}
