package com.game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:gameDB.db";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase(){
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute("CREATE TABLE IF NOT EXISTS player (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "playerLogin TEXT UNIQUE NOT NULL," +
                    "playerPassword TEXT NOT NULL," +
                    "playerHp INTEGER DEFAULT 100," +
                    "playerDmg INTEGER DEFAULT 10," +
                    "playerMoney INTEGER DEFAULT 20," +
                    "playerLom INTEGER DEFAULT 0," +
                    "playerHpPotion INTEGER DEFAULT 0," +
                    "playerIsNew TEXT DEFAULT \"true\")");

            stmt.execute("CREATE TABLE IF NOT EXISTS gameParameters (" +
                    "player TEXT," +
                    "mobName INTEGER," +
                    "mobHp INTEGER," +
                    "mobDmg INTEGER," +
                    "lomCost INTEGER," +
                    "hpPotionCost INTEGER," +
                    "playerLogs TEXT," +
                    "gameLogs TEXT," +
                    "currentAction TEXT DEFAULT \"mainPaneQuest1\")");


        }


        catch (SQLException e) {
            System.err.println("База данных не создана");
            e.printStackTrace();
        }
    }
}
