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

        return clearScreen() + ColorTerminal.ANSI_GREE.getAnsi() + " _ \n" +
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
                "   |  [.... [..     [....     [..         [..[..      [..[.....    [..        [..[..         [..[........[..     [.. \n\n" +
                ColorTerminal.ANSI_RESET.getAnsi();
    }

    public static void ship(List<Player> players) {

        String[] ship = {"                            ",
                         "             ;~             ",
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

        String[] finalArray = {"","","","","","","","","","","","","",""};

        for (int i=0; i<finalArray.length; i++) {           // run all lines of final string array

            for (Player player : players) {                 // run all player to merge to final string array

                if (i == 10) {                              // 1st line of sea
                    finalArray[i] += sea[0];
                    continue;
                }
                if (i == 11) {                              // 2nd line of sea
                    finalArray[i] += sea[1];
                    continue;
                }

                if (i == 12) {
                    finalArray[i] += blankLine;             // Blank line before name
                    continue;
                }

                if (i == 13) {                              // Line of the player's alias

                    int numOfSpaces = blankLine.length() - player.getAlias().length();
                    finalArray[i] += " " + player.getAlias();
                    for (int j=0; j<numOfSpaces + 1; j++) {
                        finalArray[i] += " ";
                    }
                    continue;
                }

                int lives = player.getLives();
                int missingLives = player.getMaximumLives()-lives;

                if (i < missingLives * 2) {                 // Blank lines of missing lives
                    finalArray[i] += blankLine;
                    continue;
                }

                finalArray[i] += ship[i-missingLives];      // Write remaining ship lines

            }

        }

        String finalString = "";
        for (int i = 0; i < finalArray.length; i++) {
            finalString += finalArray[i] + "\n";
        }

        PrintWriter printWriter;

        for (Player player : players) {
            try {
                printWriter = new PrintWriter(player.getSocket().getOutputStream());
                printWriter.print(finalString);
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
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

    public static void broadcastLogo(List<Player> players) {

        for (Player player : players) {
            drawLogo(player.getSocket());
        }

    }

    public static String clearScreen(){

        return "\033[H\033[2J";
    }

    public static String winner (Player player) {

        String winner = "CONGRATULATIONS " + player.getAlias() +"!\n" +
                "Sink me! Ye found the coffer!\n" + "\n" +
                "*******************************************************************************\n" +
                "          |                   |                  |                     |\n" +
                " _________|________________.=\"\"_;=.______________|_____________________|_______\n" +
                "|                   |  ,-\"_,=\"\"     `\"=.|                  |\n" +
                "|___________________|__\"=._o`\"-._        `\"=.______________|___________________\n" +
                "          |                `\"=._o`\"=._      _`\"=._                     |\n" +
                " _________|_____________________:=._o \"=._.\"_.-=\"'\"=.__________________|_______\n" +
                "|                   |    __.--\" , ; `\"=._o.\" ,-\"\"\"-._ \".   |\n" +
                "|___________________|_._\"  ,. .` ` `` ,  `\"-._\"-._   \". '__|___________________\n" +
                "          |           |o`\"=._` , \"` `; .\". ,  \"-._\"-._; ;              |\n" +
                " _________|___________| ;`-.o`\"=._; .\" ` '`.\"\\` . \"-._ /_______________|_______\n" +
                "|                   | |o;    `\"-.o`\"=._``  '` \" ,__.--o;   |\n" +
                "|___________________|_| ;     (#) `-.o `\"=.`_.--\"_o.-; ;___|___________________\n" +
                "____/______/______/___|o;._    \"      `\".o|o_.--\"    ;o;____/______/______/____\n" +
                "/______/______/______/_\"=._o--._        ; | ;        ; ;/______/______/______/_\n" +
                "____/______/______/______/__\"=._o--._   ;o|o;     _._;o;____/______/______/____\n" +
                "/______/______/______/______/____\"=._o._; | ;_.--\"o.--\"_/______/______/______/_\n" +
                "____/______/______/______/______/_____\"=.o|o_.--\"\"___/______/______/______/____\n" +
                "/______/______/______/______/______/______/______/______/______/______/_______/\n" +
                "*******************************************************************************";
        return winner;

    }

}
