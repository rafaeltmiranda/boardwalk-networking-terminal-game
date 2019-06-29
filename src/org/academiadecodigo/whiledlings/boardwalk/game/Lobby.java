package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.game.Room;

import java.net.Socket;
import java.util.ArrayList;

public class Lobby implements Runnable{

    private static ArrayList<Room> rooms;
    private Player player;

    public Lobby (Socket playerSocket) {
        this.player = new Player(playerSocket);
    }


    public void joinRoom(){}


    public void createRoom(){}

    @Override
    public void run() {

        menu();

    }

    private void menu() {
        String [] options = {"Join a room." , "Create a room."};
        MenuInputScanner menuScanner = new MenuInputScanner(options);
        menuScanner.setMessage("Do you want to join a room or create a new room?");

        int answerIndex = player.getPrompt().getUserInput(menuScanner);

        if (answerIndex == 1) {
            joinRoom();
        }

        if (answerIndex == 2) {
            createRoom();
        }
    }
}
