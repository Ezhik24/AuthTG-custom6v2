package org.ezhik.authtgem.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ezhik.authtgem.AuthTGEM;
import org.ezhik.authtgem.Handler;

import java.io.File;
import java.io.IOException;

public class UnRegisterCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        File file = new File("plugins/AuthTG/users/" + player.getUniqueId() + ".yml");
        YamlConfiguration userconfig = YamlConfiguration.loadConfiguration(file);
        if (strings.length == 0) {
            if (userconfig.getBoolean("unregister")) {
                file.delete();
                Handler.kick(player.getName(), AuthTGEM.messageMC.get("unregister_success"));
                return true;
            } else {
                userconfig.set("unregister", true);
                try {
                    userconfig.save(file);
                } catch (IOException e) {
                    System.out.println("Error saving config file: " + e);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("unregister_approve")));
                return false;
            }
        }
        if (strings.length == 1) {
            if (player.hasPermission("minetelegram.unregister")) {
                Player player1 = Bukkit.getPlayer(strings[0]);
                File file1 = new File("plugins/AuthTG/users/" + player1.getUniqueId() + ".yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file1);
                if (config.getBoolean("unregister")) {
                    file1.delete();
                    Handler.kick(player1.getName(), AuthTGEM.messageMC.get("unregister_success_pl_another"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.getPlayerUnregister(player1)));
                    return true;
                } else {
                    config.set("unregister", true);
                    try {
                        config.save(file1);
                    } catch (IOException e) {
                        System.out.println("Error saving config file: " + e);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("unregister_approve")));
                    return false;
                }
            } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("unregister_noperm")));
        }
        return true;
    }
}
