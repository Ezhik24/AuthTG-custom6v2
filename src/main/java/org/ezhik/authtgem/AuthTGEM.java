package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ezhik.authtgem.commands.*;
import org.ezhik.authtgem.events.*;
import org.ezhik.authtgem.message.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public final class AuthTGEM extends JavaPlugin {
    public static BotTelegram bot;
    public static MessageTranslationTG messageTG;
    public static MessageTranslationMC messageMC;

    @Override
    public void onEnable() {
        messageTG = new MessageTranslationTG();
        messageMC = new MessageTranslationMC();
        System.out.println("[AuthTG] Плагин включен!");
        System.out.println("[AuthTG] Plugin enabled!");
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoinEvent(), this);
        Handler handler = new Handler();
        handler.runTaskTimer(this,0,1);
        getCommand("setbypass").setExecutor(new SetBypass());
        getCommand("link").setExecutor(new LinkCMD());
        bot = new BotTelegram();
        if (bot.getBotToken() == "changeme" && bot.getBotUsername() == "changeme") {
            System.out.println("Please set your bot token and username in config.yml");
            System.out.println("Пожалуйста, укажите ваш токен и имя в config.yml");
        } else {
            TelegramBotsApi botsApi;
            try {
                botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(bot);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[AuthTG] Плагин выключен!");
        System.out.println("[AuthTG] Plugin disabled!");
    }
}