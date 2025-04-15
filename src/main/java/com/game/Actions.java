package com.game;

import com.game.controllers.MainWindowController;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.*;


public class Actions {
    private static MainWindowController controller;
    private static List<AnchorPane> allMainPanes;
    private static ImageView mobAvatar, playerAvatarGame;
    private static MenuItem firstMenuItem, secMenuItem, thirdMenuItem;
    private static final List<MenuItem> allMenuItems = new ArrayList<MenuItem>();
    private static final String[] mobFightMenuTextItems = new String[]{"Обычная атака","Атака ломом","Отступить"};
    private static final String[] shopMenuTextItems = new String[]{"Купить предмет","Продать предмет","Уйти"};
    private static TextField moveField;
    private static TextArea mobStatsTA;

    public Actions(MainWindowController controller){
        Actions.controller = controller;
        allMainPanes = Arrays.asList(
                controller.getMainPaneBlank(),
                controller.getMainPaneShop(),
                controller.getMainPaneMobFight(),
                controller.getMainPaneQuest1()
        );
        mobAvatar = controller.getMobAvatar();
        playerAvatarGame = controller.getPlayerAvatarGame();
        firstMenuItem = controller.getFirstMenuItem();
        secMenuItem = controller.getSecMenuItemMenu();
        thirdMenuItem = controller.getThirdMenuItem();
        allMenuItems.add(firstMenuItem);
        allMenuItems.add(secMenuItem);
        allMenuItems.add(thirdMenuItem);
        moveField = controller.getMoveField();
        mobStatsTA = controller.getMobStatsTA();

    }

    private static AnchorPane getVisiblePane(List<AnchorPane> panes) {
        for (AnchorPane pane : panes) {
            if (pane.isVisible()) {
                return pane;
            }
        }
        return panes.get(0);
    }

    public static void changePane(AnchorPane currentPane, AnchorPane nextPane){
        currentPane.setVisible(false);
        nextPane.setVisible(true);
    }

    public static void mobFightStart(){
        moveField.clear();
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
