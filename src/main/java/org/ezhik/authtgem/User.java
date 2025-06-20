package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.ezhik.authtgem.events.FreezerEvent;
import org.ezhik.authtgem.events.MuterEvent;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class User {
    public Long chatid = null;
    public String username = null;
    public String firstname = null;
    public String lastname = null;
    public boolean active = false;
    public boolean twofactor = false;
    public Player player = null;
    public  UUID uuid = null;
    public String playername = "";
    public List<String> friends = new ArrayList<>();

    private User(UUID uuid) {
        YamlConfiguration userconfig = new YamlConfiguration();
        File file = new File("plugins/AuthTG/users/" + uuid + ".yml");
        try {
            userconfig.load(file);
            this.uuid = uuid;
            this.playername = userconfig.getString("playername");
            if(playername == null) playername = "";
            this.chatid = userconfig.getLong("ChatID");
            this.username = userconfig.getString("username");
            this.firstname = userconfig.getString("firstname");
            this.lastname = userconfig.getString("lastname");
            this.twofactor = userconfig.getBoolean("twofactor");
            this.player = Bukkit.getPlayer(uuid);
            this.active = userconfig.getBoolean("active");
            this.friends = userconfig.getStringList("friends");
        } catch (FileNotFoundException e) {
            System.out.println("Error file not found: " + e);
        } catch (IOException e) {
            System.out.println("Error loading config file: " + e);
        } catch (InvalidConfigurationException e) {
            System.out.println("Error loading config file: " + e);
        }
    }

    public static String generateConfirmationCode() {
        Random random = new Random();
        String characters = "0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }
        return code.toString();
    }

    public static void register(Message message, UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        YamlConfiguration userconfig = new YamlConfiguration();
        File file = new File("plugins/AuthTG/users/" + uuid + ".yml");
        try {
            userconfig.load(file);
        } catch (IOException e) {
            System.out.println("Error loading config file: " + e);
        } catch (InvalidConfigurationException e) {
            System.out.println("Error parsing config file: " + e);
        }
        userconfig.set("ChatID", message.getChatId());
        userconfig.set("username", message.getChat().getUserName());
        userconfig.set("firstname", message.getChat().getFirstName());
        userconfig.set("lastname", message.getChat().getLastName());
        userconfig.set("active", true);
        userconfig.set("twofactor", false);
        try {
            userconfig.save(file);
        } catch (IOException e) {
            System.out.println("Error saving config file: " + e);
        }
        FreezerEvent.unfreezeplayer(p.getName());
        MuterEvent.unmute(p.getName());
        p.resetTitle();
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', AuthTGEM.messageMC.get("code_account_activated")));
    }

    public void sendMessage(String message) {
        AuthTGEM.bot.sendMessage(this.chatid, AuthTGEM.messageTG.getPlayerNameSM(this) + message);
    }

    public static List<User> getUserList(){
        List<User> users = new ArrayList<>();
        File folder = new File("plugins/AuthTG/users");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
                User user = new User(uuid);
                if (user.active) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    public void sendLoginAccepted(String message) {
        InlineKeyboardMarkup keyb = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> colkeyb = new ArrayList<>();
        InlineKeyboardButton yesbtn = new InlineKeyboardButton();
        yesbtn.setText(AuthTGEM.messageTG.get("login_intg_yes"));
        yesbtn.setCallbackData("ys"+this.player.getName());
        InlineKeyboardButton nobtn = new InlineKeyboardButton();
        nobtn.setText(AuthTGEM.messageTG.get("login_intg_not"));
        nobtn.setCallbackData("no"+this.player.getName());
        colkeyb.add(yesbtn);
        colkeyb.add(nobtn);
        List<List<InlineKeyboardButton>>keyboard = new ArrayList<>();
        keyboard.add(colkeyb);
        keyb.setKeyboard(keyboard);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(this.chatid);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(keyb);
        try {
            AuthTGEM.bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Error sending message: " + e);
        }

    }

    public static User getUser(UUID uuid) {
        User user = new User(uuid);
        if (user.active) {
            return user;
        }
        else return null;
    }

    public void kick(){
        Handler.kick(this.player.getName(), AuthTGEM.messageTG.get("kick_account_inTG"));
    }

    public static void sendBroadcastMessage(String message) {
        for (User user : User.getUniclUsers()) {
            AuthTGEM.bot.sendMessage(user.chatid, AuthTGEM.messageTG.get("sendMessageBroadCast") + message);
        }
    }

    public static List<String> getPlayerNames(Long chatid) {
        List<String> names = new ArrayList<>();
        for (User user : User.getUserList()) {
            if (user != null)
                if (user.chatid.equals(chatid)) {
                    names.add(user.playername);
                }
        }
        return names;
    }

    public static User getOnlineUser(Long chatid) {
        if (BotTelegram.curentplayer.containsKey(chatid.toString())) {
            Player player = Bukkit.getPlayer(BotTelegram.curentplayer.get(chatid.toString()));
            if (player != null) {
                return User.getUser(player.getUniqueId());
            }
        }
        List<String> players = getPlayerNames(chatid);
        for (User user : getUserList()){
           if(user.player != null){
               if (players.contains(user.player.getName())){
                   BotTelegram.curentplayer.put(chatid.toString(), user.playername);
                   return user;
               }
           }
        }
        return null;
    }

    public static List<User> getUniclUsers() {
        List<User> users = new ArrayList<>();
        List<Long> chatIds = new ArrayList<>();
        for (User user : User.getUserList()) {
            if (user != null) {
                if (!chatIds.contains(user.chatid)) {
                    chatIds.add(user.chatid);
                    users.add(user);
                }
            }
        }
        return users;
    }
    public static String findPlayerTG(String playername) {
        File[] files = new File("plugins/AuthTG/users/").listFiles();
        for (File file : files) {
            YamlConfiguration userconf = new YamlConfiguration();
            try {
                userconf.load(file);
            } catch (IOException e) {
                System.out.println("Error loading config file: " + e);
            } catch (InvalidConfigurationException e) {
                System.out.println("Error parsing config file: " + e);
            }
            if (userconf.getString("playername").equals(playername)) {
                return userconf.get("username").toString();
            }
        }
        return null;
    }
}