package com.github.yungfasty.chunkcleaner.commands;

import com.github.yungfasty.chunkcleaner.ChunkCleanerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class GiveChunkCleanerCommand extends Command {

    private final ChunkCleanerPlugin plugin;

    public GiveChunkCleanerCommand(ChunkCleanerPlugin plugin) {
        super("gcc");

        this.plugin = plugin;

        setAliases(Collections.singletonList("givechunk"));
    }

    public boolean execute(CommandSender sender, String label, String[] arguments) {

        if (!sender.hasPermission("chunkcleaner.give")) {
            sender.sendMessage("§cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (arguments.length != 2) {
            sender.sendMessage(String.format("§cUse \"/%s <jogador> <quantidade>\".", label));
            return true;
        }

        Player player = Bukkit.getPlayer(arguments[0]);

        if (player == null) {
            sender.sendMessage(String.format("§cJogador %s não encontrado.", arguments[0]));
            return true;
        }

        Integer integer = parseInteger(arguments[1]);

        if (integer == null || integer <= 0) {
            sender.sendMessage(String.format("§c%s não é um valor válido.", arguments[1]));
            return true;
        }

        ItemStack clone = plugin.getCleaningManager().getItemStack().clone();

        clone.setAmount(integer);

        if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItem(player.getLocation(), clone);
        else player.getInventory().addItem(clone);

        sender.sendMessage(String.format("§aVocê enviou §f%sx §apara §f%s§a.", integer, player.getName()));
        return false;
    }

    private Integer parseInteger(String s) {

        try { return Integer.parseInt(s); }
        catch (Exception e) { return null; }

    }
}