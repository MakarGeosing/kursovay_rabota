package com.game;

import java.util.HashMap;

public class Player {
    private final int id;
    public String name;
    private int hp, dmg, money;
    private final HashMap<String, Integer> inventory = new HashMap<String, Integer>();

    public Player(int id, String name, int hp, int dmg, int money) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
        this.money = money;
        this.inventory.put("lom", 1111);
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
    public int getMoney() {
        return money;
    }
    public HashMap getInventory() {
        return inventory;
    }
    public int getInventoryValue(String key) {
        return inventory.get(key);
    }


    public void setHp(int hp) {
        this.hp += hp;
    }
    public void setInventory(String item, int value) {
        if (!inventory.containsKey(item)) {
            inventory.put(item, 0);
        }
        inventory.put(item, inventory.get(item) + value);
    }
    public void setDmg(int dmg) {
        this.dmg += dmg;
    }
    public void setMoney(int money) {
        this.money = money;
    }
}
