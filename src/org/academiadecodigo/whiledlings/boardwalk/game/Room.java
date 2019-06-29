package org.academiadecodigo.whiledlings.boardwalk.game;

import org.academiadecodigo.whiledlings.boardwalk.phrases.Phrases;

import java.util.ArrayList;
import java.util.Set;

public class Room {

    public static int MAX_PLAYERS = 5;

    private ArrayList<Player> players;
    private char[] completePhrase;
    private char[] phrase;
    private Set<Character> alreadyChosen;
    private String name;
    private Player roomOwner;
    private boolean closed;

    public Room (String name, Player player){
        this.name = name;
        this.roomOwner = player;
        players.add(player);
    }

    public void joinRoom(Player player){

        if (closed){
            return;
        }

        players.add(player);

        if (players.size() == MAX_PLAYERS){
            closed = true;
        }
    }

    private void getRandomPhrase(){
        completePhrase = Phrases.ENCAPSULATION.getPhraseAsCharArray();
    }

    public String getName() {
        return name;
    }
}
