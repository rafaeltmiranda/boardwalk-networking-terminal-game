package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.Prompt;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Player {

    private Socket socket;
    private String alias;
    private int points;
    private Prompt prompt;


    public Player(Socket socket){

        this.socket = socket;
        points = 100;

        try {
            prompt = new Prompt(socket.getInputStream(), new PrintStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void addPoints(int points){

        this.points += points;
    }


    void subtractPoints(int points){

        if ((this.points -= points) < 0){
           this.points = 0;
        }
    }


    void setAlias(String alias){

        this.alias = alias;
    }
}
