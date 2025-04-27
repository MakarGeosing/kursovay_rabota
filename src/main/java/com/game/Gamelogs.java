package com.game;

import javafx.scene.control.TextArea;

public class Gamelogs {
    private final TextArea gameLogsTA;


    public Gamelogs(TextArea gameLogsObject){
        this.gameLogsTA = gameLogsObject;
    }


    public void appendLogs(String text, Object... args){
         gameLogsTA.appendText(String.format(text, args));
    }
    public void clearLogs(){
        gameLogsTA.clear();
    }


    //геттеры
    public TextArea getGameLogsTA() {
        return gameLogsTA;
    }
    public  String getGameLogsText(){
        return gameLogsTA.getText();
    }

    //сеттеры
    public void setLogs(String text){
        gameLogsTA.setText(text);
    }

}
