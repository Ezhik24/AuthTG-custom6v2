package org.ezhik.authtgem.events;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ezhik.authtgem.AuthTGEM;
import org.ezhik.authtgem.BotTelegram;
import org.ezhik.authtgem.Handler;
import org.ezhik.authtgem.User;

import java.io.File;
import java.sql.SQLException;

public class OnJoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player p = event.getPlayer();
        User user = User.getUser(p.getUniqueId());
        if (AuthTGEM.config.getBoolean("authNecessarily") && !AuthTGEM.connector.isBypass(p.getUniqueId())) {
            if (user == null) {
                String code = User.generateConfirmationCode();
                if (BotTelegram.bedrockPlayer.containsKey(p.getUniqueId())) BotTelegram.bedrockPlayer.remove(p.getUniqueId());
                Handler.kick(p.getName(), "Напишите в бот @BotFather код привязки: " + code);
                BotTelegram.bedrockPlayer.put(p.getUniqueId(), code);
            }
        }
    }
}
