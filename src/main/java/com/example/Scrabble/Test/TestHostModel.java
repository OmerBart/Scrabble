package com.example.Scrabble.Test;


import com.example.Scrabble.Model.HostModel;

// import java.util.Random;

public class TestHostModel {

    public static void main(String[] args) {
        HostModel h = new HostModel("Hadar");
        System.out.println("Host created");
        // Random r=new Random();
        // int port=6000+r.nextInt(1000);
        h.connectServer();
        

    }


}
