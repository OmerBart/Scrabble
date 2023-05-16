package com.example.Scrabble.Model;

public class GuestModel {
    private String name;
    private String serverAddress;
    private int serverPort;

    public void setGuestModel(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void sendRequestToHost(String query){
        //send query to host
    }


    public GuestModel() {
        name = "Guest";
    }
    public GuestModel(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }


}
