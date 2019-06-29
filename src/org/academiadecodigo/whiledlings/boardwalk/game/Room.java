package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.phrases.Phrases;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

public class Room implements Runnable{

    public static int MAX_PLAYERS = 5;

    private ArrayList<Player> players;
    private char[] completePhrase;
    private char[] phrase;
    private Set<Character> alreadyChosen;
    private String name;
    private Player roomOwner;
    private boolean closed;

    public Room (String name){
        this.name = name;
        players = new ArrayList<>();
    }

    public void joinRoom(Player player){

        if (closed){
            StringInputScanner notify = new StringInputScanner();
            notify.setMessage("Sorry, the room " + name + " is closed :(\nEnter any key to back for menu:");
            player.getPrompt().getUserInput(notify);
            return;
        }

        players.add(player);
        player.inRoom = true;
        player.setRoom(this);

        broadcast("Are you ready to walk the plank.", player);

        if (players.size() == MAX_PLAYERS){
            closed = true;
        }

    }

    private void start() {
        System.out.println("Start");
    }

    private String getPlayerList() {

        String names = "";

        for (Player player : players){
            names = names + player.alias + "\n";
        }

        return names;
    }

    private void getRandomPhrase(){
        completePhrase = Phrases.ENCAPSULATION.getPhraseAsCharArray();
    }

    public String getName() {
        return name;
    }

    public boolean isClosed() {
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

    public void addOwnerPlayer(Player player){

        roomOwner = player;
        players.add(player);
        player.inRoom = true;
        player.setRoom(this);

    }

    public void broadcast(String message, Player fromPlayer){

        for (int i = 0; i < players.size(); i++){

            if (players.get(i).equals(fromPlayer)){
                continue;
            }

            checkOwner(message, fromPlayer);

            try {
                PrintWriter writer = new PrintWriter(players.get(i).socket.getOutputStream());
                writer.println(fromPlayer.alias + " -> " + message);
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
}
