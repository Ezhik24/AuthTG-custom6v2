package org.ezhik.authtgem.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.checkerframework.checker.units.qual.A;
import org.ezhik.authtgem.AuthTGEM;

import java.io.File;

public class BlockCommandEvent implements Listener {
    @EventHandler
    public void onCommmand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(MuterEvent.isMute(player)) {
           if(!(event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register") || event.getMessage().startsWith("/reg") || event.getMessage().startsWith("/l") || event.getMessage().startsWith("/code"))) {
               event.setCancelled(true);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("command_block")));
           }
        }
        if (event.getMessage().startsWith("/ban")) {
            if (player.hasPermission("ab.ban.perma") && !FreezerEvent.isFreeze(player)) {
                String[] args = event.getMessage().split(" ");
                if (args.length == 4) {
                    AuthTGEM.bot.sendMessageThread(AuthTGEM.bot.adminChatID, AuthTGEM.bot.threadChatID, AuthTGEM.messageMC.getBanPlayerName(args[0],args[2],args[1]));
                }
            }
        }
        if (event.getMessage().startsWith("/mute") && !FreezerEvent.isFreeze(player)) {
            if (player.hasPermission("ab.mute.perma")) {
                String[] args = event.getMessage().split(" ");
                if (args.length == 4) {
                    AuthTGEM.bot.sendMessageThread(AuthTGEM.bot.adminChatID, AuthTGEM.bot.threadChatID, AuthTGEM.messageMC.getMutePlayerName(args[0],args[2],args[1]));
                }
            }
        }
        if (event.getMessage().startsWith("/warn") && !FreezerEvent.isFreeze(player)) {
            if (player.hasPermission("ab.warn.perma")) {
                String[] args = event.getMessage().split(" ");
                if (args.length == 4) {
                    AuthTGEM.bot.sendMessageThread(AuthTGEM.bot.adminChatID, AuthTGEM.bot.threadChatID, AuthTGEM.messageMC.getWarnPlayerName(args[0],args[2],args[1]));
                }
            }
        }
    }
}
