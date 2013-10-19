package com.gmail.rmb1993.islandSurvival.user;

import com.gmail.rmb1993.islandSurvival.arena.team.ArenaTeam;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private final String playerName;

    public User(String playerName) {
        this.playerName = playerName;
    }

    private ArenaTeam team;
    private int currentLives = 3;

    /**
     * Get the current lives of the user
     * @return currentLives
     */
    public int getCurrentLives() {
        return currentLives;
    }

    /**
     * Set the current lives of the user
     * @param currentLives
     */
    public void setCurrentLives(int currentLives) {
        this.currentLives = currentLives;
    }

    /**
     * Get the users name
     * @return playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Get the users team
     * @return team
     */
    public ArenaTeam getTeam() {
        return team;
    }

    /**
     * Set the users team
     * @param team
     */
    public void setTeam(ArenaTeam team) {
        this.team = team;
    }
}
