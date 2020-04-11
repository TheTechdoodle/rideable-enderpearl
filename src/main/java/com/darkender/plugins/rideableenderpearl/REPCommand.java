package com.darkender.plugins.rideableenderpearl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class REPCommand implements CommandExecutor, TabCompleter
{
    private final RideableEnderpearl base;
    private final List<String> tabCompletions = new ArrayList<>();
    
    public REPCommand(RideableEnderpearl base)
    {
        this.base = base;
    }
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        base.reload();
        commandSender.sendMessage("Reloaded the config");
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args)
    {
        return tabCompletions;
    }
}
