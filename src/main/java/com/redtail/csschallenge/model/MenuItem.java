package com.redtail.csschallenge.model;

import com.redtail.csschallenge.DeliveryShelfType;

public class MenuItem {

    private String name;

    private final int shelfLife;
    private final double decayRate;
    private final DeliveryShelfType shelf;
    private final String shelfName;

    public MenuItem(String name, String shelfName, int shelfLife, double decayRate) {
        this.name = name;
        this.shelfLife = shelfLife;
        this.decayRate = decayRate;
        this.shelfName = shelfName;

        switch (shelfName) {
            case "hot":
                this.shelf = DeliveryShelfType.HOT;
                break;
            case "cold":
                this.shelf = DeliveryShelfType.COLD;
                break;
             case "frozen":
                this.shelf = DeliveryShelfType.FROZEN;
                break;
        
            default:
                this.shelf = DeliveryShelfType.OVERFLOW;
                break;
        }
    }

    public DeliveryShelfType getDeliveryShelf() {
        return shelf;
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