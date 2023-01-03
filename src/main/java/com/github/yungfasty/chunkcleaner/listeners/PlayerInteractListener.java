package com.github.yungfasty.chunkcleaner.listeners;

import com.github.yungfasty.chunkcleaner.manager.CleaningManager;
import com.github.yungfasty.chunkcleaner.models.CleaningModel;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.EngineCustomClaim;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import lombok.RequiredArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

    private final CleaningManager cleaningManager;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void onPlayerInteract(PlayerInteractEvent event) {

        ItemStack itemStack = event.getPlayer().getItemInHand();

        if (itemStack == null
                || !event.getAction().name().toLowerCase().contains("right")
                || !cleaningManager.getItemStack().isSimilar(itemStack)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Location location = event.getClickedBlock().getLocation();
        Chunk chunk = location.getChunk();

        MPlayer mPlayer = MPlayer.get(player);
        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(chunk));

        if (factionAt.isInAttack()) {
            player.sendMessage("§cEste item não pode ser usado em terras sob ataque.");
            return;
        }

        if (EngineCustomClaim.get().isChunkTemporary(chunk.getX(), chunk.getZ())) {
            player.sendMessage("§cEste item não pode usado em terras temporárias.");
            return;
        }

        if (!mPlayer.hasFaction()
                || BoardColl.get().getFactionAt(PS.valueOf(location)) != mPlayer.getFaction()
                || mPlayer.getRole().isLessThan(Rel.MEMBER)) {

            player.sendMessage("§cVocê precisa ser membro da facção para usar este item.");
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