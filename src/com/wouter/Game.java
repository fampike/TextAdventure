package com.wouter;

import com.wouter.items.Item;
import com.wouter.items.Weapon;

import java.util.ArrayList;

/**
 *  This class is the main class of the "World of Zuul" application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

class Game
{
    private Parser parser;
    private Player player;

    private boolean finished = false;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office, cellar, basement, attic;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        cellar = new Room("in the wine cellar");
        basement = new Room("In the basement");
        attic = new Room("in the attic");

        //Items
        Item broom, potion_lesser_venom;

        broom = new Item("Broom", 2);
        potion_lesser_venom = new Item("Potion of lesser venom", 1);

        Weapon weapon = new Weapon("Excallibur", 10);

        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);
        theatre.setExit("down",  basement);

        theatre.addItems(broom,potion_lesser_venom);

        pub.setExit("east", outside);
        pub.setExit("up", attic);
        pub.setExit("down", cellar);

        pub.addItem(weapon);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        cellar.setExit("up", pub);

        attic.setExit("down", pub);

        basement.setExit("up", theatre);



        player = new Player(outside);
        player.setBleeding(true);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        while (! finished) {
            Command command = parser.getCommand();
            processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private void processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("quit"))
            quit();
        else if (commandWord.equals("look"))
            System.out.println(player.getCurrentRoom().getLongDescription());
        else if (commandWord.equals("items"))
            System.out.println(player.getCurrentRoom().getItemsString());
        else if (commandWord.equals("take"))
            takeItem(command);
        else if (commandWord.equals("drop"))
            dropItem(command);

    }



    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
        System.out.println(player.getCurrentRoom().getItemsString());
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }


    /**
     * looks if the item is in the room
     * if it is put it in the inventory
     *
     */
    private void takeItem(Command command){
        ArrayList<Item> items = player.getCurrentRoom().getItems();
        String second = command.getSecondWord();
        for (Item item : items) {
            if (item.getName().equals(second)) {
                player.take(item);

            }else
                System.out.println("That item is not in the room...");
        }


    }

    /**
     * looks if the item is in the inventory
     * if it is drop it to the room
     *
     */
    private void dropItem(Command command){
        ArrayList<Item> items = player.getInventory();
        String second = command.getSecondWord();

        Item found = null;

        for (Item item : items) {
            if (item.getName().equals(second)) {
                found = item;

            }else
                System.out.println("You dont have this item");

        }

        if (found != null) {
            player.drop(found);
        }
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */

    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }


        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            //Move to the next room first
           player.moveRoom(nextRoom);
           //Show the user some information about the room
            System.out.println(player.getCurrentRoom().getLongDescription());
            //Show the users the items in the room
            System.out.println(player.getCurrentRoom().getItemsString());
            System.out.println();
            //Show the user some information about you as a player
            System.out.println("Player status: health: "+ player.getHealth() + " is alive: " + player.isAlive() + " is bleeding: " + player.isBleeding());

            //If the player is bleeding, do some damage
            if (player.isBleeding()) {
                player.doDamage(5 + player.getCarryingWeight() / 2);
                System.out.println("You are bleeding and took some damage..\n");
                //If the player died because of the bleeding
                if (!player.isAlive()) {
                    System.out.println("Blood loss does not go well on a heart, this was all yours could take.\n");
                    quit();
                }
            }


        }
    }

    /**
     * Flip a global variable indicating our intent to quit.
     */
    private void quit()
    {
       finished = true;
    }


    public static void main(String[] args)
    {
        Game game = new Game();
        game.play();
    }
}
