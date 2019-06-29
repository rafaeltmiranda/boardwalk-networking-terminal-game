package org.academiadecodigo.whiledlings.boardwalk.phrases;

public enum Phrases {

    ENCAPSULATION("ENCAPSULATION CAN BE USED TO HIDE DATA MEMBERS AND MEMBER FUNCTIONS");


    private String phrase;

    Phrases(String phrase){
        this.phrase = phrase;
    }


    public char[] getPhraseAsCharArray(){

        char[] phraseArray = new char[this.phrase.length()];
        phraseArray = this.phrase.toCharArray();

        return phraseArray;
    }
}
