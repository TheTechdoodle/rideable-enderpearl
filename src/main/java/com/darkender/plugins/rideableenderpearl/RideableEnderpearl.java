package com.darkender.plugins.rideableenderpearl;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RideableEnderpearl extends JavaPlugin implements Listener
{
    private ParticleTask particleTask = null;
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        REPCommand repCommand = new REPCommand(this);
        getCommand("rideableenderpearl").setExecutor(repCommand);
        getCommand("rideableenderpearl").setTabCompleter(repCommand);
        reload();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if(!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.ENDER_PEARL)
        {
            return;
        }
        
        Player p = (Player) event.getEntity().getShooter();
        event.getEntity().addPassenger(p);
        if(particleTask != null)
        {
            particleTask.addTrack(event.getEntity().getUniqueId());
        }
    }
    
    void reload()
    {
        saveDefaultConfig();
        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        
        boolean showParticles = getConfig().getBoolean("enderpearl-particles");
        if(showParticles && particleTask == null)
        {
            // If particles have just been enabled, start the task
            particleTask = new ParticleTask();
            particleTask.runTaskTimer(this, 1L, 1L);
        }
        else if(!showParticles && particleTask != null)
        {
            // If particles have just been disabled, stop the task
            particleTask.cancel();
            particleTask = null;
        }
    }
}
