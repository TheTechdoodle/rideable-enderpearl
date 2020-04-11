package com.darkender.plugins.rideableenderpearl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class REPCommand implements CommandExecutor
{
    private RideableEnderpearl base;
    
    public REPCommand(RideableEnderpearl base)
    {
        this.base = base;
    }
    
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings)
    {
        base.reload();
        commandSender.sendMessage("Reloaded the config");
        return true;
    }
}
