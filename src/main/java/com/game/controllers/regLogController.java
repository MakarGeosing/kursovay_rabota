package com.game.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class regLogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField logPasswordField, regPasswordField;

    @FXML
    private Button loginBtn, regBtn;

    @FXML
    private TextField loginField, regField;

    @FXML
    void loginBtnAction(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) loginBtn.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/game/mainWindow.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 700, 600);

        currentStage.setResizable(false);
        currentStage.setTitle("Game");
        currentStage.setScene(scene);

    }

    @FXML
    void regBtnAction(ActionEvent event) {

    }

    @FXML
    void initialize() {


    }

}
