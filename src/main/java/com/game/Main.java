package com.game;

import com.game.controllers.MainWindowController;
import com.game.controllers.RegLogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Database.initDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("regLog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 333);

        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            System.out.println("Игра закрылась");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText("Вы уверены, что хотите выйти?");
            alert.setContentText("Игра сохранит все ваши данные.");


            Optional<ButtonType> result =  alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                if (!stage.getTitle().equals("Авторизация")){
                    try {
                        MainWindowController.getPlayer().save();
                        Actions.saveGameParameters();
                    }
                    catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else {
                event.consume();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
