package com.game;

import com.game.controllers.MainWindowController;

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
    //private final Mob mob = Actions.getMob();

    public Mob(int id, String name, int hp, int dmg) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
    }

    public static Mob createMob() {
        return new Mob(1, getRndMobName(), 100, 10);
    }
    public static String getRndMobName(){
        int index = RandomNums.randomNum(allMobNames.length);
        return allMobNames[index];
    }

    public void mobTakeDmg(String typeOfAttack, int dmg, int mobHp){
        if (typeOfAttack.equals("Обычная") && mobHp > 10){
            Actions.getMob().setHp(dmg);
            gameLogs.appendLogs("Вы нанесли %s %d урона.\n", Actions.getMob().getName(), player1.getDmg());
            Actions.updateStats("mob", Actions.getMob().getName(), Actions.getMob().getHp(), Actions.getMob().getDmg(),0);
        }
        else if (typeOfAttack.equals("Ломом") && mobHp > 20) {
            if((int) MainWindowController.getPlayer().getInventory().get("lom") >= 1) {
                System.out.println(player1.getInventory().get("lom"));
                gameLogs.appendLogs("Вы ударили %s ломом на %d.\n", Actions.getMob().getName(), (player1.getDmg() * 2));
                Actions.getMob().setHp(-(player1.getDmg()*2));
                player1.setInventory("lom",-1);
                Actions.updateStats("mob", Actions.getMob().getName(), Actions.getMob().getHp(), Actions.getMob().getDmg(),0);

                System.out.println(player1.getInventory());
            }
            else {
                playerLogs.appendLogs("У вас нету лома\n");
            }
            
        }
        else {
            int rndMoney = RandomNums.randomNum(10) + 1;
            player1.setMoney(player1.getMoney() + rndMoney);
            gameLogs.appendLogs("Монстр %s умер\n", Actions.getMob().getName());
            playerLogs.appendLogs("Получено: %d денег\n", rndMoney);
            Actions.updateStats("player", player1.getName(), player1.getHp(), player1.getDmg(), player1.getMoney());
            Actions.rndEvent();
        }
    }

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

    public void setHp(int hp) {
        this.hp += hp;
    }
    public void setDmg(int dmg) {
        this.dmg += dmg;
    }
    public void setName(String name) {
        this.name = name;
    }
}

