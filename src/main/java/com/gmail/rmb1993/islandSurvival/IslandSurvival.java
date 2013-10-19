package com.gmail.rmb1993.islandSurvival;

import com.gmail.rmb1993.islandSurvival.arena.Island;
import com.gmail.rmb1993.islandSurvival.listeners.ISEntityListener;
import com.gmail.rmb1993.islandSurvival.listeners.ISPlayerListener;
import com.gmail.rmb1993.islandSurvival.user.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class IslandSurvival extends JavaPlugin {

    private Database db;
    private Island arena;
    private HashMap<String, User> users;
    private ISPlayerListener playerListener;
    private ISEntityListener entityListener;

    public void onEnable() {
        users = new HashMap<>();
        db = new Database(this);
        arena = db.loadArena();
        Bukkit.getWorld("").setAutoSave(false);//don't auto save the world so restarting the server will reset the map
        playerListener = new ISPlayerListener(this);
        entityListener = new ISEntityListener(this);
    }

    public void onDisable() {
        db.saveArena();
    }

    public Island getArena() {
        return arena;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
}
