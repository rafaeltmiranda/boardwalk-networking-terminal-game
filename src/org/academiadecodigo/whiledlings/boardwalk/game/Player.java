package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.whiledlings.boardwalk.utility.Closer;

import java.io.*;
import java.net.Socket;

public class Player{

    private static final int MAXIMUM_LIVES = 5;

    private Socket socket;
    private String alias;
    private int lives;
    private Prompt prompt;
    boolean inRoom;
    BufferedReader inputStream = null;
    private Room room;
    boolean inGame;

    Player(Socket socket){

        inRoom = false;
        this.socket = socket;
        lives = 5;

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


    void subtractLife(){
        lives--;
    }


    void setAlias(String alias){

        this.alias = alias;
    }


    void setRoom(Room room){

        this.room = room;
    }


    void listen() {

        String message = null;

        while (!room.isClosed()) {

            try {
                message = inputStream.readLine();

                if (message == null){
                    Closer.close(socket);
                    room.removePlayer(this);
                    return;
                }

                room.broadcast(message, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public int getLives() {
        return lives;
    }

    public String getAlias() {
        return alias;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getMaximumLives() {
        return MAXIMUM_LIVES;
    }

    void resetLives(){
        lives = MAXIMUM_LIVES;
    }
}
