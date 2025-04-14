package com.game;

import com.game.controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public Player player1 = new Player(1,"Makar" ,100, 10);
    public Mob mob = new Mob(1, "Abracadabra", 100, 10);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("regLog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 333);

        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        RegLogController controller = fxmlLoader.getController();
        controller.setMainApp(this); // Передаём ссылку на Main
    }

    public static void main(String[] args) {
        launch();
    }
}