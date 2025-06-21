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

public class MessageTranslationMC extends LinkedHashMap<String, String> {
    public  MessageTranslationMC() {
        File configfile = new File("plugins/AuthTG/messages/messageMC_RU.yml");
        YamlConfiguration messageconfig = new YamlConfiguration();
        try {
            messageconfig.load(configfile);
        } catch (FileNotFoundException e) {
            this.put("command_block","&f&l[&b&lMT&f&l] &c&lЭта команда доступна только для зарегистрированных пользователей!");
            this.put("succes_login_account"," &f&l[&b&lMT&f&l] &a&lУспешный вход в аккаунт!");
            this.put("rejected_login_account"," &f&l[&b&lMT&f&l] &c&lОтклонено Владельцем учетной записи из Телеграмма");
            this.put("joinAnotherLocate", "&c&lКто-то уже играет с этого никнейма...");
            this.put("setbypass_player_noperm", "&f&l[&b&lMT&f&l] &c&lУ вас нет доступа");
            this.put("setbypass_player_wrongcmd", "&f&l[&b&lMT&f&l] &c&lКоманда введена неверна,введите: /setbypass <ник>");
            this.put("setbypass_console_wrongcmd", "[AuthTG] Команда введена неверна,введите: /setbypass <ник>");
            this.put("setbypass_player_approve", "&f&l[&b&lMT&f&l] &aДанный игрок не зарегистрирован,если вы действительно хотите выдать bypass введите команду еще раз");
            this.put("setbypass_console_approve", "[AuthTG] Данный игрок не зарегистрирован,если вы действительно хотите выдать bypass введите команду еще раз");
            this.put("setbypass_player_success", "&f&l[&b&lMT&f&l] &aУспешно!");
            this.put("setbypass_console_success", "Успешно");
            this.put("ban", "[Бот] На сервере было выдан бан {BR} Игрок: {PLAYER} {BR} Причина: {REASON} {BR} Длительность: {TIME}");
            this.put("mute", "[Бот] На сервере было выдан мут {BR} Игрок: {PLAYER} {BR} Причина: {REASON} {BR} Длительность: {TIME}");
            this.put("warn", "[Бот] На сервере было выдан варн {BR} Игрок: {PLAYER} {BR} Причина: {REASON} {BR} Длительность: {TIME}");
            File newconfigfile = new File("plugins/AuthTG/messages/messageMC_RU.yml");
            YamlConfiguration newmessageconfig = new YamlConfiguration();
            for (String key : this.keySet()) {
                newmessageconfig.set(key, this.get(key));
                this.replace(key, this.get(key).replace("{BR}", "\n"));
            };
            try {
                newmessageconfig.save(newconfigfile);
            } catch (IOException ex) {
                System.out.println("Error saving config file: " + ex);
            }
        }
        catch (IOException e) {
            System.out.println("Error loading config file: " + e);
        } catch (InvalidConfigurationException e) {
            System.out.println("Error parsing config file: " + e);
        }
        for (String key : messageconfig.getKeys(false)) {
            this.put(key, messageconfig.getString(key).replace("{BR}", "\n"));
        }
    }

    public String getBanPlayerName(String playername, String reason, String time) {
        return this.get("ban").replace("{PLAYER}", playername).replace("{REASON}", reason).replace("{TIME}", time);
    }
    public String getMutePlayerName(String playername, String reason, String time) {
        return this.get("mute").replace("{PLAYER}", playername).replace("{REASON}", reason).replace("{TIME}", time);
    }
    public String getWarnPlayerName(String playername, String reason, String time) {
        return this.get("warn").replace("{PLAYER}", playername).replace("{REASON}", reason).replace("{TIME}", time);
    }
}