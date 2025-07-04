package org.ezhik.authtgem.commands;

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
            Handler.kick(p.getName(), AuthTGEM.messageMC.getLinkAccountCode(code));
            BotTelegram.bedrockPlayer.put(p.getUniqueId(), code);
        }
        return true;
    }
}
