package com.game.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.game.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class regLogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField logPasswordField;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField loginField;

    @FXML
    private Button regBtn;

    @FXML
    private TextField regField;

    @FXML
    private PasswordField regPasswordField;

    @FXML
    void loginBtnAction(ActionEvent event) {
        FXMLLoader loader =  new FXMLLoader (Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(loader.load(), 600, 699);

    }

    @FXML
    void regBtnAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert logPasswordField != null : "fx:id=\"logPasswordField\" was not injected: check your FXML file 'regLog.fxml'.";
        assert loginBtn != null : "fx:id=\"loginBtn\" was not injected: check your FXML file 'regLog.fxml'.";
        assert loginField != null : "fx:id=\"loginField\" was not injected: check your FXML file 'regLog.fxml'.";
        assert regBtn != null : "fx:id=\"regBtn\" was not injected: check your FXML file 'regLog.fxml'.";
        assert regField != null : "fx:id=\"regField\" was not injected: check your FXML file 'regLog.fxml'.";
        assert regPasswordField != null : "fx:id=\"regPasswordField\" was not injected: check your FXML file 'regLog.fxml'.";

    }

}
