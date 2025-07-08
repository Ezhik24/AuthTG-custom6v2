package org.ezhik.authtgem;

import java.sql.*;
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

    public void setPlayerName(UUID uuid, String name) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        if (!rs.next()) {
            stmt.executeUpdate(
                    "INSERT INTO AuthTGUsers (uuid, playername) VALUES ('" + uuid.toString() + "', '" + name + "')"
            );
        } else {
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET playername = '" + name + "' WHERE uuid = '" + uuid.toString() + "'"
            );
        }
        conn.close();
    }
    public void setActive(UUID uuid, boolean active) throws SQLException {
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
    }
    public void setChatID(UUID uuid, long chatid) throws SQLException {
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
    }
    public void setUsername(UUID uuid, String username) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        stmt.executeUpdate(
                "UPDATE AuthTGUsers SET username = '" + username + "' WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
    }
    public void setCurrentUUID(UUID uuid, boolean currentUUID) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        stmt.executeUpdate(
                "UPDATE AuthTGUsers SET currentUUID = " + currentUUID + " WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
    }
    public void setBypass(UUID uuid, boolean bypass) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        if (!rs.next()) {
            stmt.executeUpdate(
                    "INSERT INTO AuthTGUsers (uuid, bypass) VALUES ('" + uuid.toString() + "', " + bypass + ")"
            );
        } else {
            stmt.executeUpdate(
                    "UPDATE AuthTGUsers SET bypass = " + bypass + " WHERE uuid = '" + uuid.toString() + "'"
            );
        }
        conn.close();
    }
    public void setFirstName(UUID uuid, String firstname) throws SQLException {
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
    }
    public void setLastName(UUID uuid, String lastname) throws SQLException {
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
    }

    public String getPlayerName(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT playername FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getString("playername");
    }
    public boolean isActive(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT active FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getBoolean("active");
    }
    public long getChatID(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT chatid FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getLong("chatid");
    }
    public String getUsername(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT username FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getString("username");
    }
    public UUID getCurrentUUID(Long chatid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT uuid FROM AuthTGUsers WHERE chatid = '" + chatid + "'"
        );
        conn.close();
        return UUID.fromString(rs.getString("uuid"));
    }
    public boolean isBypass(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT bypass FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getBoolean("bypass");
    }
    public String getFirstName(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT firstname FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getString("firstname");
    }
    public String getLastName(UUID uuid) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT lastname FROM AuthTGUsers WHERE uuid = '" + uuid.toString() + "'"
        );
        conn.close();
        return rs.getString("lastname");
    }
    public UUID getUUID(String playername) throws SQLException {
        conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + database,
                username,
                password
        );
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT uuid FROM AuthTGUsers WHERE playername = '" + playername + "'"
        );
        conn.close();
        return UUID.fromString(rs.getString("uuid"));
    }
}
