package com.game;

import com.game.controllers.MainWindowController;
import com.game.controllers.RegLogController;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Actions {
    private static MainWindowController controller;
    private static List<AnchorPane> allMainPanes;
    private static ImageView mobAvatar, mobAvatarGame, playerAvatarGame,lomAvatarGame;
    private static MenuItem firstMenuItem, secMenuItem, thirdMenuItem, fourthMenuItem;
    private static TextField moveField;
    private static StringProperty mobStats, playerStats;
    private static Player player1;
    private static Mob mob;
    private static Gamelogs playerLogs, gamelogs;
    private static Label lomCostLbl, hpPotionCostLbl, lomQuantityLbl, hpPotionQuantityLbl;
    private static int rndLomCost, rndHpPotionCost;

    public Actions(MainWindowController controller){
        Actions.controller = controller;
        allMainPanes = Arrays.asList(
                controller.getMainPaneBlank(),
                controller.getMainPaneShop(),
                controller.getMainPaneMobFight(),
                controller.getMainPaneQuest1(),
                controller.getMainPaneQuest2()
        );
        mobAvatar = controller.getMobAvatar();
        mobAvatarGame = controller.getMobAvatarGame();
        playerAvatarGame = controller.getPlayerAvatarGame();
        lomAvatarGame = controller.getLomAvatarGame();
        firstMenuItem = controller.getFirstMenuItem();
        secMenuItem = controller.getSecMenuItemMenu();
        thirdMenuItem = controller.getThirdMenuItem();
        fourthMenuItem = controller.getFourthMenuItem();
        moveField = controller.getMoveField();
        mobStats = controller.getMobStats();
        playerStats = controller.getPlayerStatsProperty();
        player1 = MainWindowController.getPlayer();
        gamelogs = MainWindowController.getGameLogs();
        playerLogs = MainWindowController.getPlayerLogs();
        lomCostLbl = controller.getLomCostLbl();
        hpPotionCostLbl = controller.getHpPotionCostLbl();
        lomQuantityLbl = controller.getLomQuantityLbl();
        hpPotionQuantityLbl = controller.getHpPotionQuantityLbl();

    }


    public static void mobFightStart(){
        mob = Mob.createMob();
        updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg(),0);
        moveField.clear();
        if (MainWindowController.getQuest().equals("quest2")) mobAvatar.setImage(MainWindowController.getMobCatImage());
        else mobAvatar.setImage(MainWindowController.getMobImage());
        mobAvatarGame.setVisible(true);
        playerAvatarGame.setVisible(true);
        mobAvatar.setVisible(true);

        setMenuItemsVisibility(true);
        fourthMenuItem.setVisible(false);

        setMenuItemText("Обычная атака","Атака ломом","Отступить","0");
        gamelogs.appendLogs("Вы встретили монстра %s.\n", Actions.getMob().getName());

        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneMobFight());
    }
    public static void shopStart() {
        moveField.clear();
        playerAvatarGame.setVisible(false);
        mobAvatarGame.setVisible(false);
        lomAvatarGame.setVisible(true);
        mobAvatar.setVisible(true);
        mobAvatar.setImage(MainWindowController.getShopImage());

        setMenuItemsVisibility(true);
        secMenuItem.setVisible(false);
        fourthMenuItem.setVisible(false);
        setMenuItemText("Купить предмет", "", "Уйти", "");

        rndLomCost = RandomNums.randomCost();
        rndHpPotionCost = RandomNums.randomCost();
        lomCostLbl.setText(String.format("Цена лома: %d", rndLomCost));
        hpPotionCostLbl.setText(String.format("Цена зелья здоровья: %d", rndHpPotionCost));

        mobStats.set("Торговец");
        gamelogs.appendLogs("Вы встретили торговца\n");

        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneShop());
    }
    public static void shopBuy(List<String> items) {
        if(items.isEmpty()){
            gamelogs.appendLogs("Корзина пуста\n");
            return;
        }
        int lomCost = (int) items.stream().filter(item -> item.equals("lom")).count();
        int hpPotionCost = (int) items.stream().filter(item -> item.equals("hpPotion")).count();

        int totalCost = (lomCost * rndLomCost) + (hpPotionCost * rndHpPotionCost);

        if(player1.getMoney() < totalCost){
            playerLogs.appendLogs("Недостаточно средств\n");
            items.clear();
            controller.setCarts(0);
            gamelogs.appendLogs("Корзина очистилась\n");
            return;
        }

        player1.setMoney(player1.getMoney() - totalCost);

        if (lomCost > 0) {
            player1.setInventory("lom", lomCost);
        }
        if (hpPotionCost > 0) {
            player1.setInventory("hpPotion", hpPotionCost);
        }

        updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
        playerLogs.appendLogs("Предметы куплены\n");
        controller.setCarts(0);
        gamelogs.appendLogs("Корзина пуста\n");
        items.clear();
    }
    public static void quest1Start(){
        moveField.clear();
        updateStats("Char", "?", 0, 0,0);
        mobAvatar.setImage(MainWindowController.getQuest1CharImage());
        mobAvatar.setVisible(true);
        mobAvatarGame.setVisible(true);
        playerAvatarGame.setVisible(true);

        setMenuItemText("Привет", "Кто ты?", "Что тебе надо?", "Уйти");

        setMenuItemsVisibility(true);
        secMenuItem.setVisible(false);
        thirdMenuItem.setVisible(false);
        gamelogs.appendLogs("Вы встретили незнакомца.\n?: Приветствую тебя путник!\n");

        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneQuest1());
    }
    public static void quest2Start(){
        moveField.clear();
        updateStats("Char", "Житель", 0, 0,0);
        MainWindowController.setQuest("quest2");
        mobAvatar.setImage(MainWindowController.getQuest2CharImage());
        mobAvatar.setVisible(true);
        mobAvatarGame.setVisible(true);
        playerAvatarGame.setVisible(true);

        setMenuItemText("Привет, что это за деревня?", "Чем?", "Я согласен", "Идти по тропинке");

        setMenuItemsVisibility(false);
        firstMenuItem.setVisible(true);
        gamelogs.appendLogs("Вы попали в деревню и встретили жителя.\nЖитель: Дарова, мужик!\n");
        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneQuest2());
    }

    public static void rndEvent() {
        int rnd = RandomNums.randomNum(31);
        switch (rnd){
            case 0,1,2,3,4,5,6,7,8,9,10,11,12,13: Actions.mobFightStart(); break;
            case 14,15,16,17,18,19,20,21,22,23,24: Actions.shopStart(); break;
            case 25,26,27,28,29,30: Actions.quest2Start(); break;
        }


    }
    public static void updateStats(String object, String name, int hp, int dmg, int money) {
        if (object.equals("player")) {
            if (player1.getHp() <= 0){
                RegLogController.showAlert(Alert.AlertType.CONFIRMATION, "Вы погибли\n", "Смерть","Смерть");
                javafx.application.Platform.exit();
            }
            playerStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d\nДЕНЬГИ: %d", name, hp, dmg, money));
        }
        else if (object.equals("mob")) {
            mobStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d", name, hp, dmg));
        }
        else {
            mobStats.set(String.format("ИМЯ: %s\n", name));
        }

    }
    public static AnchorPane getVisiblePane(List<AnchorPane> panes) {
        for (AnchorPane pane : panes) {
            if (pane.isVisible()) {
                return pane;
            }
        }
        return panes.getFirst();
    }
    public static void changePane(AnchorPane currentPane, AnchorPane nextPane){
        currentPane.setVisible(false);
        nextPane.setVisible(true);
    }

    public static void saveGameParameters() throws SQLException {
        String checkSql = "SELECT 1 FROM gameParameters WHERE player = ?";
        boolean exists;

        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, player1.getName());
            exists = checkStmt.executeQuery().next();
        }

        String sql;
        if(!exists){
            sql = "INSERT INTO gameParameters(player,mobName, mobHp, mobDmg, lomCost, HpPotionCost, playerLogs, gameLogs, currentAction)" +
                    "VALUES(?,?,?,?,?,?,?,?,?)";
        }
        else {
            sql = "UPDATE gameParameters SET " +
                    "mobName = ?, mobHp = ?, mobDmg = ?, " +
                    "lomCost = ?, HpPotionCost = ?, " +
                    "playerLogs = ?, gameLogs = ?, currentAction= ? " +
                    "WHERE player = ?";
        }


        try (Connection conn = Database.getConnection();
             PreparedStatement prstm = conn.prepareStatement(sql)) {

            if(!exists){
                prstm.setString(1, player1.getName());
                prstm.setString(2, mob != null ? mob.getName() : "NULL");
                prstm.setInt(3, mob != null ? mob.getHp() : 100);
                prstm.setInt(4, mob != null ? mob.getDmg() : 10);
                prstm.setInt(5, rndLomCost);
                prstm.setInt(6, rndHpPotionCost);
                prstm.setString(7, playerLogs != null ? playerLogs.getGameLogsText() : "");
                prstm.setString(8, gamelogs != null ? gamelogs.getGameLogsText() : "");
                prstm.setString(9, getVisiblePane(allMainPanes).getId());
                prstm.execute();
            }
            else {

                prstm.setString(1, mob != null ? mob.getName() : "NULL");
                prstm.setInt(2, mob != null ? mob.getHp() : 100);
                prstm.setInt(3, mob != null ? mob.getDmg() : 10);
                prstm.setInt(4, rndLomCost);
                prstm.setInt(5, rndHpPotionCost);
                prstm.setString(6, playerLogs != null ? playerLogs.getGameLogsText() : "");
                prstm.setString(7, gamelogs != null ? gamelogs.getGameLogsText() : "");
                prstm.setString(8, getVisiblePane(allMainPanes).getId());
                prstm.setString(9, player1.getName());
                prstm.execute();
            }
        }
    }
    public static String loadGameParameters() throws SQLException {
        String sql = "SELECT * FROM gameParameters WHERE player = ?";
        String currentAction = "";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, RegLogController.getLogin());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                if (mob != null) {
                    mob.setName(rs.getString("mobName"));
                    mob.setHp(rs.getInt("mobHp"));
                    mob.setDmg(rs.getInt("mobDmg"));
                }
                rndLomCost = rs.getInt("lomCost");
                rndHpPotionCost = rs.getInt("hpPotionCost");
                playerLogs.setLogs(rs.getString("playerLogs"));
                gamelogs.setLogs(rs.getString("gameLogs"));
                currentAction = rs.getString("currentAction");
            }
        }
        updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
        if (mob != null) {
            updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg(), 0);
        }

        lomCostLbl.setText(String.format("Цена лома: %d", rndLomCost));
        hpPotionCostLbl.setText(String.format("Цена зелья здоровья: %d", rndHpPotionCost));
        return currentAction;
    }

    //геттеры
    public static Mob getMob() {
        return mob;
    }
    public static Label getLomQuantityLbl() {
        return lomQuantityLbl;
    }
    public static Label getHpPotionQuantityLbl() {
        return hpPotionQuantityLbl;
    }
    public static List<AnchorPane> getAllMainPanes() {
        return allMainPanes;
    }

    //сеттеры
    public static void setMove(TextField field, MenuItem menuItem){
        field.setText(menuItem.getText());
    }
    public static void setMenuItemsVisibility(boolean value){
        firstMenuItem.setVisible(value);
        secMenuItem.setVisible(value);
        thirdMenuItem.setVisible(value);
        fourthMenuItem.setVisible(value);
    }
    public static void setMenuItemsDisable(boolean value){
        firstMenuItem.setDisable(value);
        secMenuItem.setDisable(value);
        thirdMenuItem.setDisable(value);
        fourthMenuItem.setDisable(value);
    }
    public static void setMenuItemText(String first, String sec, String thd, String fr){
        firstMenuItem.setText(first);
        secMenuItem.setText(sec);
        thirdMenuItem.setText(thd);
        fourthMenuItem.setText(fr);

    }
}