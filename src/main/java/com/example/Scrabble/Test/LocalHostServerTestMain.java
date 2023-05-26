package com.example.Scrabble.Test;

import com.example.Scrabble.Model.GuestPlayer;
import com.example.Scrabble.Model.HostPlayer;

import java.util.Scanner;

import static java.lang.Thread.sleep;

public class LocalHostServerTestMain {
    public static void main(String[] args) throws InterruptedException {
        //System.out.println("i am ");
        HostPlayer hostPlayer = HostPlayer.get(new GuestPlayer("Host", 1));
        Scanner scanner = new Scanner(System.in);


        sleep(15000);
        System.out.println("Close Host Server? (y/n)");
        String input = scanner.nextLine();
        if (input.equals("y")) {
            hostPlayer.stopGame();
            scanner.close();
        }

        // hostPlayer.startGame();
    }
}