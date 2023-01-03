package com.github.yungfasty.chunkcleaner.updaters;

import com.github.yungfasty.chunkcleaner.CCleanerPlugin;
import com.github.yungfasty.chunkcleaner.models.CleaningModel;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class CleaningUpdater {

    public static void init() {

        Set<CleaningModel> cleaningModelSet = CCleanerPlugin.getInstance().getCleaningManager().getCleaningModelSet();
        long currentTimeMillis = System.currentTimeMillis();
        Iterator<CleaningModel> iterator = cleaningModelSet.iterator();

        while (iterator.hasNext()) {

            CleaningModel cleaningModel = iterator.next();

            cleaningModel.tick(currentTimeMillis);

            if (cleaningModel.getY() == 1) iterator.remove();

        }

        /*
        for (CleaningModel cleaningModel : cleaningModelSet)
            cleaningModel.tick(currentTimeMillis);

        cleaningModelSet.removeIf(cleaningModel -> cleaningModel.getY() == 1);

         */

    }

}