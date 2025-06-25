package org.ezhik.authtgem;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BotTelegram extends TelegramLongPollingBot {
    private String username = "changeme";
    private String token = "changeme";
    public static Map<String, String> curentplayer = new HashMap<>();
    public static Map<UUID,String> bedrockPlayer = new HashMap<>();
    public boolean authNecessarily = false;

    public BotTelegram() {
        YamlConfiguration config = new YamlConfiguration();
        File file = new File("plugins/AuthTG/config.yml");
        if (!file.exists()) {
            config.set("username", username);
            config.set("token", token);
            config.set("authNecessarily", authNecessarily);
            try {
                config.save(file);
            } catch (Exception e) {
                System.out.println("Error creating config file: " + e);
            }
        } else {
            try {
                config.load(file);
            } catch (IOException e) {
                System.out.println("Error loading config file: " + e);
            } catch (InvalidConfigurationException e) {
                System.out.println("Error loading config file: " + e);
            }
            username = config.getString("username");
            token = config.getString("token");
            authNecessarily = config.getBoolean("authNecessarily");
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            User.setUserMessage(update);
            if (update.getMessage().getText().toString().startsWith("/")) {
                if (update.getMessage().getText().toString().startsWith("/find")) {
                    String[] args = update.getMessage().getText().toString().split(" ");
                    if (args.length == 2) {
                        YamlConfiguration config = User.findPlayerTG(args[1]);
                        if (config != null) {
                            this.sendMessage(update.getMessage().getChatId(), "[Бот] Найдено по вашему запросу: \nНикнейм: " + config.getString("playername") + "\nUserName: " + config.getString("username") + "\nFirstname: " + config.getString("firstname"));
                        } else {
                            this.sendMessage(update.getMessage().getChatId(), "[Бот] Не найдено ничего по запросу " + args[1]);
                        }
                    } else {
                        this.sendMessage(update.getMessage().getChatId(), "[Бот] Команда введена неверно. Введите /find [никнейм]");
                    }
                }
                if (update.getMessage().getText().toString().equals("/accounts")) {
                    this.chosePlayer(update.getMessage().getChatId());
                }
                this.deleteMessage(update.getMessage());
            } else {
                if (!bedrockPlayer.isEmpty()) {
                    for (UUID map : bedrockPlayer.keySet()) {
                        if (update.getMessage().getText().toString().equals(bedrockPlayer.get(map))) {
                            User.register(update.getMessage(),map);
                            this.sendMessage(update.getMessage().getChatId() ,AuthTGEM.messageTG.get("code_account_activated_auth"));
                        }
                    }
                }
            }
        }
        if (update.hasCallbackQuery()) {
            User.setUserCallback(update);
            if (update.getCallbackQuery().getData().toString().startsWith("acc")) {
                String playername = update.getCallbackQuery().getData().toString().replace("acc", "");
                curentplayer.put(update.getCallbackQuery().getMessage().getChatId().toString(), playername);
                this.sendMessage(update.getCallbackQuery().getMessage().getChatId(), AuthTGEM.messageTG.getAccChoosePN(playername));
            }
        }
    }

    public void sendMessage(Long Chatid, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Chatid);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message: " + e);
        }
    }

    public void deleteMessage(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId());
        deleteMessage.setMessageId(message.getMessageId());
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error deleting message: " + e);
        }
    }

    public void chosePlayer(Long chatID) {
        InlineKeyboardMarkup players = new InlineKeyboardMarkup();
        List<String> playernames = User.getUserList(chatID);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String name : playernames) {
            List<InlineKeyboardButton> colkeyb = new ArrayList<>();
            InlineKeyboardButton playerbtn = new InlineKeyboardButton();
            playerbtn.setText(name);
            playerbtn.setCallbackData("acc" + name);
            colkeyb.add(playerbtn);
            keyboard.add(colkeyb);
        }
        players.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(AuthTGEM.messageTG.get("account_choose"));
        sendMessage.setReplyMarkup(players);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message: " + e);
        }
    }
}