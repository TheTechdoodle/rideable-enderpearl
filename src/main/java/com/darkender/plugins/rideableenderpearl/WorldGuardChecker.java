package com.darkender.plugins.rideableenderpearl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WorldGuardChecker
{
    private StateFlag canRideEnderpearlFlag = null;
    
    public WorldGuardChecker()
    {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try
        {
            StateFlag flag = new StateFlag("ride-enderpearl", true);
            registry.register(flag);
            canRideEnderpearlFlag = flag;
        }
        catch(FlagConflictException e)
        {
            Bukkit.getLogger().warning("WorldGuard flag already exists!");
        }
    }
    
    public boolean canRide(Player player)
    {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return set.testState(localPlayer, canRideEnderpearlFlag);
    }
}
