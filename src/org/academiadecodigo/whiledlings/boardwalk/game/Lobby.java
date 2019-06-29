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

public class Lobby implements Runnable{

    private static ArrayList<Room> rooms = new ArrayList<>();
    private Player player;

    public Lobby (Socket playerSocket) {
        this.player = new Player(playerSocket);
    }

    private void joinRoom(Room room){
        room.joinRoom(player);
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
            if (room.getName() == options[answerIndex - 1]) {
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
        roomNameQuestion.setMessage("What do you want to name your room, old salt?");

        boolean differentName = false;
        String roomName = null;

        while (!differentName) {
            System.out.println("in while");
            roomName = player.getPrompt().getUserInput(roomNameQuestion);


            if (validName(roomName)) {
                differentName = true;
            }


        }

        System.out.println("out of while");
        rooms.add(new Room(roomName, player));
        System.out.println("Number of rooms: " + rooms.size());
        System.out.println(rooms.get(0).getName());
    }

    @Override
    public void run() {

        menu();

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

        System.out.println("in valid name");

        for (Room room : rooms) {

            if (room.getName().equals(roomName)) {
                System.out.println("name was not valid");
                return false;
            }
        }
        System.out.println("name was valid");
        return true;
    }

    public static void removeRoom (Room room) {
        rooms.remove(room);
    }
}
