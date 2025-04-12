package com.game.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private TextField logLoginField, regLoginField;
    @FXML
    private Button loginBtn, regBtn;

    private final HashMap<String, String> fieldKeys = new HashMap<>();

    @FXML
    void initialize() {}


    @FXML
    void loginBtnAction(ActionEvent event) throws IOException {
        if(validation(logLoginField, logPasswordField)) changeStage("/com/game/mainWindow.fxml", loginBtn, "Game");
    }

    @FXML
    void regBtnAction(ActionEvent event) throws IOException {
        if(validation(regLoginField, regPasswordField)) changeStage("/com/game/mainWindow.fxml", loginBtn, "Game");
    }

    public void changeStage(String FxmlPath, Button BtnName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlPath));
        Stage currentStage = (Stage) BtnName.getScene().getWindow();

        Parent root = loader.load();
        Scene scene = new Scene(root, 700, 600);

        currentStage.setResizable(false);
        currentStage.setTitle(title);
        currentStage.setScene(scene);

    }


    private boolean validation(TextField logField, PasswordField passwordField) {
        final TextField[] fields = {logField, passwordField};
        fieldKeys.put(logField.getId(), "Логин");
        fieldKeys.put(passwordField.getId(), "Пароль");

        for (TextField el : fields) {
            // Проверка на пустоту
            if (el.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Ваше поле " + fieldKeys.get(el.getId()) + " пустое");
                return false;
            }
            else if (el.getId().equals(logField.getId())) {
                String regex_login = "^[a-zA-Z0-9_-]{1,255}$";
                Pattern patt = Pattern.compile(regex_login);
                Matcher matc = patt.matcher(el.getText());
                if (!matc.matches()) {
                    showAlert(Alert.AlertType.ERROR,"Ваш логин не должен:\n" +
                            "Содержать Кириллицу, спец.символы, пробелы и быть больше 255 символов.");
                    return false;
                }
            }
            // Пароль
            else if (el.getId().equals(passwordField.getId())) {
                String regex_password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?!.*\\s)[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{8,255}$";
                Pattern patt = Pattern.compile(regex_password);
                Matcher matc = patt.matcher(el.getText());
                if (!matc.matches()) {
                    showAlert(Alert.AlertType.ERROR,"Ваш пароль должен:\n" +
                            "Содержать не менее 8 символов и быть не более 255-та знаков.\n" +
                            "Содержать как минимум одну цифру.\n"+
                            "Содержать как минимум одну букву в верхнем регистре.\n" +
                            "Содержать как минимум одну букву в нижнем регистре.\n" +
                            "Содержать как минимум один специальный символ.\n" +
                            "Не содержать пробелы.");
                    return false;
                }
                else  sendData(el.getText());
            }
        }
        return true;
    }

    public void showAlert(Alert.AlertType alertType, String msg){
        Alert alert = new Alert(alertType,  msg );
        alert.show();
    }

    private void sendData(String data) {
        System.out.println(data + " Отправлены на сервер");

    }

}
