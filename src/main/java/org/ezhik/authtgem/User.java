package org.ezhik.authtgem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class User {
    public Long chatid;
    public String username;
    public String firstname;
    public String lastname;
    public boolean active;
    public Player player;
    public  UUID uuid;
    public String playername;

    private User(UUID uuid) {
        this.uuid = uuid;
        this.chatid = AuthTGEM.connector.getChatID(uuid);
        this.active = AuthTGEM.connector.isActive(uuid);
        this.playername = AuthTGEM.connector.getPlayerName(uuid);
        this.player = Bukkit.getPlayer(uuid);
        this.username = AuthTGEM.connector.getUsername(uuid);
        this.firstname = AuthTGEM.connector.getFirstName(uuid);
        this.lastname = AuthTGEM.connector.getLastName(uuid);
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

    public static User getUser(UUID uuid) {
        User user = new User(uuid);
        if (user.active) {
            return user;
        }
        else return null;
    }
}