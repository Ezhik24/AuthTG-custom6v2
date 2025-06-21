package org.ezhik.authtgem.events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ezhik.authtgem.AuthTGEM;
import org.ezhik.authtgem.BotTelegram;
import org.ezhik.authtgem.Handler;
import org.ezhik.authtgem.User;
import org.geysermc.api.Geyser;
import org.geysermc.api.GeyserApiBase;

import java.io.File;
import java.io.IOException;

public class OnJoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        User user = User.getUser(p.getUniqueId());
        if (AuthTGEM.bot.authNecessarily) {
            FreezerEvent.freezeplayer(p.getName());
            if (user == null) {
                String code = User.generateConfirmationCode();
                Handler.kick(p.getName(), "Напишите боту: @BotFather, и введите код: " + code);
            } else {
                user.sendLoginAccepted("Это вы вошли в игру?");
            }
        }
        if (user != null) {
            user.sendMessage(AuthTGEM.messageTG.get("user_login"));
        }
    }
}
