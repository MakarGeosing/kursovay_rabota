package com.game;

import com.game.controllers.MainWindowController;
import javafx.beans.property.StringProperty;
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
    private static TextField moveField;
    private static StringProperty mobStats, playerStats;
    private static Player player1;
    private static Mob mob;
    private static Gamelogs playerLogs, gamelogs;
    private static Label lomCostLbl, shopHintLbl;
    private static int rndLomCost;

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
        mob = Mob.getMob();
        gamelogs = controller.getGameLogs();
        playerLogs = controller.getPlayerLogs();
        lomCostLbl = controller.getLomCostLbl();
        shopHintLbl = controller.getShopHintLbl();

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

    public static void mobFightStart(){
        mob = Mob.createMob();
        System.out.println(mob.getName());
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

        rndLomCost = RandomNums.randomCost();
        lomAvatarGame.setVisible(true);
        lomCostLbl.setText(String.format("Цена лома: %d", rndLomCost));
        mobAvatarGame.setVisible(false);
        mobAvatar.setImage(controller.getShopImage());
        mobAvatar.setVisible(true);

        mobStats.set("Магазин");
        gamelogs.appendLogs("Вы встретили магазин\n");

    }
    public static void shopBuy(List<String> items){
        if(player1.getMoney() >= (rndLomCost * items.size())){
            player1.setMoney(player1.getMoney() - (rndLomCost * items.size()));
            for(String el: items){
                if(el.equals("lom")){
                    int lomQuantity = 0;
                    lomQuantity++;
                    player1.setInventory(el, lomQuantity);
                }

                updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
                playerLogs.appendLogs("Предмет куплен\n");
                playerLogs.appendLogs(player1.getInventory().toString() + "\n");
            }
        }
        else
        {
            playerLogs.appendLogs("У вас недостаточно средств\n");
        }
        controller.setCart(0);
        gamelogs.appendLogs("Корзина очистилась\n");
        items.clear();

    }

    public static void updateStats(String object, String name, int hp, int dmg, int money) {
        if (object.equals("player")) {
            playerStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d\nДЕНЬГИ: %d", name, hp, dmg, money));
        } else {
            mobStats.set(String.format("ИМЯ: %s\nХП: %d\nУРОН: %d", name, hp, dmg));
        }
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