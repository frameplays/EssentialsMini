package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.commands.playercommands
 * / ClassName FireworkCMD
 * / Date: 27.06.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class FireWorkCMD extends CommandBase {

    public FireWorkCMD(Main plugin) {
        super(plugin, "firework");
        setup(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getPlugin().getPrefix() + getPlugin().getOnlyPlayer());
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(getPlugin().getPermissionName() + "firework")) {
            player.sendMessage(getPlugin().getPrefix() + getPlugin().getNOPERMS());
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() != Material.FIREWORK_ROCKET) {
                player.sendMessage(getPlugin().getPrefix() + "§cKein Feuerwerksrackete in der Hand gefunden!");
                return true;
            }
            FireworkMeta effect = (FireworkMeta) itemStack.getItemMeta();
            FireworkEffect.Type type = FireworkEffect.Type.valueOf(args[1].toUpperCase());
            if (type == null) {
                sender.sendMessage(getPlugin().getPrefix() + "§aFeuerwerk Typ nicht gefunden!");
                return true;
            }
            if (args.length == 3) {
                List<Color> colors = new ArrayList<>();
                if (args[2].contains(":")) {
                    String[] s = args[2].split(":");
                    for (String a : s) {
                        colors.add(DyeColor.valueOf(a.toUpperCase()).getColor());
                    }
                } else {
                    colors.add(DyeColor.valueOf(args[2].toUpperCase()).getColor());
                }
                effect.addEffect(FireworkEffect.builder().with(type).withColor(colors.toArray(new Color[0])).build());
                itemStack.setItemMeta(effect);
                return true;
            }

            if(args.length == 4) {
                List<Color> colors = new ArrayList<>();
                if (args[2].contains(":")) {
                    String[] s = args[2].split(":");
                    for (String a : s) {
                        colors.add(DyeColor.valueOf(a.toUpperCase()).getColor());
                    }
                } else {
                    colors.add(DyeColor.valueOf(args[2].toUpperCase()).getColor());
                }
                effect.addEffect(FireworkEffect.builder().with(type).withColor(colors.toArray(new Color[0])).build());
                effect.setPower(Integer.parseInt(args[3]));
                itemStack.setItemMeta(effect);
            }
            if(args.length == 5) {
                List<Color> colors = new ArrayList<>();
                if (args[2].contains(":")) {
                    String[] s = args[2].split(":");
                    for (String a : s) {
                        colors.add(DyeColor.valueOf(a.toUpperCase()).getColor());
                    }
                } else {
                    colors.add(DyeColor.valueOf(args[2].toUpperCase()).getColor());
                }
                effect.addEffect(FireworkEffect.builder().with(type).withColor(colors.toArray(new Color[0])).flicker(Boolean.parseBoolean(args[4])).build());
                effect.setPower(Integer.parseInt(args[3]));
                itemStack.setItemMeta(effect);
            }
            if(args.length == 6) {
                List<Color> colors = new ArrayList<>();
                if (args[2].contains(":")) {
                    String[] s = args[2].split(":");
                    for (String a : s) {
                        colors.add(DyeColor.valueOf(a.toUpperCase()).getColor());
                    }
                } else {
                    colors.add(DyeColor.valueOf(args[2].toUpperCase()).getColor());
                }
                effect.addEffect(FireworkEffect.builder().with(type).withColor(colors.toArray(new Color[0])).flicker(Boolean.parseBoolean(args[4])).trail(Boolean.parseBoolean(args[5])).build());
                effect.setPower(Integer.parseInt(args[3]));
                itemStack.setItemMeta(effect);
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
