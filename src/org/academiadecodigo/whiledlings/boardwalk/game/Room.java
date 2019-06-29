package org.academiadecodigo.whiledlings.boardwalk.game;

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

    public Room (String name, Player player){
        this.name = name;
        this.roomOwner = player;
        players.add(player);
    }

    public void joinRoom(){

    }

    private void getRandomPhrase(){

    }

    public String getName() {
        return name;
    }
}
