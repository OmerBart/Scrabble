package com.example.Scrabble.DevMains;

import com.example.Scrabble.Model.Player.GuestPlayer;

import java.util.Scanner;

import static java.lang.Thread.onSpinWait;
import static java.lang.Thread.sleep;

public class GuestPlayerTest {
    public static void main(String[] args) throws InterruptedException {
        int port = 65432;
        Scanner scanner = new Scanner(System.in);

        // Create guest player
        GuestPlayer guestPlayer = new GuestPlayer("Guest");

        // Set server address
        guestPlayer.setServerAddress("localhost", port);
        sleep(2000);

        // Join the game
        System.out.println("Joining game..." + guestPlayer.joinGame());

        // Wait for game to start
        System.out.println("Waiting for game to start...");
        sleep(1000);
        int count = 0;
        // Game loop
        do {
            // Guest player's turn

            if (guestPlayer.isMyTurn()) {
                System.out.println("Guest's turn");
                System.out.println("Guest tiles: " + guestPlayer.printTiles());
                System.out.println("Guest getting new Tile: " + guestPlayer.getTile());
                System.out.println("Placing word...");
                // Place word logic here
                System.out.println("Enter word to place: word,x,y,isHorizontal");
                String input = scanner.nextLine();
                String[] inputArray = input.split(",");
                System.out.println("For word: " +
                        inputArray[0] +
                        " Got " +
                        guestPlayer.placeWord(inputArray[0], Integer.parseInt(inputArray[1]),
                                Integer.parseInt(inputArray[2]), Boolean.parseBoolean(inputArray[3]))
                        + " points!");

                guestPlayer.endTurn();
                count++;
            }
//            else
//                System.out.println("Not my turn");
            onSpinWait();
        } while (count <= 1);

        // Close the game
        guestPlayer.disconnectFromServer();
        scanner.close();

    }
}
