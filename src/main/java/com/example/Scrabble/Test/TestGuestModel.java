package com.example.Scrabble.Test;

import com.example.Scrabble.Model.GuestModel;
import com.example.Scrabble.Model.HostModel;

public class TestGuestModel {

    public static void main(String[] args) {
        HostModel h = new HostModel("Hadar");
        h.connectServer();
        GuestModel g = new GuestModel("Hadar", h);
        
        System.out.println("Guest created");
        g.sendRequestToHost("Q,s1.txt,s2.txt,19393");
        g.sendRequestToHost("Q,s1.txt,s2.txt,29393");
        g.sendRequestToHost("Q,s1.txt,s2.txt,19393");
        g.sendRequestToHost("Q,s1.txt,s2.txt,19393");
    }
}
