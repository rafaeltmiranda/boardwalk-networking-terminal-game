package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.string.PasswordInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.phrases.Phrases;
import org.academiadecodigo.whiledlings.boardwalk.utility.OutputBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

public class Room implements Runnable{

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

    public Room (String name){
        this.name = name;
        players = new ArrayList<>();
    }

    void joinRoom(Player player){

        StringInputScanner notify = new StringInputScanner();
        notify.setMessage("Sorry, the room " + name + " is closed :(\nEnter any key to back for menu:");

        if (closed){
            player.getPrompt().getUserInput(notify);
            return;
        }

        if (passwordProtected){
            if (!checkPassword(player)){
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

        if (players.size() == MAX_PLAYERS){
            closed = true;
        }

    }

    private boolean checkPassword(Player player){
        PasswordInputScanner passwordInputScanner = new PasswordInputScanner();
        passwordInputScanner.setMessage("Enter password\n");
        String password = null;

        password = player.getPrompt().getUserInput(passwordInputScanner);

        if (password.equals(this.password)){
            return true;
        }

        return false;
    }

    private void start() {
        System.out.println("Start");
    }

    private String getPlayerList() {

        String names = "";

        for (Player player : players){
            names = names + player.getAlias() + "\n";
        }

        return names;
    }

    private void getRandomPhrase(){

        completePhrase = Phrases.values()[(int) (Math.random() * Phrases.values().length)].getPhraseAsCharArray();
        playablePhrase = new char[completePhrase.length];

        for (int i = 0; i < completePhrase.length ; i++) {
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

    void addOwnerPlayer(Player player){

        roomOwner = player;
        players.add(player);
        player.inRoom = true;
        player.setRoom(this);

    }

    void broadcast(String message, Player fromPlayer){

        for (int i = 0; i < players.size(); i++){

            if (players.get(i).equals(fromPlayer)){
                continue;
            }

            checkOwner(message, fromPlayer);

            try {
                PrintWriter writer = new PrintWriter(players.get(i).socket.getOutputStream());
                writer.println(OutputBuilder.ANSI_GREEN + fromPlayer.getAlias() + " -> " +
                        OutputBuilder.ANSI_RESET + message);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void broadcast(String message) {

        for (int i = 0; i < players.size(); i++){

            try {
                PrintWriter writer = new PrintWriter(players.get(i).socket.getOutputStream());
                writer.println(message);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void removePlayer(Player player){
        players.remove(player);
        player.inRoom = false;
    }

    private synchronized void checkOwner(String message, Player player){

        if (player.equals(roomOwner)){
            if (message.equals("start")){
                closed = true;
                notifyAll();
            }
        }
    }

    void setPasswordProtectedTrue(){
        passwordProtected = true;
    }

    void setPassword(String password) {
        this.password = password;
    }

    boolean checkIfPlayerInRoom(Player player){

        if (players.contains(player)){
            return true;
        }
        return false;
    }

    int getNumberOfPlayers(){
        return players.size();
    }
}
