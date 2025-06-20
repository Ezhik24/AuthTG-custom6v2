package org.ezhik.authtgem.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.ezhik.authtgem.AuthTGEM;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.SocketHandler;

public class SetBypass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = null;
        if (commandSender instanceof Player) {
            player = (Player) commandSender;
        }
        if (player != null && !player.hasPermission("minetelegram.setbypass")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("setbypass_player_noperm")));
            return false;
        }
        if (player != null && strings[0] == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("setbypass_player_wrongcmd")));
            return false;
        } else if (strings[0] == null) {
            System.out.println(AuthTGEM.messageMC.get("setbypass_console_wrongcmd"));
        }
        Player player1 = Bukkit.getPlayer(strings[0]);
        File file = new File("plugins/AuthTG/users/" + player1.getUniqueId() + ".yml");
        YamlConfiguration userconfig = new YamlConfiguration();
        try {
            userconfig.load(file);
            userconfig.set("bypass", true);
        } catch (IOException e) {
            System.out.println("Error " + e);
        } catch (InvalidConfigurationException e) {
            System.out.println("Error " + e);
        }
        if (!file.exists()) {
            if (player != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("setbypass_player_approve")));
            } else {
                System.out.println(AuthTGEM.messageMC.get("setbypass_console_approve"));
            }
            try {
                userconfig.save(file);
            } catch (IOException e) {
                System.out.println("Error saving file " + e);
            }
            return false;
        }
        if (player != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',AuthTGEM.messageMC.get("setbypass_player_success")));
        } else {
            System.out.println(AuthTGEM.messageMC.get("setbypass_console_success"));
        }
        try {
            userconfig.save(file);
        } catch (IOException e) {
            System.out.println("Error saving file " + e);
        }
        return true;

    }
}
