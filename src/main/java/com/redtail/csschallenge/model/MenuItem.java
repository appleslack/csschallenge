package com.redtail.csschallenge.model;

public class MenuItem {

    private String name;
    private String temp;
    private int shelfLife;
    private double decayRate;

    public MenuItem(String name, String temp, int shelfLife, double decayRate) {
        this.name = name;
        this.temp = temp;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
    }

    public String getName() {
        return name;
    }

    public String getTemp() {
        return temp;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public double getDecayRate() {
        return decayRate;
    }

    @Override
    public String toString() {
        return "MenuItem [decayRate=" + decayRate + ", name=" + name + ", shelfLife=" + shelfLife + ", temp=" + temp
                + "]";
    }

    
}