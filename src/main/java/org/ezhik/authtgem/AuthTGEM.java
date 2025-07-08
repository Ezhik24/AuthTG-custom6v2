package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.ezhik.authtgem.commands.*;
import org.ezhik.authtgem.events.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.io.File;
import java.sql.SQLException;

public final class AuthTGEM extends JavaPlugin {
    public static BotTelegram bot;
    public static MySQLConnector connector;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        config = getConfig();
        try {
            connector = new MySQLConnector(config.getString("mysql.host"),config.getString("mysql.database"),config.getString("mysql.username"),config.getString("mysql.password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[AuthTG] Plugin enabled!");
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoinEvent(), this);
        Handler handler = new Handler();
        handler.runTaskTimer(this,0,1);
        getCommand("setbypass").setExecutor(new SetBypass());
        getCommand("link").setExecutor(new LinkCMD());
        bot = new BotTelegram(config.getString("bot.username"),config.getString("bot.token"));
        if (bot.getBotToken().equals("changeme") && bot.getBotUsername().equals("changeme")) {
            System.out.println("[AuthTG] Please set your bot token and username in config.yml");
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
        System.out.println("[AuthTG] Plugin disabled!");
    }
}