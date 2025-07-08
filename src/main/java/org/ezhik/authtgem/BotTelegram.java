package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.sql.SQLException;

import java.io.File;
import java.util.*;

public class BotTelegram extends TelegramLongPollingBot {
    private String username;
    private String token;
    public static Map<String, String> curentplayer = new HashMap<>();
    public static Map<UUID,String> bedrockPlayer = new HashMap<>();

    public BotTelegram(String username, String token) {
        this.username = username;
        this.token = token;
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
                if (update.getMessage().getText().toString().equals("/unlink")) {
                    User user = User.getCurrentUser(update.getMessage().getChatId());
                    File file = new File("plugins/AuthTG/" + user.uuid + ".yml");
                    file.delete();
                    this.sendMessage(update.getMessage().getChatId(), "[Бот] Аккаунт " + user.playername + " успешно удален!");
                    if (AuthTGEM.config.getBoolean("authNecessarily")) {
                        if (user.player != null) {
                            Handler.kick(user.playername, "Вы отвязали аккаунт от телеграма.");
                        }
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
                            OfflinePlayer player = Bukkit.getOfflinePlayer(map);
                            try {
                                AuthTGEM.connector.setPlayerName(map, player.getName());
                                AuthTGEM.connector.setActive(map, true);
                                AuthTGEM.connector.setChatID(map, update.getMessage().getChatId());
                                AuthTGEM.connector.setCurrentUUID(map, true);
                                AuthTGEM.connector.setFirstName(map, update.getMessage().getFrom().getFirstName());
                                AuthTGEM.connector.setLastName(map, update.getMessage().getFrom().getLastName());
                                AuthTGEM.connector.setUsername(map, update.getMessage().getFrom().getUserName());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            this.sendMessage(update.getMessage().getChatId() ,"[Бот] Вы успешно привязали аккаунт!");
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
                this.sendMessage(update.getCallbackQuery().getMessage().getChatId(), "[Бот] Выбран аккаунт: " + playername);
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
        List<String> playernames = User.getPlayerNames(chatID);
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
        sendMessage.setText("[Бот] Выберите аккаунт:");
        sendMessage.setReplyMarkup(players);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message: " + e);
        }
    }
}