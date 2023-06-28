package com.example.Scrabble.DevMains;

//import com.example.Scrabble.Model.LocalServer.GameManager;
import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;


import java.util.Objects;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class LocalHostServerTestMain {
    public static void main(String[] args) throws InterruptedException {

        //GameManager gameManager = GameManager.get();
        Scanner scanner = new Scanner(System.in);

        // Create host player
        HostPlayer hostPlayer = HostPlayer.get(new GuestPlayer("Host"));

        // Wait for players to join
        System.out.println("Waiting for players to join...");
        sleep(2000);
        // Start the game
        System.out.println("Start game? (y/n)");
        if(Objects.equals(scanner.nextLine(), "y")) {
            System.out.println("Starting game...");
            System.out.println(hostPlayer.startGame());
        }
        int count = 0;
        sleep(1000);
        // Game loop
        while (true) {
            // Host player's turn
            if (hostPlayer.isMyTurn()) {
                System.out.println("Host's turn");
                System.out.println("Host tiles: " + hostPlayer.printTiles());
                System.out.println(hostPlayer.getTile());

                //System.out.println("Placing word...");
                // Place word logic here
               // sleep(1000);
                hostPlayer.endTurn();
                count++;
            }

            // End Game?
            if (count == 2) {
                System.out.println("End Game? (y/n)");
                String input = scanner.nextLine();
                if (input.equals("y")) {
                    System.out.println("Game over!");
                    break;
                }
                if (input.equals("n")) {
                    System.out.println("Game continues!");
                }
            }
        }

        // Close the game
        //gameManager.stopGame();
        scanner.close();




//        sleep(1500);
//        System.out.println(hostPlayer.startGame());
//        sleep(1500);
//        System.out.println("host tiles: " + hostPlayer.printTiles());
//        sleep(1500);
//        if(hostPlayer.isMyTurn())
//        {
//            System.out.println("Host's turn");
//            System.out.println(hostPlayer.getTile());
//            sleep(1000);
//            hostPlayer.endTurn();
//            //GameManager.get().updatePlayers();
//            //sleep(4000);
//        }
//        else
//            System.out.println("da fuck man");
//        //sleep(2000);
//        //System.out.println(hostPlayer.getPlayerTiles());
//        //sleep(2000);
//        //System.out.println("Close Host Server? (y/n)");
//        String input = scanner.nextLine();
//        System.out.println("y to end?");
//       // System.out.println(hostPlayer.placeWord(input,7,7,true));
//         if (input.equals("y")) {
//            hostPlayer.stopGame();
//            scanner.close();
//        }

        // hostPlayer.startGame();
    }
}
