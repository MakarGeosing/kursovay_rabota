package com.game;

public class Mob {
    private final int id;
    private int hp, dmg;
    private String name;
    private static final String[] allMobNames = new String[]{"Боб", "Джо", "Риппи", "Алекс", "Альберт", "Бакстер",
            "Брендон", "Вилсон", "Хаус", "Джейк", "Джо", "Кевин",
            "Майкл", "Джозеф"};
    private static Mob mob;

    public Mob(int id, String name, int hp, int dmg) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.dmg = dmg;
    }

    public static Mob createMob(){
        mob = new Mob(1, Mob.getRndMobName(), 100, 10);
        return mob;
    }

    public static Mob getMob() {
        return mob;
    }
    public static String getRndMobName(){
        return allMobNames[RandomNums.randomNum()];
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

