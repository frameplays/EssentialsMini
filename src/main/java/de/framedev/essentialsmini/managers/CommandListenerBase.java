package de.framedev.essentialsmini.managers;

import de.framedev.essentialsmini.main.Main;
import lombok.NonNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * / This Plugin was Created by FrameDev
 * / Package : de.framedev.essentialsmini.managers
 * / ClassName CommandListenerBase
 * / Date: 24.08.21
 * / Project: EssentialsMini
 * / Copyrighted by FrameDev
 */

public abstract class CommandListenerBase extends CommandBase implements Listener {

    public CommandListenerBase(Main plugin) {
        super(plugin);
    }

    public CommandListenerBase(Main plugin, @NonNull String cmdName) {
        super(plugin, cmdName);
    }

    public CommandListenerBase(Main plugin, @NonNull String cmdName, CommandExecutor executor) {
        super(plugin, cmdName, executor);
    }

    public CommandListenerBase(Main plugin, CommandExecutor executor, @NotNull String... cmdNames) {
        super(plugin, executor, cmdNames);
    }

    public void setupListener(Listener listener) {
        getPlugin().getListeners().add(listener);
    }
}
