package com.game;

import com.game.controllers.MainWindowController;

import java.sql.SQLException;

public class Mob {
    private final int id;
    private int hp, dmg;
    private String name;
    private static final String[] allMobNames = new String[]{"Боб", "Джо", "Риппи", "Алекс", "Альберт", "Бакстер",
            "Брендон", "Вилсон", "Хаус", "Джейк", "Кевин",
            "Майкл", "Джозеф"};
    private final Player player1 = MainWindowController.getPlayer();
    private final Gamelogs gameLogs = MainWindowController.getGameLogs();
    private final Gamelogs playerLogs = MainWindowController.getPlayerLogs();


    public Mob(int id, String name, int hp, int dmg) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
    }

    public static Mob createMob() {
        return new Mob(1, getRndMobName(), 100, RandomNums.rndDmg());
    }
    public static String getRndMobName(){
        int index = RandomNums.randomNum(allMobNames.length);
        return allMobNames[index];
    }

    public void mobTakeDmg(String typeOfAttack, int dmg, int mobHp) {
        if (typeOfAttack.equals("Обычная") && mobHp > 10){
            this.setHp(this.getHp() - dmg);
            gameLogs.appendLogs("Вы нанесли %s %d урона.\n", this.getName(), player1.getDmg());
            Actions.updateStats("mob", this.getName(), this.getHp(), this.getDmg(),0);
            mobAttack();

        }
        else if (typeOfAttack.equals("Ломом") && mobHp > 20) {
            if(MainWindowController.getPlayer().getInventory().get("lom") >= 1) {
                gameLogs.appendLogs("Вы оглушили и ударили %s ломом на %d.\n", this.getName(), (player1.getDmg() * 2));
                this.setHp(this.getHp()-(player1.getDmg()*2));
                player1.setInventory("lom",-1);
                Actions.updateStats("mob", this.getName(), this.getHp(), this.getDmg(),0);

            }
            else {
                playerLogs.appendLogs("У вас нету лома\n");
            }
            
        }
        else {
            int rndMoney = RandomNums.randomNum(10) + 6;
            player1.setMoney(player1.getMoney() + rndMoney);
            gameLogs.appendLogs("Монстр %s умер\n", this.getName());
            playerLogs.appendLogs("Получено: %d денег\n", rndMoney);
            Actions.updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
            switch (MainWindowController.getQuest()){
                case "NULL":{
                    Actions.rndEvent();
                    break;
                }
                case "quest2": {
                    playerLogs.appendLogs("Найдена кошка!\n");
                    MainWindowController.getInstance().setCat(true);
                    MainWindowController.getInstance().handleQuest2("Кошка");
                    break;
                }
            }
        }
    }
    public void mobAttack(){
        player1.setHp(player1.getHp() - this.dmg);
        playerLogs.appendLogs("Монстр нанес вам %d урона\n", this.dmg);
        Actions.updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
    }

    //геттеры
    public static String[] getAllMobNames(){
        return allMobNames;
    }
    public int getId() {
        return id;
    }
    public int getHp() {
        return hp;
    }
    public int getDmg() {
        return dmg;
    }
    public String getName() {
        return name;
    }

    //сеттеры
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }
    public void setName(String name) {
        this.name = name;
    }
}

