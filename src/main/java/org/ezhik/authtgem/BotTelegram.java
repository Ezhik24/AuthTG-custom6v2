package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ezhik.authtgem.events.FreezerEvent;
import org.ezhik.authtgem.events.MuterEvent;
import org.geysermc.api.Geyser;
import org.geysermc.api.GeyserApiBase;
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

import static org.ezhik.authtgem.AuthTGEM.bot;

public class BotTelegram extends TelegramLongPollingBot {
    private String username = "changeme";
    private String token = "changeme";
    public static Map<String, String> curentplayer = new HashMap<>();
    public static Map<UUID,String> bedrockPlayer = new HashMap<>();
    public boolean authNecessarily = false;
    public Long adminChatID = 0L;
    public Integer threadChatID = 0;

    public BotTelegram() {
        YamlConfiguration config = new YamlConfiguration();
        File file = new File("plugins/AuthTG/config.yml");
        if (!file.exists()) {
            config.set("username", username);
            config.set("token", token);
            config.set("authNecessarily", authNecessarily);
            config.set("adminChatID", adminChatID);
            config.set("threadChatID", threadChatID);
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
            adminChatID = config.getLong("adminChatID");
            threadChatID = config.getInt("threadChatID");
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
            if (update.getMessage().getText().toString().startsWith("/")) {
                if (update.getMessage().getText().toString().startsWith("/find")) {
                    String[] args = update.getMessage().getText().toString().split(" ");
                    if (args.length == 2) {
                        String username = User.findPlayerTG(args[1]);
                        if (username != null) {
                            this.sendMessage(update.getMessage().getChatId(), "[Бот] Найден ТГ аккаунт по нику " + args[1] + ": @" + username);
                        }
                    } else {
                        this.sendMessage(update.getMessage().getChatId(), "[Бот] Команда введена неверно. Введите /find [никнейм]");
                    }
                }
                if (update.getMessage().getText().toString().equals("/kickme")) {
                    User user = User.getOnlineUser(update.getMessage().getChatId());
                    if (user != null) {
                        user.kick();
                        user.sendMessage(AuthTGEM.messageTG.get("kickme_kick_succes"));
                    } else
                        this.sendMessage(update.getMessage().getChatId(), AuthTGEM.messageTG.get("kickme_player_notfound"));
                }
                if (update.getMessage().getText().toString().equals("/accounts")) {
                    this.chosePlayer(update.getMessage().getChatId());
                }
                this.deleteMessage(update.getMessage());
            } else {
                if (!bedrockPlayer.isEmpty()) {
                    for (Map.Entry<UUID,String> map : bedrockPlayer.entrySet()) {
                        UUID uuid = map.getKey();
                        String key = map.getValue();
                        if (update.getMessage().getText().toString().equals(key)) {
                            User.register(update.getMessage(),uuid);
                            this.sendMessage(update.getMessage().getChatId() ,AuthTGEM.messageTG.get("code_account_activated_auth"));
                        }
                    }
                }

            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().toString().startsWith("ys")) {
                String playername = update.getCallbackQuery().getData().toString().replace("ys", "");
                FreezerEvent.unfreezeplayer(playername);
                MuterEvent.unmute(playername);
                this.deleteMessage(update.getCallbackQuery().getMessage());
                Player player = Bukkit.getPlayer(playername);
                player.resetTitle();
                File file = new File("plugins/AuthTG/users/" + player.getUniqueId() + ".yml");
                YamlConfiguration userconfig = YamlConfiguration.loadConfiguration(file);
                if (update.getMessage() != null && !userconfig.getString("username").equals(update.getMessage().getChat().getUserName())) {
                    userconfig.set("username", update.getMessage().getChat().getUserName());
                }
                try {
                    userconfig.save(file);
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("succes_login_account")));
            }
            if (update.getCallbackQuery().getData().toString().startsWith("no")) {
                String playername = update.getCallbackQuery().getData().toString().replace("no", "");
                Handler.kick(playername, ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("rejected_login_account")));
                this.deleteMessage(update.getCallbackQuery().getMessage());
            }

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
    public void sendMessageThread(Long chatid,Integer thread ,String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(message);
        sendMessage.setChatId(chatid);
        sendMessage.setMessageThreadId(thread);
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
        sendMessage.setText(AuthTGEM.messageTG.get("account_choose"));
        sendMessage.setReplyMarkup(players);
        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message: " + e);
        }
    }
}