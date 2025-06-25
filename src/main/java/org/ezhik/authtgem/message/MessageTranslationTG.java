package org.ezhik.authtgem.message;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

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
            this.put("account_choose", "Выберите игрока");
            this.put("code_account_activated_auth", "[Бот] Ваш аккаунт успешно активирован!");
            this.put("acc_choose","[Бот] Выбран игрок {PLAYER}");
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

    public String getAccChoosePN(String playername) {
        return this.get("acc_choose").replace("{PLAYER}",playername);
    }
}
