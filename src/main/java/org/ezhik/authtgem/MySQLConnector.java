package org.ezhik.authtgem;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLConnector {
    Connection conn;
    Statement stmt;
    String host;
    String database;
    String username;
    String password;
    public MySQLConnector(String host, String database, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        stmt = conn.createStatement();
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS AuthTGUsers ("
            + "priKey INT NOT NULL AUTO_INCREMENT,"
            + "uuid varchar(36) NOT NULL,"
            + "playername varchar(120) NOT NULL,"
            + "active BOOLEAN NOT NULL DEFAULT false,"
            + "chatid BIGINT,"
            + "username varchar(32),"
            + "firstname varchar(120),"
            + "lastname varchar(120),"
            + "currentUUID BOOLEAN NOT NULL DEFAULT false,"
            + "bypass BOOLEAN NOT NULL DEFAULT false,"
            + "UNIQUE (uuid),"
            + "PRIMARY KEY (priKey))"
        );
        conn.close();
    }

    public void setPlayerName(UUID uuid, String name){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "INSERT IGNORE INTO AuthTGUsers (uuid, playername) VALUES ('" + uuid.toString() + "', '" + name + "')"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setPlayername: " + e.getMessage());
        }
    }
    public void setActive(UUID uuid, boolean active) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET active = " + active + " WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setActive: " + e.getMessage());
        }
    }
    public void setChatID(UUID uuid, long chatid) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET chatid = " + chatid + " WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setChatID: " + e.getMessage());
        }
    }
    public void setUsername(UUID uuid, String user)  {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET username = '" + user + "' WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setUsername: " + e.getMessage());
        }
    }
    public void setCurrentUUID(UUID uuid, Long chatid){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET currentUUID = false WHERE chatid = " + chatid
            );
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET currentUUID = true WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setCurrentUUID: " + e.getMessage());
        }
    }
    public void setBypass(String playername, boolean bypass) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET bypass = " + bypass + " WHERE playername = '" + playername + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setBypass: " + e.getMessage());
        }
    }
    public void setFirstName(UUID uuid, String firstname) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET firstname = '" + firstname + "' WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setFirstName: " + e.getMessage());
        }
    }
    public void setLastName(UUID uuid, String lastname){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET lastname = '" + lastname + "' WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("setLastName: " + e.getMessage());
        }
    }

    public String getPlayerName(UUID uuid){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT playername FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) {
                return rs.getString("playername");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getPlayerName: " + e.getMessage());
        }
        return null;
    }
    public boolean isActive(UUID uuid){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT active FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) {
                return rs.getBoolean("active");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("isActive: " + e.getMessage());
        }
        return false;
    }
    public long getChatID(UUID uuid) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT chatid FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) {
                return rs.getLong("chatid");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getChatID: " + e.getMessage());
        }
        return 0;
    }
    public String getUsername(UUID uuid) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT username FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) {
                return rs.getString("username");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getUsername: " + e.getMessage());
        }
        return null;
    }
    public UUID getCurrentUUID(Long chatid){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM AuthTGUsers WHERE chatid = '" + chatid + "'"
            );
            if (rs.next()) {
                if (rs.getBoolean("currentUUID")) {
                    return UUID.fromString(rs.getString("uuid"));
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getCurrentUUID: " + e.getMessage());
        }
        return null;
    }
    public boolean isBypass(UUID uuid){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT bypass FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) return rs.getBoolean("bypass");
            conn.close();
        } catch (SQLException e) {
            System.out.println("isBypass: " + e.getMessage());
        }
        return false;
    }
    public String getFirstName(UUID uuid)  {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT firstname FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) {
                return rs.getString("firstname");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getFirstName: " + e.getMessage());
        }
        return null;
    }
    public String getLastName(UUID uuid){
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT lastname FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            if (rs.next()) {
                return rs.getString("lastname");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getLastName: " + e.getMessage());
        }
        return null;
    }
    public UUID getUUID(String playername) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT uuid FROM AuthTGUsers WHERE playername = '" + playername + "'"
            );
            if (rs.next()) {
                return UUID.fromString(rs.getString("uuid"));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("getUUID: " + e.getMessage());
        }
        return null;
    }

    public List<String> getPlayerNames(Long chatid) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT playername FROM AuthTGUsers WHERE chatid = '" + chatid + "'"
            );
            List<String> playernames = new ArrayList<>();
            while (rs.next()) {
                playernames.add(rs.getString("playername"));
            }
            conn.close();
            return playernames;
        } catch (SQLException e) {
            System.out.println("getPlayerNames: " + e.getMessage());
        }
        return null;
    }

    public void deletePlayer(UUID uuid) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            stmt.executeUpdate(
                    "DELETE FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
            );
            conn.close();
        } catch (SQLException e) {
            System.out.println("deletePlayer: " + e.getMessage());
        }
    }

    public User findUser(String arg) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            arg = arg.replace("@", "");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM AuthTGUsers WHERE firstname = '" + arg + "' OR username = '" + arg + "' OR playername = '" + arg + "'"
            );
            if (rs.next()) {
                return User.getUser(UUID.fromString(rs.getString("uuid")));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("findUser: " + e.getMessage());
        }
        return null;
    }

    public void setUserMessage(Update update) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM AuthTGUsers WHERE chatid = " + update.getMessage().getChatId()
            );
            if (rs.next()) {
                if (!rs.getString("username").equals(update.getMessage().getFrom().getUserName())) {
                    stmt.executeUpdate(
                            "UPDATE AuthTGUsers SET username = '" + update.getMessage().getFrom().getUserName() + "' WHERE chatid = " + update.getMessage().getChatId()
                    );
                }
                if (!rs.getString("firstname").equals(update.getMessage().getFrom().getFirstName())) {
                    stmt.executeUpdate(
                            "UPDATE AuthTGUsers SET firstname = '" + update.getMessage().getFrom().getFirstName() + "' WHERE chatid = " + update.getMessage().getChatId()
                    );
                }
                if (!rs.getString("lastname").equals(update.getMessage().getFrom().getLastName())) {
                    stmt.executeUpdate(
                            "UPDATE AuthTGUsers SET lastname = '" + update.getMessage().getFrom().getLastName() + "' WHERE chatid = " + update.getMessage().getChatId()
                    );
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("setUserMessage: " + e.getMessage());
        }
    }

    public void setUserCallback(Update update) {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + host + "/" + database,
                    username,
                    password
            );
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM AuthTGUsers WHERE chatid = " + update.getCallbackQuery().getMessage().getChatId()
            );
            if (rs.next()) {
                if (!rs.getString("username").equals(update.getCallbackQuery().getFrom().getUserName())) {
                    stmt.executeUpdate(
                            "UPDATE AuthTGUsers SET username = '" + update.getCallbackQuery().getFrom().getUserName() + "' WHERE chatid = " + update.getCallbackQuery().getMessage().getChatId()
                    );
                }
                if (!rs.getString("firstname").equals(update.getCallbackQuery().getFrom().getFirstName())) {
                    stmt.executeUpdate(
                            "UPDATE AuthTGUsers SET firstname = '" + update.getCallbackQuery().getFrom().getFirstName() + "' WHERE chatid = " + update.getCallbackQuery().getMessage().getChatId()
                    );
                }
                if (!rs.getString("lastname").equals(update.getCallbackQuery().getFrom().getLastName())) {
                    stmt.executeUpdate(
                            "UPDATE AuthTGUsers SET lastname = '" + update.getCallbackQuery().getFrom().getLastName() + "' WHERE chatid = " + update.getCallbackQuery().getMessage().getChatId()
                    );
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("setUserCallback: " + e.getMessage());
        }
    }
}
