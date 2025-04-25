package com.game;

import com.game.controllers.MainWindowController;
import com.game.controllers.RegLogController;
import eu.hansolo.tilesfx.addons.Switch;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.*;


public class Actions {
    private static MainWindowController controller;
    private static List<AnchorPane> allMainPanes;
    private static ImageView mobAvatar, mobAvatarGame, playerAvatarGame,lomAvatarGame;
    private static MenuItem firstMenuItem, secMenuItem, thirdMenuItem;
    private static final List<MenuItem> allMenuItems = new ArrayList<MenuItem>();
    private static final String[] mobFightMenuTextItems = new String[]{"Обычная атака","Атака ломом","Отступить"};
    private static final String[] shopMenuTextItems = new String[]{"Купить предмет","Продать предмет","Уйти"};
    private static final String[] quest1TextItems = new String[]{"Привет","Кто ты?","Что тебе надо?"};
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
                controller.getMainPaneQuest1()
        );
        mobAvatar = controller.getMobAvatar();
        mobAvatarGame = controller.getMobAvatarGame();
        playerAvatarGame = controller.getPlayerAvatarGame();
        lomAvatarGame = controller.getLomAvatarGame();
        firstMenuItem = controller.getFirstMenuItem();
        secMenuItem = controller.getSecMenuItemMenu();
        thirdMenuItem = controller.getThirdMenuItem();
        allMenuItems.add(controller.getFirstMenuItem());
        allMenuItems.add(controller.getSecMenuItemMenu());
        allMenuItems.add(controller.getThirdMenuItem());
        moveField = controller.getMoveField();
        mobStats = controller.getMobStats();
        playerStats = controller.getPlayerStatsProperty();
        player1 = controller.getPlayer();
        gamelogs = controller.getGameLogs();
        playerLogs = controller.getPlayerLogs();
        lomCostLbl = controller.getLomCostLbl();
        hpPotionCostLbl = controller.getHpPotionCostLbl();
        lomQuantityLbl = controller.getLomQuantityLbl();
        hpPotionQuantityLbl = controller.getHpPotionQuantityLbl();


    }

    public static void mobFightStart(){
        mob = Mob.createMob();
        updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg(),0);
        moveField.clear();
        mobAvatar.setImage(controller.getMobImage());
        mobAvatarGame.setVisible(true);
        playerAvatarGame.setVisible(true);
        mobAvatar.setVisible(true);
        setTextMenuItem(allMenuItems,mobFightMenuTextItems);

        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneMobFight());
    }
    public static void shopStart(){
        moveField.clear();
        setTextMenuItem(allMenuItems,shopMenuTextItems);
        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneShop());

        playerAvatarGame.setVisible(false);
        mobAvatarGame.setVisible(false);
        lomAvatarGame.setVisible(true);
        mobAvatar.setVisible(true);
        mobAvatar.setImage(controller.getShopImage());

        rndLomCost = RandomNums.randomCost();
        rndHpPotionCost = RandomNums.randomCost();
        lomCostLbl.setText(String.format("Цена лома: %d", rndLomCost));
        hpPotionCostLbl.setText(String.format("Цена Зелья здоровья: %d", rndHpPotionCost));

        mobStats.set("Магазин");
        gamelogs.appendLogs("Вы встретили магазин\n");
    }
    public static void shopBuy(List<String> items) {
        if(items.isEmpty()){
            gamelogs.appendLogs("Корзина пуста\n");
            return;
        }
        int lomCount = (int) items.stream().filter(item -> item.equals("lom")).count();
        int hpPotionCount = (int) items.stream().filter(item -> item.equals("hpPotion")).count();

        int totalCost = (lomCount * rndLomCost) + (hpPotionCount * rndHpPotionCost);

        if(player1.getMoney() < totalCost){
            playerLogs.appendLogs("Недостаточно средств\n");
            items.clear();
            controller.setCarts(0);
            gamelogs.appendLogs("Корзина очистилась\n");
            return;
        }

        player1.setMoney(player1.getMoney() - totalCost);

        if (lomCount > 0) {
            player1.setInventory("lom", lomCount);
        }
        if (hpPotionCount > 0) {
            player1.setInventory("hpPotion", hpPotionCount);
        }

        updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
        playerLogs.appendLogs("Предметы куплены\n");
        controller.setCarts(0);
        gamelogs.appendLogs("Корзина пуста\n");
        items.clear();
    }
    public static void quest1Start(){
        moveField.clear();
        updateStats("Char", "Чурбек", 0, 0,0);
        mobAvatar.setImage(controller.getQuestCharImage());
        mobAvatar.setVisible(true);
        mobAvatarGame.setVisible(true);
        playerAvatarGame.setVisible(true);
        setTextMenuItem(allMenuItems, quest1TextItems);

        gamelogs.appendLogs("Вы встретели Чурбека\nЧурбек: Приветсвую тебя путник! Этому миру пизда!\n");

        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        changePane(currentMainPane, controller.getMainPaneQuest1());
    }


    public static void rndEvent(){
        int rnd = RandomNums.randomNum(3);
        switch (rnd){
            case 0: Actions.mobFightStart(); break;
            case 1: Actions.shopStart(); break;
            case 2: Actions.quest1Start(); break;
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
        else if (object.equals("mob"))
        {
            mobStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d", name, hp, dmg));
        }
        else
        {
            mobStats.set(String.format("ИМЯ: %s\n", name));
        }

    }
    private static AnchorPane getVisiblePane(List<AnchorPane> panes) {
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

    public static Mob getMob() {
        return mob;
    }
    public static Label getLomQuantityLbl() {
        return lomQuantityLbl;
    }
    public static Label getHpPotionQuantityLbl() {
        return hpPotionQuantityLbl;
    }

    public static void setMove(TextField field, MenuItem menuItem){
        field.setText(menuItem.getText());
    }
    public static void setTextMenuItem(List<MenuItem> allMenuItems, String[] text){
        for (int i = 0; i < allMenuItems.size(); i++) {
            if (i < text.length) {
                allMenuItems.get(i).setText(text[i]);
            }
        }
    }
}