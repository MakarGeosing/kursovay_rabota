package com.game;

import com.game.controllers.MainWindowController;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Actions {
    private static MainWindowController controller;
    private static List<AnchorPane> allMainPanes;
    private static List<AnchorPane> allActionsPanes;

    public Actions(MainWindowController controller){
        Actions.controller = controller;
        allMainPanes = Arrays.asList(
                controller.mainBlankPane,
                controller.mainPaneShop,
                controller.mainPaneMobFight
        );
        allActionsPanes = Arrays.asList(
                controller.actionsBlankPane,
                controller.actionsPaneShop,
                controller.actionsPaneMobFight
        );
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

        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        AnchorPane currentActionsPane = getVisiblePane(allActionsPanes);

        changePane(currentMainPane, controller.mainPaneMobFight);
        changePane(currentActionsPane, controller.actionsPaneMobFight);
    }
    public static void shopStart(){
        AnchorPane currentMainPane = getVisiblePane(allMainPanes);
        AnchorPane currentActionsPane = getVisiblePane(allActionsPanes);
        changePane(currentMainPane, controller.mainPaneShop);
        changePane(currentActionsPane, controller.actionsPaneShop);

    }

    public static void setMove(TextField field, MenuItem menuItem){
        field.setText(menuItem.getText());
    }
}
