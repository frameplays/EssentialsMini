/*
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ändern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.commands;

import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.managers.CommandBase;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author DHZoc
 */
public class TeleportCMD implements CommandExecutor {

    private final Main plugin;

    public TeleportCMD(Main plugin) {
        this.plugin = plugin;
        plugin.getCommands().put("tpa", this);
        plugin.getCommands().put("tpaaccept", this);
        plugin.getCommands().put("tpadeny", this);
        plugin.getCommands().put("tphereall", this);
        plugin.getCommands().put("tpahere", this);
        plugin.getCommands().put("tpahereaccept", this);
        plugin.getCommands().put("tpaheredeny", this);
    }

    private final HashMap<Player, Player> tpRequest = new HashMap<>();
    private final HashMap<Player, Player> tpHereRequest = new HashMap<>();

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command command, @Nonnull String label, @Nonnull String[] args) {
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != sender) {
                        if (target != null) {
                            tpRequest.put(target, (Player) sender);
                            sender.sendMessage(plugin.getPrefix() + "§aDu hast an §6" + target.getName()
                                    + " §aeine Teleportier Anfrage gesendet!");
                            target.sendMessage(plugin.getPrefix() + "§aDu hast von §6" + sender.getName()
                                    + " §aeine Teleportier Anfrage bekommen!");
                            BaseComponent baseComponent = new TextComponent();
                            baseComponent.addExtra("§6[Akzeptieren]");
                            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaaccept " + sender.getName()));
                            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§aAkzeptieren der Tpa Anfrage!").create()));
                            BaseComponent ablehnen = new TextComponent();
                            ablehnen.addExtra("§c[Ablehnen]");
                            ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpadeny " + sender.getName()));
                            ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§cAblehnen der TPA Anfrage!").create()));
                            target.spigot().sendMessage(baseComponent);
                            target.spigot().sendMessage(ablehnen);
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler ist nicht Online!");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cDu kannst nicht dir selber eine Teleportier Anfrage senden!");
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/tpa <PlayerName>"));
            }
        }
        if (command.getName().equalsIgnoreCase("tpaaccept")) {
            if (sender instanceof Player) {
                if (tpRequest.containsKey(sender)) {
                    tpRequest.get(sender).teleport(((Player) sender).getLocation());
                    sender.sendMessage(plugin.getPrefix() + "§6" + tpRequest.get(sender).getName()
                            + " §awurde zu dir Teleportiert!");
                    tpRequest.get(sender).sendMessage(
                            "§aDu wurdest zu §6" + sender.getName() + " §aTeleportiert!");
                    tpRequest.remove(sender);
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cDu hast keine Anfrage bekommen!");
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpadeny")) {
            if (sender instanceof Player) {
                if (tpRequest.containsKey(sender)) {
                    sender.sendMessage(plugin.getPrefix() + "§aDu hast die Telportier Anfrage abgelehnt!");
                    tpRequest.get(sender)
                            .sendMessage("§6" + sender.getName() + " §chat deine Teleportier Anfrage abgelehnt!");
                    tpRequest.remove(sender);
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpahere")) {
            if (sender instanceof Player) {
                Player target = Bukkit.getPlayer(args[0]);
                if (!tpHereRequest.containsKey(target) && tpHereRequest.get(target).getName().equalsIgnoreCase(args[0])) {
                    Player player = (Player) sender;
                    if (target != null) {
                        tpHereRequest.put(target, player);
                        target.sendMessage(plugin.getPrefix() + "§6" + player.getName() + " §aMöchte dich zu ihm Telportieren!");
                        player.sendMessage(plugin.getPrefix() + "§aDieser Spieler möchtest du zu dir Teleportieren §6" + target.getName());
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cDieser Spieler ist nicht Online!");
                    }
                } else {
                    Player player = (Player) sender;
                    if (target != null) {
                        tpHereRequest.put(target, player);
                        target.sendMessage(plugin.getPrefix() + "§6" + player.getName() + " §aMöchte dich zu ihm Telportieren!");
                        player.sendMessage(plugin.getPrefix() + "§aDieser Spieler möchtest du zu dir Teleportieren §6" + target.getName());
                    } else {
                        player.sendMessage(plugin.getPrefix() + "§cDieser Spieler ist nicht Online!");
                    }
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tpaheredeny")) {
            //*****
            //*****
            int i = 20;
            System.out.println(i);
        }
        if (command.getName().equalsIgnoreCase("tpahereaccept")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (tpHereRequest.containsKey(player)) {
                    tpHereRequest.get(player).teleport(player.getLocation());
                    tpHereRequest.remove(player);
                } else {
                    sender.sendMessage(plugin.getPrefix() + "§cDu hast keine Anfrage bekommen!");
                }
            }
        }
        if (command.getName().equalsIgnoreCase("tphereall")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("essentialsmini.tphereall")) {
                    Bukkit.getOnlinePlayers().forEach(players -> players.teleport(player.getLocation()));
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        return false;
    }
}
