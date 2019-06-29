package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.whiledlings.boardwalk.phrases.Phrases;

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
    }

    public void joinRoom(Player player){

        if (closed){
            StringInputScanner notify = new StringInputScanner();
            notify.setMessage("Sorry, the room " + name + " is closed :(\nEnter any key to back for menu:");
            player.getPrompt().getUserInput(notify);
            return;
        }

        players.add(player);

        if (players.size() == MAX_PLAYERS){
            closed = true;
        }
    }

    private void waitStart(Player player){

        while (!closed){

            if (!player.equals(roomOwner)) {

                StringInputScanner notify = new StringInputScanner();

                notify.setMessage("Waiting for more players ...\n"
                        + "Players in room:" + getPlayerList());
                player.getPrompt().getUserInput(notify);
                continue;
            }

            String[] options = {"Start", "Back to menu"};
            MenuInputScanner start = new MenuInputScanner(options);

            start.setMessage("Players in room:" + getPlayerList()
                    + "Start game?");

            if (player.getPrompt().getUserInput(start) == 1){
                closed = true;
            }
        }

        start();

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

    }

    public void addOwnerPlayer(Player player){

        roomOwner = player;
        players.add(player);

    }
}
