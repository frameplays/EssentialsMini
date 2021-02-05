package de.framedev.essentialsmin.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.framedev.essentialsmin.main.Main;

import java.io.*;
import java.util.HashMap;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.managers
 * Date: 04.02.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class YAMLConfigurator {

    private HashMap<String, Object> data = getConfig();

    String fileName;

    public YAMLConfigurator(String fileName) {
        this.fileName = fileName;
    }

    protected HashMap<String, Object> getData() {
        return data;
    }

    private HashMap<String, Object> getConfig() {
        File file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");
        ObjectMapper mapper = new YAMLMapper();
        HashMap<String, Object> hash = new HashMap<>();
        try {
            MapType type = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class);
            hash = mapper.readValue(new FileReader(file), type);
        } catch (Exception ignored) {
        }
        return hash;
    }

    public void addDefault(String path, Object value) {
        data.put(path, value);
    }

    public void set(String path, Object value) {
        data.put(path, value);
    }

    public int getInt(String path) {
        if (data.containsKey(path))
            return Integer.parseInt(String.valueOf(data.get(path)));
        return 0;
    }

    public double getDouble(String path) {
        if (data.containsKey(path))
            return Double.parseDouble(String.valueOf(data.get(path)));
        return 0d;
    }

    public Object get(String path) {
        if (data.containsKey(path))
            return data.get(path);
        return null;
    }

    public String getString(String path) {
        if (data.containsKey(path))
            return (String) data.get(path);
        return null;
    }

    public boolean getBoolean(String path) {
        if (data.containsKey(path))
            return Boolean.parseBoolean(String.valueOf(data.get(path)));
        return false;
    }

    public boolean isInteger(String path) {
        if (data.containsKey(path)) {
            try {
                Integer.parseInt(String.valueOf(data.get(path)));
                return true;
            } catch (Exception ignored) {

            }
        }
        return false;
    }

    public boolean isDouble(String path) {
        if (data.containsKey(path)) {
            try {
                Double.parseDouble(String.valueOf(data.get(path)));
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public boolean contains(String path) {
        return data.containsKey(path);
    }

    public void saveDefaultConfig() {
        InputStream is = Main.getInstance().getResource(fileName + ".yml");
        ObjectMapper mapper = new YAMLMapper();
        HashMap<String, Object> hash = new HashMap<>();
        try {
            MapType type = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class);
            hash = mapper.readValue(is, type);
        } catch (Exception ignored) {
        }
        if (is != null) {
            try {
                String yaml = mapper.writeValueAsString(hash);
                FileWriter writer = new FileWriter(new File(Main.getInstance().getDataFolder(), fileName + ".yml"));
                writer.write(yaml);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!data.isEmpty())
            hash.putAll(data);
        data = hash;
    }

    public void saveConfig() {
        ObjectMapper mapper = new YAMLMapper();
        File file = new File(Main.getInstance().getDataFolder(), fileName + ".yml");
        try {
            mapper.writeValue(new FileWriter(file), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "YAMLConfigurator{" +
                "data=" + data +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
