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
        if (!player.hasPermission("minetelegram.setbypass")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&lAuthTG&f&l] &cУ вас недостаточно прав!"));
            return false;
        }
        if (strings[0] == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&lAuthTG&f&l] &cВы не указали ник!"));
            return false;
        }
        Player player1 = Bukkit.getPlayer(strings[0]);
        if (player1 == null) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&lAuthTG&f&l] &cИгрок не онлайн!"));
            return false;
        }
        AuthTGEM.connector.setBypass(player1.getUniqueId(), true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l[&c&lAuthTG&f&l] &cВы установили байпас для игрока &6" + player1.getName() + "&c!"));
        return true;

    }
}
