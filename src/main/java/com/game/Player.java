package com.game;

public class Player {
    private final int id;
    public String name;
    private int hp, dmg;

    public Player(int id, String name, int hp, int dmg) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
    }










    public String getName() {
        return name;
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


    public void setHp(int hp) {
        this.hp += hp;
    }
    public void setDmg(int dmg) {
        this.dmg += dmg;
    }




}
