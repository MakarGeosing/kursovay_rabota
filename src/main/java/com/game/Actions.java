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
    private static StringProperty mobStats;
    private static Player player1;
    private static Mob mob;
    private static Gamelogs playerLogs;
    private static Gamelogs gamelogs;
    private static Label lomQuantityLbl;
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
        player1 = controller.getPlayer();
        mob = controller.getMob();
        gamelogs = controller.getGameLogs();
        playerLogs = controller.getPlayerLogs();
        lomQuantityLbl = controller.getLomQuantityLbl();


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
        controller.updateStats("mob",mob.getName(), mob.getHp(), mob.getDmg(),0);
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

        rndLomCost = randomCost();
        lomAvatarGame.setVisible(true);
        lomQuantityLbl.setText(String.format("Цена лома: %d", rndLomCost));
        mobAvatarGame.setVisible(false);
        mobAvatar.setImage(controller.getShopImage());
        mobAvatar.setVisible(true);

        mobStats.set("Магазин");
        gamelogs.appendLogs("Вы встретили магазин\n");

    }
    public static void shopBuy(List<String> items){
        if(player1.getMoney() > (rndLomCost * items.size())){
            player1.setMoney(player1.getMoney() - rndLomCost);
            for(String el: items){
                int i = 0;
                i++;
                player1.setInventory(el, i);
                controller.updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
                playerLogs.appendLogs("Предмет куплен\n");
                playerLogs.appendLogs(player1.getInventory().toString());
            }
        }
        else{
            playerLogs.appendLogs("У вас недостаточно средств\n");
        }
        items.clear();

    }

    public static int randomCost(){
        Random rndCost = new Random();
        return rndCost.nextInt(5,20);
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
    public void setPlayer(Player player) {
        player1 = player;
    }
}