package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class BotTelegram extends TelegramLongPollingBot {
    private String username;
    private String token;
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
            AuthTGEM.connector.setUserMessage(update);
            if (update.getMessage().getText().toString().startsWith("/")) {
                if (update.getMessage().getText().toString().startsWith("/find")) {
                    String[] args = update.getMessage().getText().toString().split(" ");
                    if (args.length == 2) {
                        User user = AuthTGEM.connector.findUser(args[1]);
                        if (user != null) {
                            this.sendMessage(update.getMessage().getChatId(), AuthTGEM.config.getString("messages.telegram.findfound").replace("{PLAYERNAME}", user.playername).replace("{USERNAME}", user.username).replace("{FIRSTNAME}", user.firstname).replace("{BR}", "\n"));
                        } else {
                            this.sendMessage(update.getMessage().getChatId(), AuthTGEM.config.getString("messages.telegram.findnotfound").replace("{ARG}", args[1]));
                        }
                    } else {
                        this.sendMessage(update.getMessage().getChatId(), AuthTGEM.config.getString("messages.telegram.findusage"));
                    }
                }
                if (update.getMessage().getText().toString().equals("/unlink")) {
                    UUID uuid  = AuthTGEM.connector.getCurrentUUID(update.getMessage().getChatId());
                    User user = User.getUser(uuid);
                    this.sendMessage(update.getMessage().getChatId(), AuthTGEM.config.getString("messages.telegram.unlinked").replace("{PLAYER}", user.playername));
                    if (AuthTGEM.config.getBoolean("authNecessarily")) {
                        if (user.player != null) {
                            Handler.kick(user.playername, AuthTGEM.config.getString("messages.telegram.kickunlink"));
                        }
                    }
                    AuthTGEM.connector.deletePlayer(uuid);
                }
                if (update.getMessage().getText().toString().equals("/accounts")) {
                    this.chosePlayer(update.getMessage().getChatId());
                }
                this.deleteMessage(update.getMessage());
            } else {
                if (!bedrockPlayer.isEmpty()) {
                    for (UUID uuid : bedrockPlayer.keySet()) {
                        if (update.getMessage().getText().toString().equals(bedrockPlayer.get(uuid))) {
                            AuthTGEM.connector.setActive(uuid, true);
                            AuthTGEM.connector.setChatID(uuid, update.getMessage().getChatId());
                            AuthTGEM.connector.setCurrentUUID(uuid, update.getMessage().getChatId());
                            AuthTGEM.connector.setFirstName(uuid, update.getMessage().getFrom().getFirstName());
                            AuthTGEM.connector.setLastName(uuid, update.getMessage().getFrom().getLastName());
                            AuthTGEM.connector.setUsername(uuid, update.getMessage().getFrom().getUserName());
                            this.sendMessage(update.getMessage().getChatId() ,AuthTGEM.config.getString("messages.telegram.linked"));
                        }
                    }
                }
            }
        }
        if (update.hasCallbackQuery()) {
            AuthTGEM.connector.setUserCallback(update);
            if (update.getCallbackQuery().getData().toString().startsWith("acc")) {
                String playername = update.getCallbackQuery().getData().toString().replace("acc", "");
                UUID uuid = AuthTGEM.connector.getUUID(playername);
                AuthTGEM.connector.setCurrentUUID(uuid, update.getCallbackQuery().getMessage().getChatId());
                this.sendMessage(update.getCallbackQuery().getMessage().getChatId(), AuthTGEM.config.getString("messages.telegram.accset").replace("{PLAYER}", playername));
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
        List<String> playernames = AuthTGEM.connector.getPlayerNames(chatID);
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
        sendMessage.setText(AuthTGEM.config.getString("messages.telegram.accchose"));
        sendMessage.setReplyMarkup(players);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message: " + e);
        }
    }
}