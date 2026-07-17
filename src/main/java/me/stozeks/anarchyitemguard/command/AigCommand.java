package me.stozeks.anarchyitemguard.command;

import me.stozeks.anarchyitemguard.AnarchyItemGuardPlugin;
import me.stozeks.anarchyitemguard.manager.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class AigCommand implements CommandExecutor, TabCompleter {

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
        if (args.length == 0) {
            sendMessage(sender, "messages.usage");
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            sendMessage(sender, "messages.usage");
            return true;
        }

        if (!sender.hasPermission("anarchyitemguard.reload")) {
            sendMessage(sender, "messages.no-permission");
            return true;
        }

        plugin.reloadConfig();
        itemManager.reload();

        sendMessage(sender, "messages.reload-success");

        plugin.getLogger().info(
                sender.getName() + " перезагрузил конфигурацию плагина."
        );

        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        if (!sender.hasPermission("anarchyitemguard.reload")) {
            return Collections.emptyList();
        }

        String enteredText = args[0].toLowerCase();

        if ("reload".startsWith(enteredText)) {
            return Collections.singletonList("reload");
        }

        return Collections.emptyList();
    }

    private void sendMessage(
            CommandSender sender,
            String configPath
    ) {
        String message = plugin.getConfig().getString(configPath);

        if (message == null || message.trim().isEmpty()) {
            return;
        }

        sender.sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );
    }
}
