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

public class SetBypass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission("minetelegram.setbypass")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("setbypass_player_noperm")));
            return false;
        }
        if (strings[0] == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("setbypass_player_wrongcmd")));
            return false;
        }
        Player player1 = Bukkit.getPlayer(strings[0]);
        File file = new File("plugins/AuthTG/users/" + player1.getUniqueId() + ".yml");
        YamlConfiguration userconfig = YamlConfiguration.loadConfiguration(file);
        userconfig.set("bypass", true);
        if (!file.exists()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("setbypass_player_approve")));
            try {
                userconfig.save(file);
            } catch (IOException e) {
                System.out.println("Error saving file " + e);
            }
            return false;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',AuthTGEM.messageMC.get("setbypass_player_success")));
        try {
            userconfig.save(file);
        } catch (IOException e) {
            System.out.println("Error saving file " + e);
        }
        return true;

    }
}
