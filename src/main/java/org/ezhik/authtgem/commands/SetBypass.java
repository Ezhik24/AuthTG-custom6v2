package org.ezhik.authtgem.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ezhik.authtgem.AuthTGEM;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SetBypass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("authtg.setbypass")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.config.getString("messages.minecraft.nopermbypass")));
            return false;
        }
        String message = String.join(" ", strings);
        if (message.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.config.getString("messages.minecraft.noargsbypass")));
            return false;
        }
        AuthTGEM.connector.setBypass(strings[0], true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',AuthTGEM.config.getString("messages.minecraft.bypassset").replace("{PLAYER}", strings[0])));
        return true;

    }
}
