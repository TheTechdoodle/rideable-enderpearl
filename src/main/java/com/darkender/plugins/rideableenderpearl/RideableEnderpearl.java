package com.darkender.plugins.rideableenderpearl;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RideableEnderpearl extends JavaPlugin implements Listener
{
    private ParticleTask particleTask = null;
    private final Set<UUID> allowedDismounts = new HashSet<>();
    private boolean preventSuffocationDamage;
    private boolean preventThrowingWhileRiding;
    private boolean preventDismount;
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        REPCommand repCommand = new REPCommand(this);
        getCommand("rideableenderpearl").setExecutor(repCommand);
        getCommand("rideableenderpearl").setTabCompleter(repCommand);
        reload();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event)
    {
        if(preventSuffocationDamage && event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION &&
                event.getEntity().isInsideVehicle() && event.getEntity().getVehicle().getType() == EntityType.ENDER_PEARL)
        {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        if(!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.ENDER_PEARL)
        {
            return;
        }
        
        Player p = (Player) event.getEntity().getShooter();
        
        // Prevent throwing another enderpearl while currently riding one
        if(preventThrowingWhileRiding && p.isInsideVehicle() &&
                p.getVehicle().getType() == EntityType.ENDER_PEARL && !p.getVehicle().isDead())
        {
            event.setCancelled(true);
            return;
        }
        
        event.getEntity().addPassenger(p);
        if(particleTask != null)
        {
            particleTask.addTrack(event.getEntity().getUniqueId());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if(!preventDismount || event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
        {
            return;
        }
        // The teleport event gets sent before the ProjectileHitEvent does
        // Without allowing players to dismount when the enderpearl ends flight, a client-server desync occurs
        // To fix this, the player is added to a set that allows them to dismount
        allowedDismounts.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerDismountEnderpearl(EntityDismountEvent event)
    {
        if(!preventDismount || event.getEntityType() != EntityType.PLAYER ||
                event.getDismounted().getType() != EntityType.ENDER_PEARL)
        {
            return;
        }

        Player p = (Player) event.getEntity();
        if(allowedDismounts.contains(p.getUniqueId()))
        {
            allowedDismounts.remove(p.getUniqueId());
        }
        else
        {
            event.setCancelled(true);
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
        
        preventSuffocationDamage = getConfig().getBoolean("prevent-suffocation-damage");
        preventThrowingWhileRiding = getConfig().getBoolean("prevent-throwing-while-riding");
        preventDismount = getConfig().getBoolean("prevent-dismount");
    }
}
