package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.game.Room;

import java.net.Socket;
import java.util.ArrayList;

public class Lobby implements Runnable{

    private static ArrayList<Room> rooms = new ArrayList<>();
    private Player player;

    public Lobby (Socket playerSocket) {
        this.player = new Player(playerSocket);
    }

    public void joinRoom(Room room){
        room.joinRoom(player);
    }

    public void roomList() {

        String optionsString = "";

        for (Room room : rooms) {
            if (!room.isClosed()) {
                optionsString += room.getName() + " ";
            }
            String options[] = optionsString.split(" ");
        }
        optionsString += "I don't want any of then";

        String options[] = optionsString.split(" ");

        MenuInputScanner menuRoomList = new MenuInputScanner(options);
        menuRoomList.setMessage("Chose a pirate room who suits you...");

        int answerIndex = player.getPrompt().getUserInput(menuRoomList);

        if (answerIndex == options.length - 1) {
            return;
        }

        Room selectedRoom = null;

        for (Room room : rooms) {
            if (room.getName() == options[answerIndex - 1]) {
                selectedRoom = room;
            }
        }

        joinRoom(selectedRoom);
    }


    public void createRoom(){
        StringInputScanner roomNameQuestion = new StringInputScanner();
        roomNameQuestion.setMessage("What do you want to name your room, old salt?");

        String roomName = player.getPrompt().getUserInput(roomNameQuestion);

        Room roomCreated = new Room(roomName, player);

        rooms.add(roomCreated);
    }

    @Override
    public void run() {

        menu();

    }

    private void menu() {
        String [] options = {"Join a room." , "Create a room."};
        MenuInputScanner menuScanner = new MenuInputScanner(options);
        menuScanner.setMessage("Ahoy! Do you want to join a room or create a new room?");

        int answerIndex = player.getPrompt().getUserInput(menuScanner);

        if (answerIndex == 1) {
            joinRoom();
        }

        if (answerIndex == 2) {
            createRoom();
        }
    }

    public static void removeRoom (Room room) {
        rooms.remove(room);
    }
}
