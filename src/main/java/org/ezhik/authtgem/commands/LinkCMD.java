package org.ezhik.authtgem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ezhik.authtgem.AuthTGEM;
import org.ezhik.authtgem.BotTelegram;
import org.ezhik.authtgem.Handler;
import org.ezhik.authtgem.User;

public class LinkCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        User user = User.getUser(p.getUniqueId());
        if (user == null) {
            String code = User.generateConfirmationCode();
            if (BotTelegram.bedrockPlayer.containsKey(p.getUniqueId())) BotTelegram.bedrockPlayer.remove(p.getUniqueId());
            Handler.kick(p.getName(), "Напишите в бота @BotFather код привязки: " + code);
            BotTelegram.bedrockPlayer.put(p.getUniqueId(), code);
            return true;
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l[&c&lAuthTG&f&l] &cВы уже привязали к аккаунту!"));
            return false;
        }
    }
}
