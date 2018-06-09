package com.wouter.items;

public class Weapon extends Item {

    public Weapon(String name, int weight) {
        super(name, weight);
    }

    @Override
    public void onPickup() {
        System.out.println("The mighty sword shines");
    }

    @Override
    public void onUse() {
        System.out.println("You swing your sword.");
    }
}
