package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.game.Room;
import org.academiadecodigo.whiledlings.boardwalk.utility.Closer;
import org.academiadecodigo.whiledlings.boardwalk.utility.OutputBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Lobby implements Runnable{

    private static ArrayList<Room> rooms = new ArrayList<>();
    private Player player;

    public Lobby (Socket playerSocket) {
        this.player = new Player(playerSocket);
    }

    private void joinRoom(Room room){

        room.joinRoom(player);
        Thread thread = new Thread(player);
        thread.start();

    }

    private void roomListMenu() {

        String[] options = getRoomsAsString();

        MenuInputScanner menuRoomList = new MenuInputScanner(options);
        menuRoomList.setMessage("Choose a pirate room that suits you...");

        int answerIndex = player.getPrompt().getUserInput(menuRoomList);

        if (answerIndex == options.length) {
            return;
        }

        Room selectedRoom = null;

        for (Room room : rooms) {
            if (room.getName().equals(options[answerIndex - 1])) {
                selectedRoom = room;
            }
        }
        joinRoom(selectedRoom);
    }


    private String[] getRoomsAsString(){

        String optionsString = "";

        for (Room room : rooms) {
            if (!room.isClosed()) {
                optionsString += room.getName() + "|";
            }
        }
        optionsString += "I don't want any of those";

        String options[] = optionsString.split("\\|");

        return options;
    }


    private void createRoom(){
        StringInputScanner roomNameQuestion = new StringInputScanner();
        roomNameQuestion.setMessage("What do you want to name your room, old salt?\n");

        boolean differentName = false;
        String roomName = null;
        PrintWriter writer = null;

        try {
             writer = new PrintWriter(player.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!differentName) {
            roomName = player.getPrompt().getUserInput(roomNameQuestion);


            if (validName(roomName)) {
                differentName = true;
                continue;
            }

            writer.println("Room name already in use\n");
            writer.flush();

        }

        Room room = new Room(roomName);
        rooms.add(room);
        room.addOwnerPlayer(player);

        Thread playerThread = new Thread(player);            // TODO: 29/06/2019 check this
        playerThread.start();

        Thread roomThread = new Thread(room);
        roomThread.start();



    }

    @Override
    public void run() {

        chooseAlias();
        menu();

    }

    private void chooseAlias() {

        PrintWriter writer = null;

        try {

            writer = new PrintWriter(player.socket.getOutputStream());
            writer.println("What is you pirate name?");
            writer.flush();
            player.setAlias(player.inputStream.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu() {
        String[] options = {"Join a room.", "Create a room."};
        MenuInputScanner menuScanner = new MenuInputScanner(options);
        menuScanner.setMessage("Ahoy! Do you want to join a room or create a new room?");
        PrintWriter printWriter = null;

        while (!player.inRoom) {

            try {
                printWriter = new PrintWriter(player.socket.getOutputStream());
                printWriter.print(OutputBuilder.logo());
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int answerIndex = player.getPrompt().getUserInput(menuScanner);

            if (answerIndex == 1) {
                roomListMenu();
                continue;
            }

            if (answerIndex == 2) {
                createRoom();
            }
        }
    }

    private boolean validName (String roomName) {

        for (Room room : rooms) {

            if (room.getName().equals(roomName)) {
                return false;
            }
        }
        return true;
    }

    public static void removeRoom (Room room) {
        rooms.remove(room);
    }
}
