package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.string.PasswordInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.phrases.Sentences;
import org.academiadecodigo.whiledlings.boardwalk.utility.ColorTerminal;
import org.academiadecodigo.whiledlings.boardwalk.utility.OutputBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class Room {

    private static final int MAX_PLAYERS = 4;
    boolean passwordProtected;
    private ArrayList<Player> players;
    private char[] completeSentence;
    private String completeSentenceString;
    private char[] playableSentence;
    private Set<Character> alreadyChosen;
    private String name;
    private Player roomOwner;
    private boolean closed;
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

        if (closed) {
            notify.setMessage("Sorry, the room " + name + " is closed " +
                    ":(\nEnter any key to back for menu:");
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

        return password.equals(this.password);
    }


    private void start() {

        String response;
        getRandomPhrase();
        boolean correctGuess;

        while (!endGame) {
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);

                if (playersInGame == 0) {
                    onLosers();
                    return;
                }

                if (!player.inGame) {
                    continue;
                }

                if (player.getLives() <= 0) {
                    playersInGame--;
                    player.inGame = false;
                    continue;
                }

                refreshScreen(player);
                response = getResponse(player, "Your choice: ");
                correctGuess = verifyResponse(response, player);

                if (correctGuess) {
                    i--;
                }

                if (endGame) {
                    printWinner(player);
                    sendPlayersToLobby();
                    break;
                }
            }
        }
    }

    private void printWinner(Player player) {
        broadcast(OutputBuilder.clearScreen() + OutputBuilder.winner(player, completeSentenceString));

        StringInputScanner exitScanner = new StringInputScanner();
        exitScanner.setMessage("\n Press any key to leave the game");

        for (Player player1 : players) {
            player1.getPrompt().getUserInput(exitScanner);
        }
    }

    private boolean verifyResponse(String response, Player player) {

        boolean existLetter = false;
        char[] letters = response.toCharArray();

        if (response.length() == 1) {

            while (alreadyChosen.contains(letters[0])) {

                response = getResponse(player, "Letter already chosen, choice again: ");

                if (response.length() > 1) {
                    verifyResponse(response, player);
                }

                letters = response.toCharArray();

            }

            alreadyChosen.add(response.charAt(0));

            for (int i = 0; i < completeSentence.length; i++) {

                if (letters[0] == completeSentence[i]) {
                    playableSentence[i] = completeSentence[i];
                    existLetter = true;
                }
            }

            if (!existLetter) {
                player.subtractLife();
            }

            for (int i = 0; i < completeSentence.length; i++) {

                if (completeSentence[i] == playableSentence[i]) {
                    continue;
                }

                return existLetter;
            }

            endGame = true;
            return existLetter;
        }

        if (completeSentence.length != letters.length) {
            player.subtractLife();
            player.subtractLife();
            return existLetter;
        }

        for (int i = 0; i < completeSentence.length; i++) {

            if (completeSentence[i] == letters[i]) {
                continue;
            }

            player.subtractLife();
            player.subtractLife();
            return existLetter;
        }

        endGame = true;
        return existLetter;

    }

    private void refreshScreen(Player player) {

        OutputBuilder.broadcastLogo(players);
        String phrase = OutputBuilder.buildOutput(playableSentence);
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

        StringBuilder names = new StringBuilder("Buccaneers in the room:\n");

        for (Player player : players) {
            names.append(player.getAlias()).append("\n");
        }

        names.append("\n").append("While waiting you can chat with other players in the room. The game will " +
                "automatically start when the player ").append(players.get(0).getAlias())
                .append(" type \"start\" in the chat").append("\n");

        return names.toString();
    }

    private void getRandomPhrase() {

        Sentences sentence = Sentences.values()[(int) (Math.random() * Sentences.values().length)];
        completeSentenceString = sentence.getSentence();
        completeSentence = sentence.getSentenceAsCharArray();
        playableSentence = new char[completeSentence.length];

        for (int i = 0; i < completeSentence.length; i++) {
            playableSentence[i] = completeSentence[i] == ' ' ? ' ' : '_';
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

        player.sendContent("Waiting for other players\n");

    }

    void broadcast(String message, Player fromPlayer) {

        for (Player player : players) {

            if (player.equals(fromPlayer)) {
                continue;
            }

            checkOwner(message, fromPlayer);

            player.sendContent(ColorTerminal.ANSI_GREEN.getAnsi() + fromPlayer.getAlias() + " -> " +
                    ColorTerminal.ANSI_RESET.getAnsi() + message);
        }
    }

    void broadcast(String message) {

        for (Player player : players) {

            player.sendContent(message);
        }
    }

    void removePlayer(Player player) {
        players.remove(player);
        player.inRoom = false;
    }

    private synchronized void checkOwner(String message, Player player) {

        if (player.equals(roomOwner)) {
            if ("start".equals(message)) {
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

        return players.contains(player);
    }

    int getNumberOfPlayers() {
        return players.size();
    }

    private void onLosers() {
        broadcast(OutputBuilder.logo() + OutputBuilder.buildOutput(completeSentence) +
                OutputBuilder.allLoosers(completeSentenceString));

        StringInputScanner exitScanner = new StringInputScanner();
        exitScanner.setMessage("\n Press any key and \"Enter\" to leave the game");

        for (Player player : players) {
            player.getPrompt().getUserInput(exitScanner);
        }

        sendPlayersToLobby();
    }


    private void sendPlayersToLobby() {
        for (Player player : players) {

            player.inRoom = false;
            if (!player.equals(roomOwner)) {
                Thread thread = new Thread(new Lobby(player.getSocket()));
                thread.start();
            }
        }
    }
}
