package com.wouter;

import com.wouter.items.Item;

import java.util.ArrayList;

public class Player {

    /**
     * The current room the player is in
     */
    private Room currentRoom;

    /**
     * Is the player alive
     */
    private boolean alive = true;

    /**
     * Whether the player is bleeding
     */
    private boolean isBleeding = false;

    /**
     * The health the player has
     */
    private int health = 100;

    /**
     * The items the player is carrying
     */
    private ArrayList<Item> inventory;

    /**
     * How much kg can a player carry
     */
    private int capacity = 100;

    private int carryingWeight = 0;

    public Player(Room initalRoom) {
        this.currentRoom = initalRoom;

        inventory = new ArrayList<>();
    }

    /**
     * The room the player is currently in
     * @return the current room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Move to another room
     * @param room the room to move to
     */
    public void moveRoom(Room room) {
        currentRoom = room;
    }

    /**
     * Does some damage to the player, if its health reaches zero, he dies
     * @param amount
     */
    public void doDamage(int amount) {
        if (alive)
            health -= amount;

        if (health <= 0) {
            alive = false;
        }
    }

    /**
     * Heal the player
     * @param amount the amount to heal
     */
    public void heal(int amount) {
        health += amount;
    }

    /**
     * Returns if the player is dead
     * @return boolean if dead
     */
    public boolean isAlive(){
        return alive;
    }

    public int getHealth() {
        return health;
    }

    /**
     * Pick up an item
     * @param item the item to pick up
     */
    public void take(Item item) {
        if (carryingWeight + item.getWeight() < capacity) {
            item.onPickup();
            carryingWeight += item.getWeight();
            inventory.add(item);
            System.out.println("You pick up: " + item.getName());
        } else {
            System.out.println("You can not hold more items");
        }
    }

    /**
     * Removes an item from the inventory and drops it to the ground
     * @param item item to drop
     */
    public void drop(Item item) {
        inventory.remove(item);
        carryingWeight -= item.getWeight();
        currentRoom.addItem(item);
        System.out.println("You drop " + item.getName() + " to the floor");
    }

    public boolean isBleeding() {
        return isBleeding;
    }

    public void setBleeding(boolean bleeding) {
        isBleeding = bleeding;
    }

    public int getCarryingWeight() {
        return carryingWeight;
    }
}
