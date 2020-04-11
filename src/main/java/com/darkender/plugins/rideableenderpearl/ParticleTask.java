package com.darkender.plugins.rideableenderpearl;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ParticleTask extends BukkitRunnable
{
    private List<UUID> track = new ArrayList<UUID>();
    
    public void run()
    {
        // Display particles on all tracked enderpearls
        // If the enderpearl doesn't exist, remove it
        Iterator<UUID> iterator = track.iterator();
        while(iterator.hasNext())
        {
            Entity e = Bukkit.getEntity(iterator.next());
            if(e == null)
            {
                iterator.remove();
                continue;
            }
            e.getWorld().spawnParticle(Particle.SPELL_INSTANT, e.getLocation(), 3);
        }
    }
    
    public void addTrack(UUID id)
    {
        track.add(id);
    }
}
