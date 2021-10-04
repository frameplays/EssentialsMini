package de.framedev.essentialsmini.commands.playercommands;

import de.framedev.essentialsmini.main.Main;
import de.framedev.essentialsmini.managers.CommandBase;
import de.framedev.essentialsmini.utils.TextUtils;
import de.framedev.essentialsmini.utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.commands.playercommands
 * / ClassName ExperienceCMD
 * / Date: 03.10.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public class ExperienceCMD extends CommandBase {

    private final TextUtils textUtils;

    public ExperienceCMD(Main plugin) {
        super(plugin, "xp");
        this.textUtils = new TextUtils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 4) {
            if (!sender.hasPermission(getPlugin().getPermissionName() + "xp")) {
                sender.sendMessage(getPlugin().getPrefix() + getPlugin().getNOPERMS());
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (!textUtils.isInteger(args[1])) {
                    sender.sendMessage(getPlugin().getPrefix() + "§6" + args[1] + " §cis no Number!");
                    return true;
                }
                int amount = Integer.parseInt(args[1]);
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNameNotOnline(args[1]));
                    return true;
                }
                String xpMessage = getPlugin().getCustomMessagesConfig().getString(Variables.EXPERIENCE + "XP");
                xpMessage = textUtils.replaceAndToParagraph(xpMessage);
                xpMessage = textUtils.replaceObject(xpMessage, "%XP%", amount + "");
                String levelMessage = getPlugin().getCustomMessagesConfig().getString(Variables.EXPERIENCE + "Level");
                levelMessage = textUtils.replaceAndToParagraph(levelMessage);
                levelMessage = textUtils.replaceObject(levelMessage, "%Level%", amount + "");
                if (args[3].equalsIgnoreCase("level")) {
                    player.setLevel(amount);
                    player.sendMessage(getPlugin().getPrefix() + levelMessage);
                } else if (args[3].equalsIgnoreCase("xp")) {
                    player.setTotalExperience(amount);
                    player.sendMessage(getPlugin().getPrefix() + xpMessage);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("add")) {
                if (!textUtils.isInteger(args[1])) {
                    sender.sendMessage(getPlugin().getPrefix() + "§6" + args[1] + " §cis no Number!");
                    return true;
                }
                int amount = Integer.parseInt(args[1]);
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNameNotOnline(args[1]));
                    return true;
                }
                int level = player.getLevel();
                level += amount;
                int xp = player.getTotalExperience();
                xp += amount;
                String xpMessage = getPlugin().getCustomMessagesConfig().getString(Variables.EXPERIENCE + "XP");
                xpMessage = textUtils.replaceAndToParagraph(xpMessage);
                xpMessage = textUtils.replaceObject(xpMessage, "%XP%", xp + "");
                String levelMessage = getPlugin().getCustomMessagesConfig().getString(Variables.EXPERIENCE + "Level");
                levelMessage = textUtils.replaceAndToParagraph(levelMessage);
                levelMessage = textUtils.replaceObject(levelMessage, "%Level%", level + "");
                if (args[3].equalsIgnoreCase("level")) {
                    player.setLevel(level);
                    player.sendMessage(getPlugin().getPrefix() + levelMessage);
                } else if (args[3].equalsIgnoreCase("xp")) {
                    player.setTotalExperience(xp);
                    player.sendMessage(getPlugin().getPrefix() + xpMessage);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (!textUtils.isInteger(args[1])) {
                    sender.sendMessage(getPlugin().getPrefix() + "§6" + args[1] + " §cis no Number!");
                    return true;
                }
                int amount = Integer.parseInt(args[1]);
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(getPlugin().getPrefix() + getPlugin().getVariables().getPlayerNameNotOnline(args[1]));
                    return true;
                }
                int level = player.getLevel();
                level -= amount;
                int xp = player.getTotalExperience();
                xp -= amount;
                String xpMessage = getPlugin().getCustomMessagesConfig().getString(Variables.EXPERIENCE + "XP");
                xpMessage = textUtils.replaceAndToParagraph(xpMessage);
                xpMessage = textUtils.replaceObject(xpMessage, "%XP%", xp + "");
                String levelMessage = getPlugin().getCustomMessagesConfig().getString(Variables.EXPERIENCE + "Level");
                levelMessage = textUtils.replaceAndToParagraph(levelMessage);
                levelMessage = textUtils.replaceObject(levelMessage, "%Level%", level + "");
                if (args[3].equalsIgnoreCase("level")) {
                    player.setLevel(level);
                    player.sendMessage(getPlugin().getPrefix() + levelMessage);
                } else if (args[3].equalsIgnoreCase("xp")) {
                    player.setTotalExperience(xp);
                    player.sendMessage(getPlugin().getPrefix() + xpMessage);
                }
                return true;
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        commands.add("set");
        commands.add("add");
        commands.add("remove");
        if (args.length == 1) {
            List<String> empty = new ArrayList<>();
            for (String s : commands) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase()))
                    empty.add(s);
            }
            Collections.sort(empty);
            return empty;
        } else if (args.length == 4) {
            List<String> cmds = new ArrayList<>();
            cmds.add("level");
            cmds.add("xp");
            List<String> empty = new ArrayList<>();
            for (String s : cmds) {
                if (s.toLowerCase().startsWith(args[3].toLowerCase()))
                    empty.add(s);
            }
            Collections.sort(empty);
            return empty;
        }
        return super.onTabComplete(sender, command, label, args);
    }
}
