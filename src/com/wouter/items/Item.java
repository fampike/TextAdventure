package com.wouter.items;

public class Item {


    private final int weight;
    private String name;
    private String description;

    public Item(String name, int weight) {
        this.weight = weight;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public void onPickup() {}

    public void onUse() {}
}