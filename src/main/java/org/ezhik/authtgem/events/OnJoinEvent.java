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
import java.text.CompactNumberFormat;

public class OnJoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        User user = User.getUser(p.getUniqueId());
        File file = new File("plugins/AuthTG/users/" + p.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (AuthTGEM.bot.authNecessarily && !config.getBoolean("bypass")) {
            FreezerEvent.freezeplayer(p.getName());
            if (user == null) {
                String code = User.generateConfirmationCode();
                Handler.kick(p.getName(), "Напишите боту: @BotFather, и введите код: " + code);
                BotTelegram.bedrockPlayer.put(p.getUniqueId(), code);
            } else {
                user.sendLoginAccepted("Это вы вошли в игру?");
            }
        }
        if (user != null) {
            user.sendMessage(AuthTGEM.messageTG.get("user_login"));
        }
    }
}
