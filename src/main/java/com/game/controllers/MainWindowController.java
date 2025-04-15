package com.game.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.game.Actions;
import com.game.Gamelogs;
import com.game.Mob;
import com.game.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainWindowController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField moveField;
    @FXML
    private MenuItem firstMenuItem, secMenuItem, thirdMenuItem, undoClearGameLogsMenuItem;
    @FXML
    private Button LomAttackBtn,attackBtn,retreatBtn, exitBtn;
    @FXML
    private SplitMenuButton clearGameLogsBtn, submitMenuBtn;
    @FXML
    private TextArea gameLogsTA, playerLogsTA, playerStatsTA, mobStatsTA;
    @FXML
    private ImageView mobAvatarGame,playerAvatar,playerAvatarGame, mobAvatar;
    @FXML
    private Label playerAvatarLbl, mobAvatarLbl;
    @FXML
    private AnchorPane mainPaneMobFight, actionsPane, mainPaneShop, mainPaneBlank, actionsPaneBlank, mainPaneQuest1;


    private Player player1;
    private Mob mob;
    private final StringProperty playerStats = new SimpleStringProperty();
    private final StringProperty mobStats = new SimpleStringProperty();
    private final StringProperty mobName = new SimpleStringProperty();
    private Gamelogs gameLogs;
    private Gamelogs playerLogs;
    private final Image mobImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/monster.png")).toString());
    private final Image playerImage = new Image(Objects.requireNonNull(getClass().getResource("/com/game/player.png")).toString());
    private String prevGameLogsText, prevPlayerLogsText;
    private Actions actions;

    @FXML
    public void initialize() {
        gameLogs = new Gamelogs(gameLogsTA);
        playerLogs = new Gamelogs(playerLogsTA);
        playerStatsTA.textProperty().bind(playerStats);
        mobStatsTA.textProperty().bind(mobStats);
        mobAvatar.setImage(mobImage);
        playerAvatar.setImage(playerImage);

        mobAvatarGame.imageProperty().bind(mobAvatar.imageProperty());
        mobAvatarGame.visibleProperty().bind(mobAvatar.visibleProperty());

        playerAvatarGame.imageProperty().bind(playerAvatar.imageProperty());
        mobStatsTA.visibleProperty().bind(mobAvatar.visibleProperty());
        actions = new Actions(this);

        Actions.mobFightStart();

    }

    public void updateStats(String object, String name, int hp, int dmg) {
        if (object.equals("player")) {
            playerStats.set(String.format("Имя: %s\nHP: %d\nDMG: %d", name, hp, dmg));
        }
        else {
            mobStats.set(String.format("Имя: %s\nHP: %d\nDMG: %d", name, hp, dmg));
        }
    }


    public void setPlayer(Player player) {
        this.player1 = player;
        updateStats("player", player.getName(), player.getHp(), player.getDmg());
    }
    public void setMob(Mob mob) {
        this.mob = mob;
        updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg());
    }

    @FXML
    public void submitMenuAction(ActionEvent actionEvent) {
        if (getMoveFieldText().equals("Отступить") || getMoveFieldText().equals("Уйти")){
            playerAvatarGame.setVisible(false);
            mobAvatar.setVisible(false);
            if(getMoveFieldText().equals("Отступить")){
                playerLogs.appendLogs("Вы испугались %s и отступили.\n", mob.getName());
                Actions.shopStart();
            }
            else{
                playerLogs.appendLogs("Вы ушли.\n");
                Actions.mobFightStart();
            }


        }
        else if (getMoveFieldText().equals("Атака ломом")) {
            gameLogs.appendLogs("Вы ударили %s ломом на %d.\n", mob.getName() ,(player1.getDmg()*2));
        }
        else if (getMoveFieldText().equals("Обычная атака")) {
            mob.setHp(-(player1.getDmg()));
            gameLogs.appendLogs("Вы нанесли %s %d урона.\n", mob.getName(), player1.getDmg());
            updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg());
            Actions.mobFightStart();
        }
        else if (getMoveFieldText().equals("Купить предмет")){
            playerLogs.appendLogs("Предмет куплен\n");
        }
        else if (getMoveFieldText().equals("Продать предмет")){
            playerLogs.appendLogs("Предмет продан\n");
        }
        else {
            RegLogController.showAlert(Alert.AlertType.ERROR, """
                    Введено неверное действие
                    Подсказка: Вы можете нажать на стрелочку справа от кнопки "Подвердить ход" и узнать все доступные на
                    данный момент действия
                    """);
        }
    }
    @FXML
    public void firstMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(),getFirstMenuItem());
    }
    @FXML
    public void secMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(),getSecMenuItemMenu());
    }
    @FXML
    public void thirdMenuItemAction(ActionEvent actionEvent) {
        Actions.setMove(getMoveField(),getThirdMenuItem());
    }

    @FXML
    void playerAvatarEntered(MouseEvent event) {
        playerAvatarLbl.setVisible(true);
    }
    @FXML
    void mobAvatarEntered(MouseEvent  event) {
        mobName.set(String.format("Это монстр %s", mob.getName()));
        mobAvatarLbl.textProperty().bind(mobName);
        mobAvatarLbl.setVisible(true);
    }
    @FXML
    void playerAvatarExited(MouseEvent event) {
        playerAvatarLbl.setVisible(false);
    }
    @FXML
    void mobAvatarExited(MouseEvent  event) {
        mobAvatarLbl.setVisible(false);
    }

    @FXML
    public void clearGameLogsBtnAction(ActionEvent actionEvent) {
        try {
            if (gameLogs != null) {
                String prevGameLogsTextCheck = gameLogs.getGameLogsText();
                if (!prevGameLogsTextCheck.isEmpty()) {
                    prevGameLogsText = gameLogs.getGameLogsText();
                    gameLogs.clearLogs();
                }
            }
            if (playerLogs != null) {
                String prevPlayerLogsTextCheck = playerLogs.getGameLogsText();
                if (!prevPlayerLogsTextCheck.isEmpty()) {
                    prevPlayerLogsText = playerLogs.getGameLogsText();
                    playerLogs.clearLogs();
                }
            }
        }
        catch (Exception e) {/*pass*/}

    }
    @FXML
    public void undoClearGameLogsAction(ActionEvent actionEvent) {
        if(gameLogs.getGameLogsText().length() > prevGameLogsText.length()){
            //pass
        }
        else if(playerLogs.getGameLogsText().length() > prevPlayerLogsText.length()){
            //pass
        }
        else {
            gameLogs.setLogs(prevGameLogsText);
            playerLogs.setLogs(prevPlayerLogsText);
        }
    }

    @FXML
    public void exitBtnAction(ActionEvent actionEvent) {
        javafx.application.Platform.exit();
    }



    public void buy(ActionEvent actionEvent) {
        Actions.mobFightStart();
    }

    public TextArea getMobStatsTA() {
        return mobStatsTA;
    }
    public TextField getMoveField() {
        return moveField;
    }
    public String getMoveFieldText() {
        return moveField.getText();
    }
    public MenuItem getFirstMenuItem() {
        return firstMenuItem;
    }
    public MenuItem getSecMenuItemMenu() {
        return secMenuItem;
    }
    public MenuItem getThirdMenuItem() {
        return thirdMenuItem;
    }

    public TextArea getGameLogsTA() {
        return gameLogsTA;
    }
    public AnchorPane getActionsPane() {
        return actionsPane;
    }

    public AnchorPane getActionsPaneBlank() {
        return actionsPaneBlank;
    }

    public AnchorPane getMainPaneBlank() {
        return mainPaneBlank;
    }

    public AnchorPane getMainPaneMobFight() {
        return mainPaneMobFight;
    }

    public AnchorPane getMainPaneQuest1() {
        return mainPaneQuest1;
    }

    public AnchorPane getMainPaneShop() {
        return mainPaneShop;
    }

    public ImageView getMobAvatar() {
        return mobAvatar;
    }

    public ImageView getMobAvatarGame() {
        return mobAvatarGame;
    }

    public ImageView getPlayerAvatar() {
        return playerAvatar;
    }

    public ImageView getPlayerAvatarGame() {
        return playerAvatarGame;
    }

    public TextArea getPlayerLogsTA() {
        return playerLogsTA;
    }

    public void setPlayerLogsTA(TextArea playerLogsTA) {
        this.playerLogsTA = playerLogsTA;
    }
}

