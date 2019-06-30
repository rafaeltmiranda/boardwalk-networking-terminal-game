package org.academiadecodigo.whiledlings.boardwalk.utility;

import org.academiadecodigo.whiledlings.boardwalk.game.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

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

        return clearScreen() + " _ \n" +
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
                "   |  [.... [..     [....     [..         [..[..      [..[.....    [..        [..[..         [..[........[..     [.. \n\n";
    }

    public static void ship(List players) {

        String[] ship = {"             ;~             ",
                         "           ./|\\.            ",
                         "         ./ /| `\\.          ",
                         "        /  | |   `\\.        ",
                         "       |   | |     `\\.      ",
                         "       |    \\|       `\\.    ",
                         "     .  `----|__________\\.  ",
                         "      \\-----''----.....___  ",
                         "       \\               \"\"/  "};

        String[] sea = { " ^~^~^~^~^`~^~^`^~^~^`^~^~^ ",
                         "  ~^~^~`~~^~^`~^~^~`~~^~^~  "};

        String blankLine = "                             ";

        String[] finalString = {"","","","","","","","","","","",""};

        for (int i=0; i<finalString.length; i++) {

            if (i==9) {
                finalString[i] += sea[0];
                continue;
            }
            if (i == 10) {
                finalString[i] += sea[1];
                continue;
            }

        }

    }

    public static void drawLogo(Socket playerSocket) {

        PrintWriter printWriter;

        try {
            printWriter = new PrintWriter(playerSocket.getOutputStream());
            printWriter.print(OutputBuilder.logo());
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String clearScreen(){

        return "\033[H\033[2J";
    }
}
