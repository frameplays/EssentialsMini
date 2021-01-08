package de.framedev.essentialsmin.managers;


/*
 * de.framedev.essentialsmin.managers
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 28.09.2020 17:11
 */

@SuppressWarnings("Unused")
public class Home {

    private String name;
    private int id;
    private String location;

    public Home(String name, int id, String location) {
        this.name = name;
        this.id = id;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Home{" +
                "name=" + name +
                ",id=" + id +
                ",location=" + location +
                '}';
    }

    public static Home fromString(String str) {
        str = str.replace("Home{","");
        str = str.replace("}","");
        String[] s = str.split(",");
        return new Home(s[0].replace("name=",""),Integer.parseInt(s[1].replace("id=","")),s[2].replace("location=",""));
    }
}
