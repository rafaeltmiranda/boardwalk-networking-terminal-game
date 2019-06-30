package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.string.PasswordInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.phrases.Phrases;
import org.academiadecodigo.whiledlings.boardwalk.utility.ColorTerminal;
import org.academiadecodigo.whiledlings.boardwalk.utility.OutputBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Room{

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
    private int playersInGame;

    Room(String name) {
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
        player.resetLives();
        player.setRoom(this);
        playersInGame++;
        player.inGame = true;

        broadcast(player.getAlias() + " is ready to walk the plank\n");
        broadcast(getPlayerList());

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
            for (Player player : players) {

                if (playersInGame == 0) {
                    onLoosers();
                    return;
                }

                if (!player.inGame){
                    continue;
                }

                System.out.println(player.getLives());
                if (player.getLives() <= 0) {
                    playersInGame--;
                    player.inGame = false;
                    continue;
                }

                refreshScreen(player);
                response = getResponse(player, "Your choice: ");
                verifyResponse(response, player);

                if (endGame) {
                    printWinner(player);
                    sendPlayersToLobby();
                    break;
                }
            }
        }
    }

    private void printWinner(Player player) {
        broadcast(OutputBuilder.clearScreen() + OutputBuilder.winner(player));

        StringInputScanner exitScanner = new StringInputScanner();
        exitScanner.setMessage("\n Press any key to leave the game");

        for (int i = 0; i < players.size(); i++) {
            players.get(i).getPrompt().getUserInput(exitScanner);
        }
    }

    private void verifyResponse(String response, Player player) {

        boolean existLetter = false;
        char[] letters = response.toCharArray();

        if (response.length() == 1) {

            while (alreadyChosen.contains(letters[0])) {

                response = getResponse(player, "Letter already chosen, choice again: ");

                if (response.length() > 1) {
                    verifyResponse(response, player);
                }

                letters = response.toCharArray();
                continue;

            }

            alreadyChosen.add(response.charAt(0));

            for (int i = 0; i < completePhrase.length; i++) {

                if (letters[0] == completePhrase[i]) {
                    playablePhrase[i] = completePhrase[i];
                    existLetter = true;
                }
            }

            if (!existLetter){
                player.subtractLife();
            }

            for (int i = 0; i < completePhrase.length; i++) {

                if (completePhrase[i] == playablePhrase[i]) {
                    continue;
                }

                return;
            }

            endGame = true;
            return;
        }

        for (int i = 0; i < completePhrase.length; i++) {

            if (completePhrase[i] == letters[i]) {
                continue;
            }

            player.subtractLife();
            player.subtractLife();
            return;
        }

        endGame = true;

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

        String names = "Buccaneers in the room:\n";

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


    void run() {

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
        player.resetLives();
        player.inGame = true;

    }

    void broadcast(String message, Player fromPlayer) {

        for (int i = 0; i < players.size(); i++) {

            if (players.get(i).equals(fromPlayer)) {
                continue;
            }

            checkOwner(message, fromPlayer);

            try {
                PrintWriter writer = new PrintWriter(players.get(i).socket.getOutputStream());
                writer.println(ColorTerminal.ANSI_GREEN.getAnsi() + fromPlayer.getAlias() + " -> " +
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

    private void onLoosers () {
        broadcast(OutputBuilder.logo() + OutputBuilder.buildOutput(completePhrase) + OutputBuilder.allLoosers());

        StringInputScanner exitScanner = new StringInputScanner();
        exitScanner.setMessage("\n Press any key to leave the game");

        for (int i = 0; i < players.size(); i++) {
            players.get(i).getPrompt().getUserInput(exitScanner);
        }

        sendPlayersToLobby();
    }


    private void sendPlayersToLobby() {
        for (Player player : players) {

            player.inRoom = false;
            if (!player.equals(roomOwner)){
                Thread thread = new Thread(new Lobby(player.socket));
                thread.start();
            }
        }
    }
}
