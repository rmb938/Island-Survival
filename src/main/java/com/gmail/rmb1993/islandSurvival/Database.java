package com.gmail.rmb1993.islandSurvival;

import com.gmail.rmb1993.islandSurvival.arena.Island;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Database {

    private final IslandSurvival plugin;

    public Database(IslandSurvival plugin) {
        this.plugin = plugin;
    }

    public Island loadArena() {
        //load arena from database
        Island arena = new Island(plugin);
        arena.startArena();
        return arena;
    }

    public void saveArena() {
        //save arena to database
    }

}
