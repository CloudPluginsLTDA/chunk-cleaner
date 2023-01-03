package com.github.yungfasty.chunkcleaner;

import com.github.yungfasty.chunkcleaner.managers.CleaningManager;
import com.github.yungfasty.chunkcleaner.managers.CommandManager;
import com.github.yungfasty.chunkcleaner.managers.ListenerManager;
import com.github.yungfasty.chunkcleaner.updaters.CleaningUpdater;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CCleanerPlugin extends JavaPlugin {

    public static CCleanerPlugin getInstance() {

        return JavaPlugin.getPlugin(CCleanerPlugin.class);

    }

    @Getter private CleaningManager cleaningManager;

    public void onEnable() {

        saveDefaultConfig();

        cleaningManager = new CleaningManager(getConfig());

        new CommandManager();
        new ListenerManager(this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, CleaningUpdater::init, 0L, getConfig().getLong("settings.ticks-between-loops"));

    }
}
