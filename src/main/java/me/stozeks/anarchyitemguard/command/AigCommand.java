package me.stozeks.anarchyitemguard.command;

import me.stozeks.anarchyitemguard.core.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AigCommand implements CommandExecutor, TabCompleter {

    private static final String RELOAD_PERMISSION =
            "anarchyitemguard.reload";

    private final AnarchyItemGuardPlugin plugin;
    private final ItemManager itemManager;

    public AigCommand(
            AnarchyItemGuardPlugin plugin,
            ItemManager itemManager
    ) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (args.length != 1
                || !args[0].equalsIgnoreCase("reload")) {
            sendMessage(sender, "messages.usage");
            return true;
        }

        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            sendMessage(sender, "messages.no-permission");
            return true;
        }

        try {
            plugin.reloadConfig();

            boolean reloadSuccessful = itemManager.reload();

            if (!reloadSuccessful) {
                sendMessage(sender, "messages.reload-failed");

                plugin.getLogger().warning(
                        "Configuration reload requested by "
                                + sender.getName()
                                + " failed validation."
                );

                return true;
            }

            sendMessage(sender, "messages.reload-success");

            plugin.getLogger().info(
                    "Configuration reloaded by "
                            + sender.getName()
                            + "."
            );
        } catch (Exception exception) {
            sendMessage(sender, "messages.reload-failed");

            plugin.getLogger().severe(
                    "Unexpected error while reloading the configuration: "
                            + exception.getMessage()
            );

            exception.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {
        if (args.length != 1
                || !sender.hasPermission(RELOAD_PERMISSION)) {
            return Collections.emptyList();
        }

        String enteredText =
                args[0].toLowerCase(Locale.ROOT);

        if ("reload".startsWith(enteredText)) {
            return Collections.singletonList("reload");
        }

        return Collections.emptyList();
    }

    private void sendMessage(
            CommandSender sender,
            String configPath
    ) {
        String message =
                plugin.getConfig().getString(configPath);

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        sender.sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );
    }
}