package com.github.yungfasty.chunkcleaner.managers;

import com.github.yungfasty.chunkcleaner.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ListenerManager {

    public ListenerManager(Plugin plugin) {

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), plugin);

    }

}
