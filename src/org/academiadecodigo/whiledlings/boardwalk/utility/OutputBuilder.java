package org.academiadecodigo.whiledlings.boardwalk.utility;

import org.academiadecodigo.whiledlings.boardwalk.game.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class OutputBuilder {

    public static String buildOutput(char[] phrase){

        String output = " ";

        for (int i = 0; i < (phrase.length * 2) + 3; i++) {
            output += "-";
        }

        output += "\n|  ";

        for (char c : phrase) {

            output += c + " ";
        }

        output += " |\n ";

        for (int i = 0; i < (phrase.length * 2) + 3; i++) {
            output += "-";
        }
        return output;
    }


    public static String logo(){

        return clearScreen() + ColorTerminal.ANSI_GREEN.getAnsi() + " _ \n" +
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
                         ColorTerminal.ANSI_PURPLE.getAnsi() + "     ." + ColorTerminal.ANSI_GREEN.getAnsi() + "  `----|__________\\.  ",
                         ColorTerminal.ANSI_PURPLE.getAnsi() + "      \\-----''----.....___  " + ColorTerminal.ANSI_RESET.getAnsi(),
                         ColorTerminal.ANSI_PURPLE.getAnsi() + "       \\               \"\"/  " + ColorTerminal.ANSI_RESET.getAnsi()};

        String[] sea = { " ^~^~^~^~^`~^~^`^~^~^`^~^~^ ",
                         "  ~^~^~`~~^~^`~^~^~`~~^~^~  "};

        String blankLine = "                            ";

        String[] finalArray = {"","","","","","","","","","","","","",""};

        for (int i=0; i<finalArray.length; i++) {           // run all lines of final string array

            for (Player player : players) {                 // run all player to merge to final string array

                ColorTerminal seaColor = ColorTerminal.ANSI_CYAN;
                if (player.getLives()<=0) {
                    seaColor = ColorTerminal.ANSI_RED;
                }

                if (i == 10) {                              // 1st line of sea
                    finalArray[i] += seaColor.getAnsi() + sea[0] + ColorTerminal.ANSI_RESET.getAnsi();
                    continue;
                }
                if (i == 11) {                              // 2nd line of sea
                    finalArray[i] += seaColor.getAnsi() + sea[1] + ColorTerminal.ANSI_RESET.getAnsi();
                    continue;
                }

                if (i == 12) {
                    finalArray[i] += blankLine;             // Blank line before name
                    continue;
                }

                if (i == 13) {                              // Line of the player's alias

                    int numOfSpaces = blankLine.length()-4 - player.getAlias().length();
                    finalArray[i] += "  " + player.getAlias();
                    for (int j=0; j<numOfSpaces + 2; j++) {
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

                ColorTerminal color = ColorTerminal.ANSI_GREEN;

                if (lives == 3) {
                    color = ColorTerminal.ANSI_YELLOW;
                }

                if (lives < 3) {
                    color = ColorTerminal.ANSI_RED;
                }
                finalArray[i] += color.getAnsi() + ship[i-missingLives*2] + ColorTerminal.ANSI_RESET.getAnsi();      // Write remaining ship lines

            }

        }

        String finalString = "";
        for (String s : finalArray) {
            finalString += s + "\n";
        }

        PrintWriter printWriter;

        for (Player player : players) {
            try {
                printWriter = new PrintWriter(player.getSocket().getOutputStream());
                printWriter.print(finalString + "\n");
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void drawLogo(Player player) {
        player.sendContent(logo());
    }

    public static void broadcastLogo(List<Player> players) {

        for (Player player : players) {
            player.sendContent(logo());
        }

    }

    public static String clearScreen(){

        return "\033[H\033[2J";
    }

    public static String winner (Player player, String completeSentence) {

        return ColorTerminal.ANSI_GREEN.getAnsi() + "\nCONGRATULATIONS " + player.getAlias() +"!\n\n" +
                "Sink me! Ye found the coffer's key! \"" + completeSentence + "\"\n\n" + ColorTerminal.ANSI_YELLOW.getAnsi() +
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
                "*******************************************************************************" + ColorTerminal.ANSI_RESET.getAnsi();

    }

    public static String allLoosers(String completePhrase) {

        return "\n \n DEAD MEN TELL NO TALES!!! The correct sentence is \"" + completePhrase + "\"\n" +
                "Everybody lost the game. Better luck next time.\n \n" +
                "  _______      ___      .___  ___.  _______      ______   ____    ____  _______ .______      \n" +
                " /  _____|    /   \\     |   \\/   | |   ____|    /  __  \\  \\   \\  /   / |   ____||   _  \\     \n" +
                "|  |  __     /  ^  \\    |  \\  /  | |  |__      |  |  |  |  \\   \\/   /  |  |__   |  |_)  |    \n" +
                "|  | |_ |   /  /_\\  \\   |  |\\/|  | |   __|     |  |  |  |   \\      /   |   __|  |      /     \n" +
                "|  |__| |  /  _____  \\  |  |  |  | |  |____    |  `--'  |    \\    /    |  |____ |  |\\  \\----.\n" +
                " \\______| /__/     \\__\\ |__|  |__| |_______|    \\______/      \\__/     |_______|| _| `._____|";
    }

}
