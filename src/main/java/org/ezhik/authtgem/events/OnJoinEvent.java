package org.ezhik.authtgem.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ezhik.authtgem.AuthTGEM;
import org.ezhik.authtgem.BotTelegram;
import org.ezhik.authtgem.Handler;
import org.ezhik.authtgem.User;

public class OnJoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        AuthTGEM.connector.setPlayerName(p.getUniqueId(), p.getName());
        if (AuthTGEM.config.getBoolean("authNecessarily") && !AuthTGEM.connector.isBypass(p.getUniqueId())) {
            if (!AuthTGEM.connector.isActive(p.getUniqueId())) {
                String code = User.generateConfirmationCode();
                if (BotTelegram.bedrockPlayer.containsKey(p.getUniqueId())) BotTelegram.bedrockPlayer.remove(p.getUniqueId());
                Handler.kick(p.getName(), "Напишите в бот @BotFather код привязки: " + code);
                BotTelegram.bedrockPlayer.put(p.getUniqueId(), code);
            }
        }
    }
}
