package org.academiadecodigo.whiledlings.boardwalk.utility;

public class OutputBuilder {


    public static String buildOutput(char[] phrase){

        String output = "";

        for (int i = 0; i < (phrase.length * 2) + 5; i++) {
            output += "-";
        }

        output += "\n|  ";

        for (char c : phrase) {

            output += c + " ";
        }

        output += " |\n";

        for (int i = 0; i < (phrase.length * 2) + 5; i++) {
            output += "-";
        }
        return output;
    }


    public static String logo(){

        return " _ \n" +
                "  \\_/ \n" +
                "   |._ \n" +
                "   |’.”-._.-“”—.-“-.__.-‘/ \n" +
                "   |  \\       .-.        ( \n" +
                "   |   |     (@.@)        ) \n" +
                "   |   |   '=.|m|.='     / \n" +
                "   |  /    .='`\"``=.    / \n" +
                "   |.'                 ( \n" +
                "   |.-\"-.__.-\"\"-.__.-\"-.) \n" +
                "   | \n" +
                "   |  [.. [..       [....           [.       [.......    [.....    [..        [..      [.       [..      [..   [.. \n" +
                "   |  [.    [..   [..    [..       [. ..     [..    [..  [..   [.. [..        [..     [. ..     [..      [..  [.. \n" +
                "   |  [.     [..[..        [..    [.  [..    [..    [..  [..    [..[..   [.   [..    [.  [..    [..      [.. [.. \n" +
                "   |  [... [.   [..        [..   [..   [..   [. [..      [..    [..[..  [..   [..   [..   [..   [..      [. [. \n" +
                "   |  [.     [..[..        [..  [...... [..  [..  [..    [..    [..[.. [. [.. [..  [...... [..  [..      [..  [.. \n" +
                "   |  [.      [.  [..     [..  [..       [.. [..    [..  [..   [.. [. [.    [.... [..       [.. [..      [..   [.. \n" +
                "   |  [.... [..     [....     [..         [..[..      [..[.....    [..        [..[..         [..[........[..     [.. \n";
    }
}
