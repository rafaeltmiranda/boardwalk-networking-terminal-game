package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.PasswordInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.game.Room;
import org.academiadecodigo.whiledlings.boardwalk.utility.Closer;
import org.academiadecodigo.whiledlings.boardwalk.utility.ColorTerminal;
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
        OutputBuilder.drawLogo(player.socket);
        room.joinRoom(player);

        if (room.checkIfPlayerInRoom(player)) {
            Thread thread = new Thread(player);
            thread.start();
        }
    }

    private void roomListMenu() {

        OutputBuilder.drawLogo(player.socket);

        String[] options = getRoomsAsString();

        MenuInputScanner menuRoomList = new MenuInputScanner(options);
        menuRoomList.setMessage("Choose a pirate room that suits you...");

        int answerIndex = player.getPrompt().getUserInput(menuRoomList);

        if (answerIndex == options.length) {
            return;
        }
        options[answerIndex - 1] = options[answerIndex - 1].
                substring(0, options[answerIndex - 1].indexOf('[') - 5);

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
                optionsString += room.getName() + "     " +
                        (room.passwordProtected ? "[PASSWORD PROTECTED]" :
                        "[OPEN]") + " - " + room.getNumberOfPlayers() +
                        " players in room" + "|";
            }
        }
        optionsString += "I don't want any of those";

        String options[] = optionsString.split("\\|");

        return options;
    }


    private void createRoom(){

        String password = null;
        StringInputScanner roomNameQuestion = new StringInputScanner();
        roomNameQuestion.setMessage("\nWhat do you want to name your room, old salt?\n");

        password = passwordProtect();

        OutputBuilder.drawLogo(player.socket);

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

        OutputBuilder.drawLogo(player.socket);

        Room room = new Room(roomName);
        rooms.add(room);
        room.addOwnerPlayer(player);

        if (password != null){
            room.setPassword(password);
            room.setPasswordProtectedTrue();
        }

        Thread playerThread = new Thread(player);            // TODO: 29/06/2019 check this
        playerThread.start();

        Thread roomThread = new Thread(room);
        roomThread.start();
    }


    private String passwordProtect(){
        String[] passwordOptions = {"Yes", "No"};
        int answer;
        String password = "";
        String paswordRepeat = " ";

        MenuInputScanner passwordMenu = new MenuInputScanner(passwordOptions);
        passwordMenu.setMessage("Do you want to password protect your room?");
        PasswordInputScanner password1 = new PasswordInputScanner();
        password1.setMessage("Enter password\n");
        PasswordInputScanner password2 = new PasswordInputScanner();
        password2.setMessage("Repeat password\n");

        OutputBuilder.drawLogo(player.socket);

        answer = player.getPrompt().getUserInput(passwordMenu);

        if (answer == 1){

            while (!(password.equals(paswordRepeat))){

                password = player.getPrompt().getUserInput(password1);
                paswordRepeat = player.getPrompt().getUserInput(password2);
            }

            return password;
        }

        return null;
    }


    @Override
    public void run() {

        chooseAlias();
        menu();

    }

    private void chooseAlias() {

        PrintWriter writer = null;
        String alias = null;

        try {

            writer = new PrintWriter(player.socket.getOutputStream());
            writer.println(OutputBuilder.logo());
            writer.println("What is you pirate name?");
            writer.flush();

            alias = player.inputStream.readLine();

            if (alias.length() > 26){
                alias = alias.substring(0,25);
            }

            player.setAlias(alias);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void menu() {
        String[] options = {"Join a room.", "Create a room.", "Check instructions."};
        MenuInputScanner menuScanner = new MenuInputScanner(options);
        menuScanner.setMessage("Ahoy! Do you want to join a room or create a new room?");
        PrintWriter printWriter = null;

        while (!player.inRoom) {

            OutputBuilder.drawLogo(player.socket);

            int answerIndex = player.getPrompt().getUserInput(menuScanner);

            if (answerIndex == 1) {
                roomListMenu();
                continue;
            }

            if (answerIndex == 2) {
                createRoom();
            }

            if (answerIndex == 3) {
                instructions();
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

    private void instructions () {

        String instructions = ColorTerminal.ANSI_BLUE.getAnsi() + ColorTerminal.ANSI_WHITE_BACKGROUND.getAnsi() + "Ahoy, Matey!\n" +
                "Welcome to the Board walk game! To be successful, follow the instructions below.\n" +
                ColorTerminal.ANSI_GREE.getAnsi() + ColorTerminal.ANSI_BLACK_BACKGROUND.getAnsi() +
                "1. When waiting in a room, the room owner may type \"start\" to begin the game at any time\n" +
                "2. In turns, each Buccaneer will try to guess a letter or a whole expression.\n" +
                "3. Guessing a letter:\n" + ColorTerminal.ANSI_CYAN.getAnsi() +
                "  -> Right: you have the chance to take another guess.\n" +
                "  -> Wrong: you are one step closer to sinking the ship!\n" + ColorTerminal.ANSI_GREE.getAnsi() +
                "4. Guessing the whole expression:\n" + ColorTerminal.ANSI_CYAN.getAnsi() +
                "  -> Right: you found the coffer. Enjoy your doubloons!\n" +
                "  -> Wrong: you are two steps closer to sinking you ship. Watch out!\n" + ColorTerminal.ANSI_GREE.getAnsi() +
                "5. The winner is the Bucko who guesses the last letter or the whole expression.\n" +
                "Be wise or be reckless, just don't feed the fish!" + ColorTerminal.ANSI_RESET;

        String [] menuInstructionsOptions = {"Go back."};
        MenuInputScanner menuInstructions = new MenuInputScanner(menuInstructionsOptions);
        menuInstructions.setMessage(instructions);


        OutputBuilder.drawLogo(player.socket);
        int answerIndex = player.getPrompt().getUserInput(menuInstructions);

        if (answerIndex == 1) {
            menu();
        }
    }


}
