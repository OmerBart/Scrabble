// package com.example.Scrabble;

// import java.util.Observable;

// import com.example.Scrabble.Model.Player.GuestPlayer;

// public class GuestFacade extends Observable {

//     private volatile GuestPlayer guestPlayer;
//     private static GuestFacade guestFacadeInstance = null;

//     public static GuestFacade get(String playerName) {
//         if (guestFacadeInstance == null) {
//             guestFacadeInstance = new GuestFacade(playerName);
//         }
//         return guestFacadeInstance;
//     }

//     public GuestFacade(String playerName) {
//         guestPlayer = new GuestPlayer(playerName);
//     }

//     @Override
//     public void notifyObservers(Object arg) {
//         setChanged();
//         super.notifyObservers(arg);
//     }
// }
