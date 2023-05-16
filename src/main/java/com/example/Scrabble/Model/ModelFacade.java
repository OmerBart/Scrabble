package com.example.Scrabble.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ModelFacade {
//    private HostModel hostModel;
//    private GuestModel guestModel;

    public HostModel makeGame(String name){
        HostModel hostModel;
        hostModel = new HostModel(name);
        return hostModel;
    }
    public GuestModel joinGame(String name){
        GuestModel guestModel;
        guestModel = new GuestModel(name);
        return guestModel;
    }


//    public hostGame(String name){
//        hostModel = new HostModel(name);
//        hostModel.connectServer();
//    }
//    public joinGame(String name){
//        guestModel = new GuestModel(name);
//        guestModel.setGuestModel(serverAddress, serverPort);
//    }
//
//    public ModelFacade() {
//        hostModel = new HostModel("null");
//        guestModel = new GuestModel();
//
//    }
//


    //add client handler methods to do with the game here

}
