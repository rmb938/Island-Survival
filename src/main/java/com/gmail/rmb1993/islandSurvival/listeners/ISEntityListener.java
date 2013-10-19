package com.gmail.rmb1993.islandSurvival.listeners;

import com.gmail.rmb1993.islandSurvival.IslandSurvival;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 10/19/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ISEntityListener implements Listener {

    private final IslandSurvival plugin;

    public ISEntityListener(IslandSurvival plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);//register listener
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            event.setDamage(20.0);//arrows and bows are rare so instant kill.
        }
    }
}
