package com.github.yungfasty.chunkcleaner.managers;

import com.github.yungfasty.chunkcleaner.models.CleaningModel;
import com.github.yungfasty.chunkcleaner.utils.ItemBuilder;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class CleaningManager {

    @Getter private final Set<CleaningModel> cleaningModelSet;
    @Getter private final ItemStack item;
    @Getter private final long millisBetweenBreaks;

    public CleaningManager(FileConfiguration configuration) {

        cleaningModelSet = Sets.newHashSet();

        item = ItemBuilder.of(configuration.getConfigurationSection("item")).wrap();
        millisBetweenBreaks = configuration.getLong("settings.millis-between-breaks");

    }

}
