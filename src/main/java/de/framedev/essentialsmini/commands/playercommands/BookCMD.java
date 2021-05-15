package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmini.commands.playercommands
 * ClassName BookCMD
 * Date: 15.05.21
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */

public class BookCMD extends CommandBase {

    public BookCMD(Main plugin) {
        super(plugin, "book");
        setup(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(getPlugin().getPrefix() + getPlugin().getOnlyPlayer());
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("essentialsmini.book")) {
            player.sendMessage(getPlugin().getPrefix() + getPlugin().getNOPERMS());
            return true;
        }

        if(player.getInventory().getItemInMainHand().getType() == Material.WRITTEN_BOOK) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
            List<String> pages = bookMeta.getPages();
            ItemStack item = new ItemStack(Material.WRITABLE_BOOK);
            BookMeta meta = (BookMeta) item.getItemMeta();
            if(meta != null) {
                meta.setPages(pages);
                item.setItemMeta(meta);
            }
            player.getInventory().remove(itemStack);
            player.getInventory().setItemInMainHand(item);
            player.openBook(item);
        } else {
            player.sendMessage(getPlugin().getPrefix() + "§aYou don't have an Written Book in your Hand!");
        }
        return super.onCommand(sender, command, label, args);
    }
}
