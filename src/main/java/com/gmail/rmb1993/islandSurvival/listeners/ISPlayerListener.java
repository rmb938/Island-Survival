package com.gmail.rmb1993.islandSurvival.listeners;

import com.gmail.rmb1993.islandSurvival.IslandSurvival;
import com.gmail.rmb1993.islandSurvival.arena.Island;
import com.gmail.rmb1993.islandSurvival.arena.team.ArenaTeam;
import com.gmail.rmb1993.islandSurvival.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISPlayerListener implements Listener {

    private final IslandSurvival plugin;

    public ISPlayerListener(IslandSurvival plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);//register listener
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (plugin.getArena().getArenaStatus() != Island.ArenaStatus.WAITING) {

            //if player was on a team when they quit allow them to join
            if (plugin.getArena().getArenaStatus() == Island.ArenaStatus.RUNNING) {
                for (ArenaTeam team : plugin.getArena().getTeams()) {
                    for (User u1 : team.getMembers()) {
                        if (u1.getPlayerName().equalsIgnoreCase(event.getPlayer().getName())) {
                            event.allow();
                            return;
                        }
                    }
                }
            }

            //deny any players from joining while arena is running
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "You can not join the arena while it is running.");
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {//remove a life from a player and check if a team has won
        User user = plugin.getUsers().get(event.getEntity().getName());
        user.setCurrentLives(user.getCurrentLives()-1);
        plugin.getArena().checkRemainingTeams();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final User user = new User(player.getName());//usually load user info possibly passed from bungee
        plugin.getUsers().put(user.getPlayerName(), user);//create user link

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {//need to wait a few ticks for player entity to get created
            @Override
            public void run() {
                for (ArenaTeam team : plugin.getArena().getTeams()) {//check if the user was in a team when they left the server
                    for (User u1 : team.getMembers()) {
                        if (u1.getPlayerName().equalsIgnoreCase(user.getPlayerName())) {//they were in a team to set them back up
                            if (u1.getCurrentLives() == 0) {//no lives so teleport them back to the waiting area
                                player.teleport(plugin.getArena().getWaitingArea());
                            }
                            plugin.getArena().setupScoreboard(u1);
                            plugin.getUsers().put(u1.getPlayerName(), u1);//fix their user link
                            return;
                        }
                    }
                }
                //wasn't in a team so add them to a team to join the arena queue and teleport them to waiting
                plugin.getArena().joinLeastTeam(user);
                player.teleport(plugin.getArena().getWaitingArea());
            }
        }, 5L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        User user = plugin.getUsers().get(event.getPlayer().getName());

        //remove user from a team if the arena is still waiting or if it is prerunning(they may not get teleported so can't keep them in)
        if (user.getTeam() != null) {
            if (plugin.getArena().getArenaStatus() == Island.ArenaStatus.WAITING || plugin.getArena().getArenaStatus() == Island.ArenaStatus.PRERUNNING) {
                user.getTeam().getMembers().remove(user);
            }
        }

        //remove user link
        plugin.getUsers().remove(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        User user = plugin.getUsers().get(event.getPlayer().getName());
        if (user.getTeam() == null) {//respawn to waiting if not in a team
            event.setRespawnLocation(plugin.getArena().getWaitingArea());
        } else if (user.getTeam() != null) {
            if (user.getCurrentLives() > 0) {
                event.setRespawnLocation(user.getTeam().getSpawnLocation());//respawn to team spawn if lives left
            } else {
                event.setRespawnLocation(plugin.getArena().getWaitingArea());//respawn to waiting if no more lives
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNameTag(PlayerReceiveNameTagEvent event) {//set name tag as team colors
        User shownUser = plugin.getUsers().get(event.getNamedPlayer().getName());
        if (shownUser.getTeam() != null) {
            event.setTag(shownUser.getTeam().getColor()+shownUser.getPlayerName());
        }
    }

}
