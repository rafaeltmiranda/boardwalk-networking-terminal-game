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

        if (players.size() == MAX_PLAYERS){
            closed = true;
        }

    }

    private void waitStart(){

        while (!closed) {

            for (int i = 0; i < players.size(); i++) {
                if (!players.get(i).equals(roomOwner)) {

                    StringInputScanner notify = new StringInputScanner();

                    notify.setMessage("Waiting for more players ...\n"
                            + "Players in room:" + getPlayerList());
                    players.get(i).getPrompt().getUserInput(notify);
                    continue;
                }

                String[] options = {"Start", "Back to menu"};
                MenuInputScanner start = new MenuInputScanner(options);

                start.setMessage("Players in room:" + getPlayerList()
                        + "Start game?");

                if (players.get(i).getPrompt().getUserInput(start) == 1) {
                    closed = true;
                }
            }
        }
    }

    private void start() {
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

        waitStart();

    }

    public void addOwnerPlayer(Player player){

        roomOwner = player;
        players.add(player);
        player.inRoom = true;
        player.setRoom(this);

    }

    public void broadcast(String message, Player player){

        for (int i = 0; i < players.size(); i++){

            if (players.get(i).equals(player)){
                continue;
            }

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
}
