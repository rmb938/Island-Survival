package com.gmail.rmb1993.islandSurvival.arena;

import com.gmail.rmb1993.islandSurvival.IslandSurvival;
import com.gmail.rmb1993.islandSurvival.arena.team.ArenaTeam;
import com.gmail.rmb1993.islandSurvival.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Island extends BukkitRunnable {

    private ArrayList<ArenaTeam> teams = new ArrayList<>();
    private int perTeam = 3;
    private Location waitingArea;
    private long runningTime = 900000;//15 minutes
    private long runTime = -1;
    private IslandSurvival plugin;
    private ArenaStatus arenaStatus = ArenaStatus.WAITING;

    public Island(IslandSurvival plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the Current Arena Status
     * @return arenaStatus
     */
    public ArenaStatus getArenaStatus() {
        return arenaStatus;
    }

    /**
     * Get the arena Teams
     * @return teams
     */
    public ArrayList<ArenaTeam> getTeams() {
        return teams;
    }

    /**
     * Get the waiting area location
     * @return waitingArea
     */
    public Location getWaitingArea() {
        return waitingArea;
    }

    /**
     * Set the waiting area location
     * @param waitingArea
     */
    public void setWaitingArea(Location waitingArea) {
        this.waitingArea = waitingArea;
    }

    /**
     * Start the arena tick. Once a second
     */
    public void startArena() {
        this.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * Check if the teams are full
     * @return boolean
     */
    public boolean teamsFull() {
        for (ArenaTeam team : teams) {
            if (team.getMembers().size() < perTeam) {
                return false;
            }
        }
        return true;
    }

    /**
     * Join a the team with the least number of players
     * @param user
     */
    public void joinLeastTeam(User user) {
        ArenaTeam leastTeam = teams.get(0);
        for (ArenaTeam team : teams) {
            if (leastTeam.getMembers().size() > team.getMembers().size()) {
                leastTeam = team;
            }
        }
        leastTeam.getMembers().add(user);
    }

    /**
     * Randomize the teams
     */
    private void randomTeams() {
        ArrayList<User> currentPlayers = new ArrayList<>();
        for (ArenaTeam team : teams) {
            currentPlayers.addAll(team.getMembers());
        }
        Collections.shuffle(currentPlayers, new Random(System.nanoTime()));
        for (User user : currentPlayers) {
            joinLeastTeam(user);
            setupScoreboard(user);
        }
    }

    /**
     * Announce the winning teams and give users rewards
     * @param team
     */
    public void runWinner(ArenaTeam team) {
        if (team == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "Time has run out! No team has won.");
            return;
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "The team " + team.getColor() + team.getColor().toString() + " has won!");
        //give users reward for winning probably send it through bungee to update if user left this server
    }

    /**
     * Check if there are users left in more then one team if not run winning
     */
    public void checkRemainingTeams() {
        ArrayList<ArenaTeam> remainingTeams = new ArrayList<>();
        for (ArenaTeam team : teams) {
            for (User user : team.getMembers()) {//check if team has at least one user with lives
                if (user.getCurrentLives() > 0) {
                    remainingTeams.add(team);
                    break;
                }
            }
        }
        if (remainingTeams.size() == 1) {
            runWinner(remainingTeams.get(0));//winning team!
            runTime = System.currentTimeMillis() + 10000;
            arenaStatus = ArenaStatus.WINNING;
        }
    }

    /**
     * kick all players to the bungee lobby
     */
    public void kickAllPlayers() {
        //kick all players to bungee lobby
    }

    /**
     * Setup the scoreboard for the first time for a user
     * @param user
     */
    public void setupScoreboard(User user) {
        //set up score board to show current lives of user and possibly other stats?
    }

    /**
     * Update the scoreboard for everyone in the arena
     */
    public void updateScoreboard() {
        //update the scoreboard for everyone in the arena
    }

    /**
     * Arena Tick
     */
    public void run() {
        switch (arenaStatus) {
            case WAITING:
                if (teamsFull() == true) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The arena will start in 10 seconds!");
                    arenaStatus = ArenaStatus.PRERUNNING;
                }
                break;
            case PRERUNNING:
                randomTeams();
                for (ArenaTeam team : teams) {
                    for (User user : team.getMembers()) {
                        Player player = Bukkit.getPlayer(user.getPlayerName());
                        if (player != null) {
                            player.sendMessage(ChatColor.GREEN + "You were placed on the " + user.getTeam().getColor() + user.getTeam().getColor().toString() + ChatColor.GREEN + " team.");
                            player.teleport(team.getSpawnLocation());
                        }
                    }
                }
                runTime = System.currentTimeMillis() + runningTime;
                arenaStatus = ArenaStatus.RUNNING;
                break;
            case RUNNING:
                if (System.currentTimeMillis() < runTime) {
                    runWinner(null);//time ran out. no team won
                    runTime = System.currentTimeMillis() + 10000;
                    arenaStatus = ArenaStatus.WINNING;
                }
                break;
            case WINNING:
                //reset arena in 10 seconds
                if (System.currentTimeMillis() < runTime) {

                    arenaStatus = ArenaStatus.RESETING;
                }
                break;
            case RESETING:
                //kick all players to the lobby then wait more 10 seconds to shutdown
                kickAllPlayers();
                runTime = System.currentTimeMillis() + 10000;
                if (System.currentTimeMillis() < runTime) {
                    Bukkit.shutdown();
                }
                break;
        }
    }

    public enum ArenaStatus {
        WAITING,
        PRERUNNING,
        RUNNING,
        WINNING,
        RESETING
    }

}
