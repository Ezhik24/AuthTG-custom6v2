package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


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
    public Player player = null;
    public  UUID uuid = null;
    public String playername = "";

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
            this.player = Bukkit.getPlayer(uuid);
            this.active = userconfig.getBoolean("active");
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
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        File file = new File("plugins/AuthTG/users/" + uuid + ".yml");
        YamlConfiguration userconfig = YamlConfiguration.loadConfiguration(file);
        userconfig.set("playername", player.getName());
        userconfig.set("ChatID", message.getChatId());
        userconfig.set("username", message.getChat().getUserName());
        userconfig.set("firstname", message.getChat().getFirstName());
        userconfig.set("lastname", message.getChat().getLastName());
        userconfig.set("active", true);
        try {
            userconfig.save(file);
        } catch (IOException e) {
            System.out.println("Error saving config file: " + e);
        }
    }

    public static List<User> getUserList(){
        List<User> users = new ArrayList<>();
        File folder = new File("plugins/Minetelegram/users");
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

    public static User getUser(UUID uuid) {
        User user = new User(uuid);
        if (user.active) {
            return user;
        }
        else return null;
    }

    public static YamlConfiguration findPlayerTG(String arg) {
        File[] files = new File("plugins/AuthTG/users/").listFiles();
        for (File file : files) {
            YamlConfiguration userconf = YamlConfiguration.loadConfiguration(file);
            if (userconf.getString("playername").equals(arg)) return userconf;
            if (userconf.getString("username").equals(arg)) return userconf;
            if (userconf.getString("firstname").equals(arg)) return userconf;
        }
        return null;
    }

    public static void setUserMessage(Update update) {
        File[] files = new File("plugins/AuthTG/users/").listFiles();
        for (File file : files) {
            YamlConfiguration userconf = YamlConfiguration.loadConfiguration(file);
            if (update.getMessage().getChatId().equals(userconf.getLong("ChatID"))) {
                if (update.getMessage().getFrom().getUserName() != null && update.getMessage().getFrom().getLastName() != null && update.getMessage().getFrom().getFirstName() != null) {
                    if (!update.getMessage().getFrom().getUserName().equals(userconf.getString("username")))
                        userconf.set("username", update.getMessage().getFrom().getUserName());
                    if (!update.getMessage().getFrom().getFirstName().equals(userconf.getString("firstname")))
                        userconf.set("firstname", update.getMessage().getFrom().getFirstName());
                    if (!update.getMessage().getFrom().getLastName().equals(userconf.getString("lastname")))
                        userconf.set("lastname", update.getMessage().getFrom().getLastName());
                }
                try {
                    userconf.save(file);
                } catch (IOException e) {
                    System.out.println("Error saving config file: " + e);
                }
            }
        }
    }
    public static void setUserCallback(Update update) {
        File[] files = new File("plugins/AuthTG/users/").listFiles();
        for (File file : files) {
            YamlConfiguration userconf = YamlConfiguration.loadConfiguration(file);
            if (update.getCallbackQuery().getMessage().getChatId().equals(userconf.getLong("ChatID"))) {
                if (update.getMessage().getFrom().getUserName() != null && update.getMessage().getFrom().getLastName() != null && update.getMessage().getFrom().getFirstName() != null) {
                    if (!update.getCallbackQuery().getMessage().getFrom().getUserName().equals(userconf.getString("username")))
                        userconf.set("username", update.getCallbackQuery().getMessage().getFrom().getUserName());
                    if (!update.getCallbackQuery().getMessage().getFrom().getFirstName().equals(userconf.getString("firstname")))
                        userconf.set("firstname", update.getCallbackQuery().getMessage().getFrom().getFirstName());
                    if (!update.getCallbackQuery().getMessage().getFrom().getLastName().equals(userconf.getString("lastname")))
                        userconf.set("lastname", update.getCallbackQuery().getMessage().getFrom().getLastName());
                }
                try {
                    userconf.save(file);
                } catch (IOException e) {
                    System.out.println("Error saving config file: " + e);
                }
            }
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

    public static User getCurrentUser(Long chatid){
        if (BotTelegram.curentplayer.containsKey(chatid.toString())) {
            return User.getUser(UUID.fromString(BotTelegram.curentplayer.get(chatid.toString())));
        } else {
            for (User user : User.getUserList()) {
                if (user.chatid.equals(chatid)) {
                    BotTelegram.curentplayer.put(chatid.toString(), user.playername);
                    return user;
                }
            }
            return null;
        }
    }
}