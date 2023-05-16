package com.example.Scrabble.Model;

public class GuestModel {
    private String name;
    private HostModel myHost;

    public GuestModel(String name, HostModel host){
        this.name = name;
        this.myHost = host;
    }

    public void sendRequestToHost(String query){
        myHost.BookActions(query);
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }


}
