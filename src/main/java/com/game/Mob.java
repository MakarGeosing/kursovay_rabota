package com.game;

public class Mob {
    private final int id;
    private int hp, dmg;
    private String name;

    public Mob(int id, String name, int hp, int dmg) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
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

