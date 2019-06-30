package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.string.PasswordInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.phrases.Phrases;
import org.academiadecodigo.whiledlings.boardwalk.utility.ColorTerminal;
import org.academiadecodigo.whiledlings.boardwalk.utility.OutputBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Room implements Runnable {

    private static final int MAX_PLAYERS = 5;

    private ArrayList<Player> players;
    private char[] completePhrase;
    private char[] playablePhrase;
    private Set<Character> alreadyChosen;
    private String name;
    private Player roomOwner;
    private boolean closed;
    boolean passwordProtected;
    private String password;
    private boolean endGame;

    public Room(String name) {
        this.name = name;
        players = new ArrayList<>();
        alreadyChosen = new HashSet<>();
    }

    void joinRoom(Player player) {

        StringInputScanner notify = new StringInputScanner();
        notify.setMessage("Sorry, the room " + name + " is closed :(\nEnter any key to back for menu:");

        if (closed) {
            player.getPrompt().getUserInput(notify);
            return;
        }

        if (passwordProtected) {
            if (!checkPassword(player)) {
                notify.setMessage("Wrong password, you fresh water sailor\n" +
                        "Enter any key to go back\n");
                player.getPrompt().getUserInput(notify);
                return;
            }
        }

        players.add(player);
        player.inRoom = true;
        player.setRoom(this);

        broadcast(player.getAlias() + " is ready to walk the plank");

        synchronized (this) {
            if (players.size() == MAX_PLAYERS) {
                closed = true;
                notifyAll();
            }
        }

    }

    private boolean checkPassword(Player player) {
        PasswordInputScanner passwordInputScanner = new PasswordInputScanner();
        passwordInputScanner.setMessage("Enter password\n");
        String password = null;

        password = player.getPrompt().getUserInput(passwordInputScanner);

        if (password.equals(this.password)) {
            return true;
        }

        return false;
    }

    private void start() {

        String response;
        getRandomPhrase();

        while (!endGame) {

            for (int i = 0; i < players.size(); i++) {

                refreshScreen(players.get(i));
                response = getResponse(players.get(i), "Your choice: ");
                verifyResponse(response, players.get(i));

                if (endGame) {
                    printWinner(players.get(i));
                    break;
                }
            }
        }
    }

    private void printWinner(Player player) {


    }

    private void verifyResponse(String response, Player player) {

        boolean existLetter = true;

        if (response.length() == 1) {

            while (existLetter) {

                if (alreadyChosen.contains(response)) {
                    getResponse(player, "Letter already chosen, choice again: ");
                    continue;
                }

                alreadyChosen.add(response.charAt(0));
                existLetter = false;
            }

            char letter = response.charAt(0);
            for (int i = 0; i < completePhrase.length; i++) {

                if (letter == completePhrase[i]) {
                    playablePhrase[i] = completePhrase[i];
                }
            }

            return;
        }

        if (response.toCharArray().equals(completePhrase)) endGame = true;

    }

    private void refreshScreen(Player player) {

        OutputBuilder.broadcastLogo(players);
        String phrase = OutputBuilder.buildOutput(playablePhrase);
        broadcast(phrase);
        OutputBuilder.ship(players);
        broadcast("Wait " + player.getAlias() + " play.", player);

    }

    private String getResponse(Player player, String message) {

        String response;
        StringInputScanner question = new StringInputScanner();
        question.setMessage(message);

        response = player.getPrompt().getUserInput(question);
        response = response.toUpperCase();

        return response;

    }

    private String getPlayerList() {

        String names = "";

        for (Player player : players) {
            names = names + player.getAlias() + "\n";
        }

        return names;
    }

    private void getRandomPhrase() {

        completePhrase = Phrases.values()[(int) (Math.random() * Phrases.values().length)].getPhraseAsCharArray();
        playablePhrase = new char[completePhrase.length];

        for (int i = 0; i < completePhrase.length; i++) {
            playablePhrase[i] = completePhrase[i] == ' ' ? ' ' : '_';
        }
    }

    String getName() {
        return name;
    }

    boolean isClosed() {
        return closed;
    }

    @Override
    public void run() {

        synchronized (this) {
            while (!closed) {

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        start();
    }

    void addOwnerPlayer(Player player) {

        roomOwner = player;
        players.add(player);
        player.inRoom = true;
        player.setRoom(this);

    }

    void broadcast(String message, Player fromPlayer) {

        for (int i = 0; i < players.size(); i++) {

            if (players.get(i).equals(fromPlayer)) {
                continue;
            }

            checkOwner(message, fromPlayer);

            try {
                PrintWriter writer = new PrintWriter(players.get(i).socket.getOutputStream());
                writer.println(ColorTerminal.ANSI_GREE.getAnsi() + fromPlayer.getAlias() + " -> " +
                        ColorTerminal.ANSI_RESET.getAnsi() + message);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void broadcast(String message) {

        for (int i = 0; i < players.size(); i++) {

            try {
                PrintWriter writer = new PrintWriter(players.get(i).socket.getOutputStream());
                writer.println(message);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void removePlayer(Player player) {
        players.remove(player);
        player.inRoom = false;
    }

    private synchronized void checkOwner(String message, Player player) {

        if (player.equals(roomOwner)) {
            if (message.equals("start")) {
                closed = true;
                notifyAll();
            }
        }
    }

    void setPasswordProtectedTrue() {
        passwordProtected = true;
    }

    void setPassword(String password) {
        this.password = password;
    }

    boolean checkIfPlayerInRoom(Player player) {

        if (players.contains(player)) {
            return true;
        }
        return false;
    }

    int getNumberOfPlayers() {
        return players.size();
    }
}
