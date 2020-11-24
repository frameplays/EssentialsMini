package de.framedev.essentialsmin.api;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.utils.CustomJson;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.api
 * Date: 24.10.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class TestAPI {

    private static TestAPI instance;
    private final Main plugin;

    public TestAPI(Main plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public static TestAPI getInstance() {
        return instance;
    }

    public void runTest(String path, Object object) {
        CustomJson json = new CustomJson("TestAPI");
        json.set(path, object);
        json.saveConfig();
    }

    public Object getTestResult(String path) {
        CustomJson json = new CustomJson("TestAPI");
        return json.get(path);
    }

    public void update(String path, Object value) {
        CustomJson json = new CustomJson("TestAPI");
        json.update(path, value);
        json.saveConfig();
    }

    public void printTestResult(String path) {
        System.out.println(getTestResult(path));
    }
}
