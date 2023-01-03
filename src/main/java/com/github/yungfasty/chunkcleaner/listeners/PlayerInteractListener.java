package com.github.yungfasty.chunkcleaner.listeners;

import com.github.yungfasty.chunkcleaner.CCleanerPlugin;
import com.github.yungfasty.chunkcleaner.managers.CleaningManager;
import com.github.yungfasty.chunkcleaner.models.CleaningModel;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final CleaningManager cleaningManager = CCleanerPlugin.getInstance().getCleaningManager();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void on(PlayerInteractEvent event) {

        ItemStack itemStack = event.getPlayer().getItemInHand();

        if (itemStack == null ||
                !event.getAction().name().toLowerCase().contains("right") ||
                !cleaningManager.getItem().isSimilar(itemStack)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayer.get(player);
        Location location = event.getClickedBlock().getLocation();

        if (!mPlayer.hasFaction() ||
                BoardColl.get().getFactionAt(PS.valueOf(location)) != mPlayer.getFaction() ||
                mPlayer.getRole().isLessThan(Rel.OFFICER)) {

            player.sendMessage("§cVocê não pode usar isto aqui.");

            return;

        }

        if (itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount() - 1);
        else player.setItemInHand(null);

        cleaningManager.getCleaningModelSet().add(
                CleaningModel.builder()
                        .chunk(location.getChunk())
                        .y((int) location.getY())
                        .nextMillis(System.currentTimeMillis())
                        .size(2)
                        .build());

    }

}
