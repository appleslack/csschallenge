package com.redtail.csschallenge.model;

import com.redtail.csschallenge.DeliveryShelfType;

public class MenuItem {

    private String name;

    private final int shelfLife;
    private final double decayRate;
    private final String shelfName;
    private final DeliveryShelfType defaultShelf;

    public MenuItem(String name, String shelfName, int shelfLife, double decayRate) {
        this.name = name;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
        this.shelfName = shelfName;

        switch (shelfName) {
            case "hot":
                this.defaultShelf = DeliveryShelfType.HOT;
                break;
            case "cold":
                this.defaultShelf = DeliveryShelfType.COLD;
                break;
             case "frozen":
                this.defaultShelf = DeliveryShelfType.FROZEN;
                break;
        
            default:
                this.defaultShelf = DeliveryShelfType.OVERFLOW;
                break;
        }
    }

    public DeliveryShelfType getDefaultShelf() {
        return defaultShelf;
    }

    public String getName() {
        return name;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public double getDecayRate() {
        return decayRate;
    }

    public String getShelfName() {
        return shelfName;
    }

    @Override
    public String toString() {
        return "MenuItem [decayRate=" + decayRate + ", name=" + name + ", shelfLife=" + shelfLife + ", shelf=" + shelfName
                + "]";
    }

    
}