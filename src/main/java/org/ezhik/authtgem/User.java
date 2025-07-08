package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
    public UUID currentuuid;

    private User(UUID uuid) {
        try {
            this.uuid = uuid;
            this.chatid = AuthTGEM.connector.getChatID(uuid);
            this.active = AuthTGEM.connector.isActive(uuid);
            this.playername = AuthTGEM.connector.getPlayerName(uuid);
            this.player = Bukkit.getPlayer(uuid);
            this.username = AuthTGEM.connector.getUsername(uuid);
            this.firstname = AuthTGEM.connector.getFirstName(uuid);
            this.lastname = AuthTGEM.connector.getLastName(uuid);
            this.currentuuid = AuthTGEM.connector.getCurrentUUID(chatid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}