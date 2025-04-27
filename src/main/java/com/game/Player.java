package com.game;

import java.sql.*;
import java.util.HashMap;

public class Player {
    private final int id;
    private final String name;
    private int hp;
    private int dmg;
    private int money;
    private final HashMap<String, Integer> inventory = new HashMap<>();
    private String isNew;

    public Player(int id, String name, int hp, int dmg, int money, int lom, int hpPotion, String isNew) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
        this.money = money;
        this.inventory.put("lom", lom);
        this.inventory.put("hpPotion", hpPotion);
        this.isNew = isNew;
    }

    public static Player load(String login) throws SQLException {
        String sql = "SELECT * FROM player WHERE playerLogin = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Player(
                        rs.getInt("Id"),
                        rs.getString("playerLogin"),
                        rs.getInt("playerHp"),
                        rs.getInt("playerDmg"),
                        rs.getInt("playerMoney"),
                        rs.getInt("playerLom"),
                        rs.getInt("playerHpPotion"),
                        rs.getString("playerIsNew")
                );
            }
            return null;
        }
    }

    public void save() throws SQLException {
        String sql = "UPDATE player SET " +
                "playerHp = ?, playerDmg = ?, playerMoney = ?, " +
                "playerLom = ?, playerHpPotion = ?, playerIsNew = ? " +
                "WHERE Id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.hp);
            pstmt.setInt(2, this.dmg);
            pstmt.setInt(3, this.money);
            pstmt.setInt(4, this.inventory.getOrDefault("lom", 0));
            pstmt.setInt(5, this.inventory.getOrDefault("hpPotion", 0));
            pstmt.setString(6, this.isNew);
            pstmt.setInt(7, this.id);
            pstmt.executeUpdate();
        }
    }

    public static boolean register(String login, String password) throws SQLException {
        String sql = "INSERT INTO player(playerLogin, playerPassword) VALUES(?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password); // Пароль хранится в открытом виде
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                return false; // Логин занят
            }
            throw e;
        }
    }

    public static boolean checkPassword(String login, String password) throws SQLException {

        String sql = "SELECT playerPassword FROM player WHERE playerLogin = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("playerPassword");
                return dbPassword.equals(password);
            }
            return false;
        }
    }

    public void invUpdate() {
        if (Actions.getLomQuantityLbl() != null) {
            Actions.getLomQuantityLbl().setText(String.valueOf(this.inventory.getOrDefault("lom", 0)));
        }
        if (Actions.getHpPotionQuantityLbl() != null) {
            Actions.getHpPotionQuantityLbl().setText(String.valueOf(this.inventory.getOrDefault("hpPotion", 0)));
        }
    }

    // Геттеры
    public String getName() { return name; }
    public int getId() { return id; }
    public int getHp() { return hp; }
    public int getDmg() { return dmg; }
    public int getMoney() { return money; }
    public HashMap<String, Integer> getInventory() { return inventory; }
    public int getInventoryValue(String key) { return inventory.getOrDefault(key, 0); }
    public String getIsNew() { return isNew; }

    // Сеттеры
    public void setHp(int hp) { this.hp = hp; }
    public void setDmg(int dmg) { this.dmg = dmg; }
    public void setMoney(int money) {
        this.money = money;
    }
    public void setInventory(String item, int value) {
        inventory.put(item, inventory.getOrDefault(item, 0) + value);
        invUpdate();
    }
    public void setIsNew(String isNew){
        this.isNew = isNew;
    }
}