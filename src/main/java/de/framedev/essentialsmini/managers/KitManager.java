package de.framedev.essentialsmini.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.framedev.essentialsmini.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class KitManager {

    private static File customConfigFile;
    private static FileConfiguration customConfig;
    public Inventory kitname = Bukkit.createInventory(null, 36);

    public static FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public void createCustomConfig() {
        customConfigFile = new File(Main.getInstance().getDataFolder(), "kits.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            Main.getInstance().saveResource("kits.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean existsKit(String name) {
        return getCustomConfig().contains("Items." + name);
    }

    public void loadKits(String name, Player player) {
        try {
            for (String s : getCustomConfig().getStringList("Items." + name + ".Content")) {
                if (s == null) return;
                if (s.contains(",")) {
                    String[] x = s.split(",");
                    ItemStack item = new ItemStack(Material.getMaterial(x[0].toUpperCase()), Integer.parseInt(x[1]));
                    this.kitname.addItem(item);
                } else {
                    this.kitname.addItem(new ItemStack(Material.getMaterial(s.toUpperCase())));
                }
            }
            for (ItemStack items : this.kitname.getContents()) {
                if (items != null) {
                    if (items.getType() == Material.LEATHER_BOOTS || items.getType() == Material.CHAINMAIL_BOOTS
                            || items.getType() == Material.IRON_BOOTS || items.getType() == Material.GOLDEN_BOOTS || items.getType() == Material.DIAMOND_BOOTS) {
                        player.getInventory().setBoots(items);
                        items = new ItemStack(Material.AIR);
                    }
                    if (items.getType() == Material.LEATHER_HELMET || items.getType() == Material.CHAINMAIL_HELMET
                            || items.getType() == Material.IRON_HELMET || items.getType() == Material.GOLDEN_HELMET || items.getType() == Material.DIAMOND_HELMET) {
                        player.getInventory().setHelmet(items);
                        items = new ItemStack(Material.AIR);
                    }
                    if (items.getType() == Material.LEATHER_LEGGINGS || items.getType() == Material.CHAINMAIL_LEGGINGS
                            || items.getType() == Material.IRON_LEGGINGS || items.getType() == Material.GOLDEN_LEGGINGS || items.getType() == Material.DIAMOND_LEGGINGS) {
                        player.getInventory().setLeggings(items);
                        items = new ItemStack(Material.AIR);
                    }
                    if (items.getType() == Material.LEATHER_CHESTPLATE || items.getType() == Material.CHAINMAIL_CHESTPLATE
                            || items.getType() == Material.IRON_CHESTPLATE || items.getType() == Material.GOLDEN_CHESTPLATE || items.getType() == Material.DIAMOND_CHESTPLATE) {
                        player.getInventory().setChestplate(items);
                        items = new ItemStack(Material.AIR);
                    }
                    player.getInventory().addItem(items);
                    clearKitInventory();
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§cError while Creating Kit §f" + ex.getMessage());
        }
    }

    public void createKit(String kitName, ItemStack[] items) {
        ArrayList<String> kit = new ArrayList<>();
        for (ItemStack itemStack : items) {
            if (itemStack == null) continue;
            kit.add(itemStack.getType() + "," + itemStack.getAmount());
        }
        customConfig.set("Items." + kitName + ".Content", kit);
        customConfig.set("Items." + kitName + ".Cost", 0);
        customConfig.set("Items." + kitName + ".Cooldown", 0);
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createKit(String kitName, ItemStack[] items, int cooldown) {
        ArrayList<String> kit = new ArrayList<>();
        for (ItemStack itemStack : items) {
            if (itemStack == null) continue;
            kit.add(itemStack.getType() + "," + itemStack.getAmount());
        }
        customConfig.set("Items." + kitName + ".Content", kit);
        customConfig.set("Items." + kitName + ".Cooldown", cooldown);
        customConfig.set("Items." + kitName + ".Cost", 0);
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createKit(String kitName, ItemStack[] items, int cooldown, int cost) {
        ArrayList<String> kit = new ArrayList<>();
        for (ItemStack itemStack : items) {
            if (itemStack == null) continue;
            kit.add(itemStack.getType() + "," + itemStack.getAmount());
        }
        customConfig.set("Items." + kitName + ".Content", kit);
        customConfig.set("Items." + kitName + ".Cooldown", cooldown);
        customConfig.set("Items." + kitName + ".Cost", cost);
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCooldown(String name) {
        return getCustomConfig().getInt("Items." + name + ".Cooldown");
    }

    public int getCost(String name) {
        return getCustomConfig().getInt("Items." + name + ".Cost");
    }

    public boolean hasCost(String name) {
        return getCustomConfig().getInt("Items." + name + ".Cost") != 0;
    }

    public boolean hasCooldown(String name) {
        return getCustomConfig().getInt("Items." + name + ".Cooldown") != 0;
    }

    public Inventory getKit(String name) {
        try {
            for (String s : getCustomConfig().getStringList("Items." + name + ".Content")) {
                if (s.contains(",")) {
                    String[] x = s.split(",");
                    ItemStack item = new ItemStack(Material.getMaterial(x[0].toUpperCase()), Integer.parseInt(x[1]));
                    this.kitname.addItem(item);
                } else {
                    this.kitname.addItem(new ItemStack(Material.getMaterial(s.toUpperCase())));
                }
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§cError while Creating Kit §f" + ex.getMessage());
        }
        return this.kitname;
    }


    private void clearKitInventory() {
        this.kitname.clear();
    }

    public List<ItemStack> loadKit(String name) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (String s : getCustomConfig().getStringList("Items." + name + ".Content")) {
            if (s != null) {
                if (s.contains(",")) {
                    String[] x = s.split(",");
                    ItemStack item = new ItemStack(Material.getMaterial(x[0].toUpperCase()));
                    item.setAmount(Integer.parseInt(x[1]));
                    items.add(item);
                } else {
                    items.add(new ItemStack(Material.getMaterial(s.toUpperCase())));
                }
            }
        }
        return items;
    }

    @SuppressWarnings("unused")
    private String toPrettyJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(kitname, Inventory.class);
    }

    public void saveKit(String name) {
        try {
            List<ItemStack> items = loadKit(name);
            FileWriter fileWriter = new FileWriter(new File(Main.getInstance().getDataFolder(), "kit.json"));
            fileWriter.write(new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(items));
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Inventory getKit() {
        List<ItemStack> stack = null;
        Inventory inventory = Bukkit.createInventory(null, 5 * 9);
        try {
            FileReader fileReader = new FileReader(new File(Main.getInstance().getDataFolder(), "kit.json"));
            Type type = new TypeToken<ArrayList<ItemStack>>() {
            }.getType();
            stack = new Gson().fromJson(fileReader, type);
            fileReader.close();
        } catch (Exception ignored) {

        }
        if (stack != null) {
            for (ItemStack stacks : stack) {
                inventory.addItem(stacks);
            }
        }
        return inventory;
    }

    @Override
    public String toString() {
        return "KitManager{" +
                "kitname=" + kitname +
                '}';
    }
}


