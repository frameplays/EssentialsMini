package de.framedev.essentialsmini.listeners;

import de.framedev.essentialsmini.main.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * This Plugin was Created by FrameDev
 * Package : de.framedev.essentialsmin.listeners
 * Date: 28.10.2020
 * Project: EssentialsMini
 * Copyrighted by FrameDev
 */
public class MoneySignListeners implements Listener {

    private Economy eco;

    public MoneySignListeners(Main plugin) {
        plugin.getListeners().add(this);
        if (plugin.getConfig().getBoolean("Economy.Activate")) {
            eco = plugin.getVaultManager().getEco();
        }
    }

    @EventHandler
    public void onSignChangeBalance(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[balance]")) {
            if (e.getPlayer().hasPermission("essentialsmini.signs.create")) {
                String signName = Main.getInstance().getConfig().getString("MoneySign.Balance");
                signName = signName.replace('&', '§');
                e.setLine(0, signName);
            } else {
                e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
            }
        }
    }

    @EventHandler
    public void onClickBalance(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getHand() == EquipmentSlot.OFF_HAND) {
                return;
            }
            if (e.getHand().equals(EquipmentSlot.HAND) &&
                    e.getClickedBlock().getState() instanceof Sign) {
                Sign s = (Sign) e.getClickedBlock().getState();
                String signName = Main.getInstance().getConfig().getString("MoneySign.Balance");
                signName = signName.replace('&', '§');
                if (s.getLine(0).equalsIgnoreCase(signName)) {
                    if (e.getPlayer().hasPermission("essentialsmini.signs.use")) {
                        String money = String.valueOf(eco.getBalance(e.getPlayer()));
                        String text = Main.getInstance().getCustomMessagesConfig().getString("Money.MSG.Balance");
                        text = text.replace("[Money]", money);
                        text = text.replace('&', '§');
                        e.getPlayer().sendMessage(text);
                    } else {
                        e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
                    }
                }
                String signNameFree = Main.getInstance().getConfig().getString("MoneySign.Free");
                signNameFree = signNameFree.replace('&', '§');
                if (s.getLine(0).equalsIgnoreCase(signNameFree)) {
                    if (e.getPlayer().hasPermission("essentialsmini.signs.use")) {
                        String[] args = s.getLines();
                        Material name = Material.getMaterial(args[1].toUpperCase());
                        int amount = Integer.parseInt(args[2]);
                        if (s.getLine(1).equalsIgnoreCase(name.name()) && s.getLine(2).equalsIgnoreCase(amount + "")) {
                            e.getPlayer().getInventory().addItem(new ItemStack(name, amount));
                        }
                    } else {
                        e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
                    }
                }
                String signNameBuy = Main.getInstance().getConfig().getString("MoneySign.Buy");
                signNameBuy = signNameBuy.replace('&', '§');
                if (s.getLine(0).equalsIgnoreCase(signNameBuy)) {
                    if (e.getPlayer().hasPermission("essentialsmini.signs.use")) {
                        String[] args = s.getLines();
                        Material name = Material.getMaterial(args[1].toUpperCase());

                        int amount = Integer.parseInt(args[2]);

                        int money = Integer.parseInt(args[3].replace(Main.getInstance().getCurrencySymbolMulti(), ""));
                        if (s.getLine(1).equalsIgnoreCase(name.name()) && s.getLine(2).equalsIgnoreCase(amount + "") && s.getLine(3).equalsIgnoreCase(money + "" + Main.getInstance().getCurrencySymbolMulti())) {
                            if (eco.getBalance(e.getPlayer()) < money) {
                                e.getPlayer().sendMessage("Not enought Money");
                                return;
                            }
                            ItemStack item = new ItemStack(name);
                            item.setAmount(amount);
                            e.getPlayer().getInventory().addItem(item);
                            eco.withdrawPlayer(e.getPlayer(), money);
                            e.getPlayer().sendMessage("§aYou bought §6" + name.name() + " §afor §6" + money + Main.getInstance().getCurrencySymbolMulti());
                        }
                    } else {
                        e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
                    }
                }
                String signNameSell = Main.getInstance().getConfig().getString("MoneySign.Sell");
                signNameSell = signNameSell.replace('&', '§');
                if (s.getLine(0).equalsIgnoreCase(signNameSell))
                    if (e.getPlayer().hasPermission("essentialsmini.signs.use")) {
                        String[] args = s.getLines();
                        Material name = Material.getMaterial(args[1].toUpperCase());

                        int amount = Integer.parseInt(args[2]);

                        int money = Integer.parseInt(args[3].replace(Main.getInstance().getCurrencySymbolMulti(), ""));
                        if (s.getLine(1).equalsIgnoreCase(name.name())
                                && s.getLine(2).equalsIgnoreCase(amount + "")
                                && s.getLine(3).equalsIgnoreCase(money + "" + Main.getInstance().getCurrencySymbolMulti())) {
                            if (e.getPlayer().getInventory().contains(name, amount)) {
                                ItemStack item = new ItemStack(name);
                                item.setAmount(amount);
                                e.getPlayer().getInventory().removeItem(item);
                                eco.depositPlayer(e.getPlayer(), money);
                                e.getPlayer().sendMessage("§aYou sell §6" + name.name() + " §afor §6" + money + Main.getInstance().getCurrencySymbolMulti());
                                return;
                            }
                            e.getPlayer().sendMessage("Not enought " + name.name());
                        }
                    } else {
                        e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
                    }
            }
        }
    }


    @EventHandler
    public void signChange(SignChangeEvent e) {
        String signName = Main.getInstance().getConfig().getString("MoneySign.Buy");
        signName = signName.replace('&', '§');
        if (e.getLine(0).equalsIgnoreCase("buy")) {
            String[] args = e.getLines();
            Material name = Material.getMaterial(args[1].toUpperCase());
            int amount = Integer.parseInt(args[2]);
            int money = Integer.parseInt(args[3]);
            if (e.getPlayer().hasPermission("essentialsmini.signs.create")) {
                if (e.getLine(1).equalsIgnoreCase(name.name()) &&
                        e.getLine(2).equalsIgnoreCase(amount + "") &&
                        e.getLine(3).equalsIgnoreCase(money + "" + Main.getInstance().getCurrencySymbolMulti())) {
                    e.setLine(0, signName);
                    e.setLine(1, name.name());
                    e.setLine(2, amount + "");
                    e.setLine(3, money + "" + Main.getInstance().getCurrencySymbolMulti());
                }
            } else {
                e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
            }
        }
    }

    @EventHandler
    public void SignChangeFree(SignChangeEvent e) {
        String signName = Main.getInstance().getConfig().getString("MoneySign.Free");
        signName = signName.replace('&', '§');
        if (e.getLine(0).equalsIgnoreCase("free")) {
            if (e.getPlayer().hasPermission("essentialsmini.signs.create")) {
                String[] args = e.getLines();
                Material name = Material.getMaterial(args[1].toUpperCase());
                int amount = Integer.parseInt(args[2]);
                if (e.getLine(1).equalsIgnoreCase(name.name()) &&
                        e.getLine(2).equalsIgnoreCase(amount + "")) {
                    e.setLine(0, signName);
                    e.setLine(1, name.name());
                    e.setLine(2, amount + "");
                }
            } else {
                e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
            }
        }
    }

    @EventHandler
    public void signChangeSell(SignChangeEvent e) {
        String signName = Main.getInstance().getConfig().getString("MoneySign.Sell");
        signName = signName.replace('&', '§');
        if (e.getLine(0).equalsIgnoreCase("sell")) {
            if (e.getPlayer().hasPermission("essentialsmini.signs.create")) {
                String[] args = e.getLines();
                Material name = Material.getMaterial(args[1].toUpperCase());
                int amount = Integer.parseInt(args[2]);
                int money = Integer.parseInt(args[3]);
                if (e.getLine(1).equalsIgnoreCase(name.name()) &&
                        e.getLine(2).equalsIgnoreCase(amount + "") &&
                        e.getLine(3).equalsIgnoreCase(money + "" + Main.getInstance().getCurrencySymbolMulti())) {
                    e.setLine(0, signName);
                    e.setLine(1, name.name());
                    e.setLine(2, amount + "");
                    e.setLine(3, money + "" + Main.getInstance().getCurrencySymbolMulti());
                }

            } else {
                e.getPlayer().sendMessage(Main.getInstance().getPrefix() + Main.getInstance().getNOPERMS());
            }
        }
    }
}
