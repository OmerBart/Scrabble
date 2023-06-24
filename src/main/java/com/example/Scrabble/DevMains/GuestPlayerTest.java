package com.example.Scrabble.DevMains;

import com.example.Scrabble.Model.Player.GuestPlayer;

import static java.lang.Thread.sleep;

public class GuestPlayerTest {
    public static void main(String[] args) throws InterruptedException {
        int port = 65432;
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //HostPlayer host = new HostPlayer(new GuestPlayer("TheHost", 1));
        GuestPlayer guest = new GuestPlayer("TheGuest", 2);
//        GuestPlayer guest2 = new GuestPlayer("TheGuest2", 3);
//        GuestPlayer guest3 = new GuestPlayer("TheGuest3", 4);
//        GuestPlayer guest4 = new GuestPlayer("TheGuest4", 5);
        //GameManager GM = GameManager.get();


        guest.setServerAddress("localhost",port);
//        guest2.setServerAddress("localhost",port);
//        guest3.setServerAddress("localhost",port);
//        guest4.setServerAddress("localhost",port);
        System.out.println("trying to join game...." + guest.joinGame());
        //guest.joinGame();
       // sleep(2000);
        guest.printTiles();
        //System.out.println("guest tiles: " + guest.printTiles());
        //sleep(4000);
        //System.out.println("guest is my turn? " + guest.isMyTurn());
        if(guest.isMyTurn()){
            System.out.println(guest.getTile());
            sleep(1000);
            //System.out.println(guest.getTile());
            //System.out.println(guest.queryIO("Men"));//18878
           // System.out.println(guest.challengeIO("kaka"));//18878
            if(guest.endTurn())
                System.out.println("turn ended");
            else
                System.out.println("turn not ended");


//            System.out.println(guest.endTurn());
//            System.out.println(guest.endTurn());


        }
//        System.out.println("trying to join game...." + guest2.joinGame());
//        System.out.println("trying to join game...." + guest3.joinGame());
//        System.out.println("trying to join game...." + guest4.joinGame());
        //System.out.println(guest3.getScore());
        // System.out.println(host.startGame());


//            System.out.println("giving each player 7 tiles");
//            for(int i = 0; i<7;i++){
//                host.getTile();
//                guest.getTile();
//                guest2.getTile();
//                guest3.getTile();
//            }
//            System.out.println(host.printTiles());
//            System.out.println(guest.printTiles());
//            System.out.println(guest2.printTiles());
//            System.out.println(guest3.printTiles());
//            System.out.println(guest.queryIO("s1.txt","s2.txt","kaka"));//18878
//            System.out.println(guest.challangeIO("s1.txt","s2.txt","18878"));//18878

//            int x,y;
//            boolean ishorizontal;
//
//            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//            System.out.println("word to place");
//            String wordtoplace = myObj.nextLine();

//            System.out.println("x:");// Read user input
//            x = myObj.nextInt();  // Read user input
//            System.out.println("y:");
//            y = myObj.nextInt();  // Read user input
//            System.out.println("ishorizontal:");
//            ishorizontal = myObj.nextBoolean();  // Read user input
//            //System.out.println("word to place: " + wordtoplace +" x " + x + " y " + y + " ishorizontal " + ishorizontal  );  // Output user input
//            System.out.println(guest.placeWord(wordtoplace,x,y,ishorizontal));



        //System.out.println(GM.printPlayers());
        //host.stopGame();

//        guest2.disconnectFromServer();
//        guest3.disconnectFromServer();
//        guest4.disconnectFromServer();

        sleep(1000*3);
        //guest.disconnectFromServer();
        System.out.println("Done!");



    }
}
