package com.github.yungfasty.chunkcleaner.models;

import com.github.yungfasty.chunkcleaner.CCleanerPlugin;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Getter @Builder
public class CleaningModel {

    private final Chunk chunk;
    private int y, size;
    private long nextMillis;

    public void tick(long currentTimeMillis) {

        if (nextMillis > currentTimeMillis || y == 1)
            return;
		
		Block block;

        for (int indexX = 0; indexX < 16; indexX++) {

            for (int indexZ = 0; indexZ < 16; indexZ++) {

                block = chunk.getBlock(indexX, y, indexZ);

                if (block.getType() != Material.CHEST &&
                        block.getType() != Material.TRAPPED_CHEST &&
                        block.getType() != Material.MOB_SPAWNER &&
                        block.getType() != Material.BEDROCK)
                    block.setType(Material.AIR);

            }
        }

        --y;

        for (int indexX = 0; indexX < size; indexX++) {

            for (int indexZ = 0; indexZ < (size / 2); indexZ++) {

                block = chunk.getBlock(indexX, y, indexZ);

                if (block.getType() != Material.CHEST &&
                        block.getType() != Material.TRAPPED_CHEST &&
                        block.getType() != Material.MOB_SPAWNER &&
                        block.getType() != Material.BEDROCK)
                    block.setType(Material.AIR);

            }
        }

        if (size >= 16) size = 1;
        else size += 1;

        nextMillis = currentTimeMillis + CCleanerPlugin.getInstance().getCleaningManager().getMillisBetweenBreaks();

    }

}
