package de.framedev.essentialsmini.commands.playercommands;


/*
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 12.08.2020 23:35
 */

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.managers.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
@SuppressWarnings("deprecation")
public class PlayerHeadsCMD extends CommandBase {

    private final Main plugin;

    public PlayerHeadsCMD(Main plugin) {
        super(plugin, "playerheads");
        this.plugin = plugin;
    }

    @Deprecated
    public ItemStack ItemStackSkull(String name) {
        ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
            meta.setDisplayName("§a" + name);
        }
        skull.setItemMeta(meta);
        return skull;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(plugin.getPermissionName() + "playerhead")) {
                if (args.length == 1) {
                    if (((Player) sender).getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD) {
                        ItemStack skull = ((Player) sender).getInventory().getItemInMainHand();
                        SkullMeta meta = (SkullMeta) skull.getItemMeta();
                        meta.setOwningPlayer(Bukkit.getOfflinePlayer(args[0]));
                        meta.setDisplayName("§a" + args[0]);
                        skull.setItemMeta(meta);
                        sender.sendMessage(plugin.getPrefix() + "§aDu hast den Player Head von §6" + args[0] + " §abekommen!");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cKein Player Head in der Hand gefunden!");
                    }
                } else if (args.length == 2) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Bukkit.getPlayer(args[1]).getInventory().addItem(new SkullBuilder(args[0]).setDisplayName(args[0]).create());
                        sender.sendMessage(plugin.getPrefix() + "§6" + Bukkit.getPlayer(args[1]).getName() + " §ahat den Player Head von §6" + args[0] + " §abekommen!");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[1]));
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/playerheads <SpielerName>"));
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
        }
        return false;
    }
}
