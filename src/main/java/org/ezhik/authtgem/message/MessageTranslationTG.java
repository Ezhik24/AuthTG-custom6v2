package org.ezhik.authtgem.message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ezhik.authtgem.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;

public class MessageTranslationTG extends LinkedHashMap<String, String> {

    public  MessageTranslationTG() {
        File configfile = new File("plugins/AuthTG/messages/messageTG_RU.yml");
        YamlConfiguration messageconfig = new YamlConfiguration();
        try {
            messageconfig.load(configfile);

        } catch (FileNotFoundException e) {
            this.put("sendMessage_prefix", "[Бот@" + "{PLAYER}" + "] ");
            this.put("sendMessageBroadCast", "[Бот] ");
            this.put("login_intg_yes", "Да");
            this.put("login_intg_not", "Нет");
            this.put("account_choose", "Выберите игрока");
            this.put("code_account_activated", "Ваш аккаунт успешно активирован!");
            this.put("code_account_activated_auth", "[Бот] Ваш аккаунт успешно активирован!");
            this.put("login_who_entered","[Бот] Это вы вошли в игру?");
            this.put("kick_account_inTG", "Владелец кикнул аккаунт через телеграмм");
            this.put("kickme_player_notfound","[Бот] Пользователь не зарегистрирован или он не в игре");
            this.put("unlink_player_notfound", "[Бот] Зайди в игру и попробуй еще раз");
            this.put("kickme_kick_succes", " Вы успешно кикнули свой аккаунт через телеграми!");
            this.put("account_already_tgasign", "[Бот] Вы уже привязывали эту учетную запись");
            this.put("account_already_tgasign_round", "[Бот] Эта учетная запись уже привязана к другой учетной записи Телегримма");
            this.put("acc_choose","[Бот] Выбран игрок {PLAYER}");
            this.put("user_login", "Ваш аккаунт вошёл в игру");
            File newconfigfile = new File("plugins/AuthTG/messages/messageTG_RU.yml");
            YamlConfiguration newmessageconfig = new YamlConfiguration();
            for (String key : this.keySet()) {
                newmessageconfig.set(key, this.get(key));
                this.replace(key, this.get(key).replace("{BR}", "\n"));
            }
            try {
                newmessageconfig.save(newconfigfile);
            } catch (IOException ex) {
                System.out.println("Error saving config file: " + ex);
            }
        } catch (IOException e) {
            System.out.println("Error loading config file: " + e);
        } catch (InvalidConfigurationException e) {
            System.out.println("Error parsing config file: " + e);
        }
        for (String key : messageconfig.getKeys(false)) {
            this.put(key, messageconfig.getString(key).replace("{BR}", "\n"));
        }
    }

    public String getPlayerNameSM(User user) {
        return this.get("sendMessage_prefix").replace("{PLAYER}", user.playername);
    }

    public String getAccChoosePN(String playername) {
        return this.get("acc_choose").replace("{PLAYER}",playername);
    }
}
