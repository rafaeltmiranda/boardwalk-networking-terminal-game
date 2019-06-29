package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.whiledlings.boardwalk.utility.Closer;

import java.io.*;
import java.net.Socket;

public class Player implements Runnable{

    Socket socket;
    String alias;
    private int points;
    private Prompt prompt;
    boolean inRoom;
    private BufferedReader inputStream = null;
    private Room room;

    public Player(Socket socket){

        inRoom = false;
        this.socket = socket;
        points = 100;

        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            prompt = new Prompt(socket.getInputStream(), new PrintStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Prompt getPrompt () {
        return prompt;
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

    @Override
    public void run() {

        listen();
    }

    void setRoom(Room room){

        this.room = room;
    }

    private void listen() {

        String message = null;

        while (true) {

            try {
                message = inputStream.readLine();

                if (message == null){        // TODO: 29/06/2019 check if this is right 
                    Closer.close(socket);
                }

                room.broadcast(message, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
