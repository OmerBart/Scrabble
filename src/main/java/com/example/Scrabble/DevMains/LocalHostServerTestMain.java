package com.example.Scrabble.DevMains;

import com.example.Scrabble.Model.Player.GuestPlayer;
import com.example.Scrabble.Model.Player.HostPlayer;

import java.util.Scanner;

import static java.lang.Thread.sleep;

public class LocalHostServerTestMain {
    public static void main(String[] args) throws InterruptedException {
        //System.out.println("i am ");
        HostPlayer hostPlayer = HostPlayer.get(new GuestPlayer("Host", 1));
        Scanner scanner = new Scanner(System.in);


        sleep(1500);
        System.out.println(hostPlayer.startGame());
        sleep(1500);
        System.out.println("host tiles: " + hostPlayer.printTiles());
        sleep(1500);
        if(hostPlayer.isMyTurn())
        {
            System.out.println("Host's turn");
            System.out.println(hostPlayer.getTile());
            //sleep(5000);
            //GameManager.get().updatePlayers();
            //sleep(2000);
        }
        else
            System.out.println("da fuck man");
        //sleep(2000);
        //System.out.println(hostPlayer.getPlayerTiles());
        sleep(3000);
        //System.out.println("Close Host Server? (y/n)");
        String input = scanner.nextLine();
        System.out.println(hostPlayer.placeWord(input,7,7,true));
         if (input.equals("y")) {
            hostPlayer.stopGame();
            scanner.close();
        }

        // hostPlayer.startGame();
    }
}
