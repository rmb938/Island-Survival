package com.gmail.rmb1993.islandSurvival.arena.team;

import com.gmail.rmb1993.islandSurvival.user.User;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArenaTeam {

    private final Color color;
    private final Location spawnLocation;
    private final ArrayList<User> members;

    public ArenaTeam(Color color, Location spawnLocation) {
        this.color = color;
        this.spawnLocation = spawnLocation;
        this.members = new ArrayList<>();
    }

    /**
     * Get the current team members
     * @return
     */
    public ArrayList<User> getMembers() {
        return members;
    }

    /**
     * Get the Team Color/Name
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get the team spawn location
     * @return
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
