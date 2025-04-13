package com.game.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.game.Gamelogs;
import com.game.Mob;
import com.game.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class mainWindowController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button LomAttackBtn,attackBtn,retreatBtn, clearGameLogsBtn,exitBtn;
    @FXML
    private TextArea gameLogsTA,playerStatsTA, mobStatsTA;
    @FXML
    private ImageView mobAvatarGame,playerAvatar,playerAvatarGame, mobAvatar;
    @FXML
    private Label playerAvatarLbl, mobAvatarLbl;


    private Player player1;
    private Mob mob;
    private final StringProperty playerStats = new SimpleStringProperty();
    private final StringProperty mobStats = new SimpleStringProperty();
    private final StringProperty mobName = new SimpleStringProperty();
    private Gamelogs gamelogs;

    @FXML
    public void initialize() {
        gamelogs = new Gamelogs(gameLogsTA);
        playerStatsTA.textProperty().bind(playerStats);
        mobStatsTA.textProperty().bind(mobStats);
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
    void LomAttackBtnAction(ActionEvent event) {
        gamelogs.appendLogs("Вы ударили %s ломом на %d.\n", mob.getName() ,(player1.getDmg()*2));
    }

    @FXML
    void attackBtnAction(ActionEvent event) {
        mob.setHp(-(player1.getDmg()));
        gamelogs.appendLogs("Вы нанесли %s %d урона.\n", mob.getName(), player1.getDmg());
        updateStats("mob", mob.getName(), mob.getHp(), mob.getDmg());
    }

    @FXML
    void retreatBtnAction(ActionEvent event) {
        gamelogs.appendLogs("Вы испугались %s и отступили.\n", mob.getName());

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

    public void clearGameLogsBtnAction(ActionEvent actionEvent) {
        gamelogs.clearLogs();
    }

    public void exitBtnAction(ActionEvent actionEvent) {
        javafx.application.Platform.exit();

    }
}

