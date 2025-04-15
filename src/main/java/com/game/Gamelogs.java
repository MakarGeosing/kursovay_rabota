package com.game;

import com.game.controllers.MainWindowController;
import javafx.scene.control.TextArea;

public class Gamelogs {
    private final TextArea gameLogsTA;

    public Gamelogs(TextArea gameLogsObject){
        this.gameLogsTA = gameLogsObject;
    }


    public void appendLogs(String text, Object... args){
         gameLogsTA.appendText(String.format(text, args));
    }

    public void setLogs(String text){
        gameLogsTA.setText(text);
    }

    public void clearLogs(){
        gameLogsTA.clear();
    }

    public TextArea getGameLogsTA() {
        return gameLogsTA;
    }
    public  String getGameLogsText(){
        return gameLogsTA.getText();
    }
}
