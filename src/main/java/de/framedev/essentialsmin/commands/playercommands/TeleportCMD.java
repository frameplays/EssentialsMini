/*
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ändern, @Copyright by FrameDev
 */
package de.framedev.essentialsmin.commands.playercommands;

import de.framedev.essentialsmin.main.Main;
import lombok.NonNull;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author DHZoc
 */
public class TeleportCMD implements CommandExecutor, Listener {

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
        plugin.getCommands().put("tptoggle", this);
        plugin.getListeners().add(this);
    }

    private final HashMap<Player, Player> tpRequest = new HashMap<>();
    private final HashMap<Player, Player> tpHereRequest = new HashMap<>();
    private final ArrayList<Player> tpToggle = new ArrayList<>();

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, @NonNull String[] args) {
        if(command.getName().equalsIgnoreCase("tptoggle")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission(plugin.getPermissionName() + "tptoggle")) {
                    if (tpToggle.contains(player)) {
                        tpToggle.remove(player);
                        player.sendMessage(plugin.getPrefix() + "§aPlayers can now Teleport to you or send you a Tpa Request!");
                        return true;
                    } else {
                        tpToggle.add(player);
                        player.sendMessage(plugin.getPrefix() + "§6Players §ccan now more Teleporting to you or Send a Tpa Request");
                        return true;
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getNOPERMS());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getOnlyPlayer());
            }
        }
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != sender) {
                        if (target != null) {
                            if(!tpToggle.contains(target)) {
                                tpRequest.put(target, (Player) sender);
                                String send = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportSend");
                                send = send.replace('&', '§');
                                if (send.contains("%Target%")) {
                                    send = send.replace("%Target%", target.getName());
                                }
                                sender.sendMessage(plugin.getPrefix() + send);
                                String got = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportGot");
                                got = got.replace('&', '§');
                                if (got.contains("%Player%")) {
                                    got = got.replace("%Player%", sender.getName());
                                }
                                target.sendMessage(plugin.getPrefix() + got);
                                BaseComponent baseComponent = new TextComponent();
                                baseComponent.addExtra("§6[Accept]");
                                baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + sender.getName()));
                                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept Tpa Request!").create()));
                                BaseComponent ablehnen = new TextComponent();
                                ablehnen.addExtra("§c[Deny]");
                                ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + sender.getName()));
                                ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny Tpa Request!").create()));
                                target.spigot().sendMessage(baseComponent);
                                target.spigot().sendMessage(ablehnen);
                            } else if(sender.hasPermission(plugin.getPermissionName() + "tptoggle.bypass")) {
                                tpRequest.put(target, (Player) sender);
                                String send = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportSend");
                                send = send.replace('&', '§');
                                if (send.contains("%Target%")) {
                                    send = send.replace("%Target%", target.getName());
                                }
                                sender.sendMessage(plugin.getPrefix() + send);
                                String got = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportGot");
                                got = got.replace('&', '§');
                                if (got.contains("%Player%")) {
                                    got = got.replace("%Player%", sender.getName());
                                }
                                target.sendMessage(plugin.getPrefix() + got);
                                BaseComponent baseComponent = new TextComponent();
                                baseComponent.addExtra("§6[Accept]");
                                baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + sender.getName()));
                                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept Tpa Request!").create()));
                                BaseComponent ablehnen = new TextComponent();
                                ablehnen.addExtra("§c[Deny]");
                                ablehnen.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + sender.getName()));
                                ablehnen.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny Tpa Request!").create()));
                                target.spigot().sendMessage(baseComponent);
                                target.spigot().sendMessage(ablehnen);
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§cThis Player doesn't accept Teleport!");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + "§cYou cannot send to your self a Tpa Request!");
                    }
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + plugin.getWrongArgs("/tpa <PlayerName>"));
            }
        }
        if (command.getName().equalsIgnoreCase("tpaaccept")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (tpRequest.containsKey(player)) {
                    if(plugin.getConfig().getBoolean("TeleportInOtherWorld")) {
                        tpRequest.get(player).teleport(player);
                    } else {
                        if(player.getWorld().getName().equalsIgnoreCase(tpRequest.get(player).getWorld().getName())) {
                            tpRequest.get(player).teleport(player);
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§aThe Player §6" + tpRequest.get(player).getName() + " §cis not in the same World!");
                            tpRequest.remove(player);
                            return true;
                        }
                    }
                    String targetMessage = plugin.getCustomMessagesConfig().getString("TpaMessages.TargetMessage");
                    targetMessage = targetMessage.replace('&','§');
                    if(targetMessage.contains("%Target%"))
                        targetMessage = targetMessage.replace("%Target%",tpRequest.get(sender).getName());
                    sender.sendMessage(plugin.getPrefix() + targetMessage);
                    String teleportTo = plugin.getCustomMessagesConfig().getString("TpaMessages.TeleportToPlayer");
                    teleportTo = teleportTo.replace('&','§');
                    if(teleportTo.contains("%Player%")) {
                        teleportTo = teleportTo.replace("%Player%",sender.getName());
                    }
                    tpRequest.get(sender).sendMessage(plugin.getPrefix() + teleportTo);
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
                    Player player = (Player) sender;
                    if (target != null) {
                        if(!tpToggle.contains(player)) {
                            tpHereRequest.put(target, player);
                            target.sendMessage(plugin.getPrefix() + "§6" + player.getName() + " §aMöchte dich zu ihm Telportieren!");
                            player.sendMessage(plugin.getPrefix() + "§aDieser Spieler möchtest du zu dir Teleportieren §6" + target.getName());
                        } else if(player.hasPermission(plugin.getPermissionName() + "tptoggle.bypass")) {
                            tpHereRequest.put(target, player);
                            target.sendMessage(plugin.getPrefix() + "§6" + player.getName() + " §aMöchte dich zu ihm Telportieren!");
                            player.sendMessage(plugin.getPrefix() + "§aDieser Spieler möchtest du zu dir Teleportieren §6" + target.getName());
                        } else {
                            sender.sendMessage(plugin.getPrefix() + "§cThis Player doesn't accept Teleport!");
                        }
                    } else {
                        player.sendMessage(plugin.getPrefix() + plugin.getVariables().getPlayerNameNotOnline(args[0]));
                    }
            }
        }
        if (command.getName().equalsIgnoreCase("tpaheredeny")) {

        }
        if (command.getName().equalsIgnoreCase("tpahereaccept")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!tpHereRequest.isEmpty() && tpHereRequest.containsKey(player)) {
                    if(plugin.getConfig().getBoolean("TeleportInOtherWorld")) {
                        player.teleport(tpHereRequest.get(player).getLocation());
                    } else {
                        if(tpHereRequest.get(player).getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
                            player.teleport(tpHereRequest.get(player).getLocation());
                        } else {
                            player.sendMessage(plugin.getPrefix() + "§aThe Player §6" + tpHereRequest.get(sender).getName() + " §cis not in the same World!");
                            tpHereRequest.remove(player);
                            return true;
                        }
                    }
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

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!plugin.getConfig().getBoolean("TeleportInOtherWorld")) {
            Player player = event.getPlayer();
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                if(Objects.equals(event.getTo(), player1.getLocation())) {
                    if(!player1.getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
                        player.sendMessage(plugin.getPrefix() + "§6" + player1.getName() + " §cis not in the same World!");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
