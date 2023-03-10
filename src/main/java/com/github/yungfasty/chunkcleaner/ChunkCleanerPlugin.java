package com.github.yungfasty.chunkcleaner;

import com.github.yungfasty.chunkcleaner.commands.GiveChunkCleanerCommand;
import com.github.yungfasty.chunkcleaner.listeners.PlayerInteractListener;
import com.github.yungfasty.chunkcleaner.manager.CleaningManager;
import com.github.yungfasty.chunkcleaner.updaters.CleaningUpdater;
import com.github.yungfasty.chunkcleaner.utils.CommandRegistry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkCleanerPlugin extends JavaPlugin {

    @Getter private CleaningManager cleaningManager;

    public void onEnable() {

        saveDefaultConfig();

        cleaningManager = new CleaningManager(getConfig());

        CommandRegistry.registerCommand(new GiveChunkCleanerCommand(this));

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(cleaningManager), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, CleaningUpdater::init, 0L, getConfig().getLong("settings.ticks-between-loops"));
    }

    public static ChunkCleanerPlugin getInstance() {
        return getPlugin(ChunkCleanerPlugin.class);
    }
}