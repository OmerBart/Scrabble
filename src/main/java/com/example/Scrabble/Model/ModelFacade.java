package com.example.Scrabble.Model;

import com.example.Scrabble.tmp.GuestModel;
import com.example.Scrabble.tmp.HostModel;

public class ModelFacade {
    public HostModel hostModel;

    public void makeGame(String name) {
        hostModel = new HostModel(name);
    }

    public GuestModel joinGame(String name) {
        GuestModel guestModel;
        guestModel = new GuestModel(name, hostModel);
        return guestModel;
    }

    // public hostGame(String name){
    // hostModel = new HostModel(name);
    // hostModel.connectServer();
    // }
    // public joinGame(String name){
    // guestModel = new GuestModel(name);
    // guestModel.setGuestModel(serverAddress, serverPort);
    // }
    //
    // public ModelFacade() {
    // hostModel = new HostModel("null");
    // guestModel = new GuestModel();
    //
    // }
    //

    // add client handler methods to do with the game here

}
