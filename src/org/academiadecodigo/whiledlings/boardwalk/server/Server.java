package org.academiadecodigo.whiledlings.boardwalk.server;

import org.academiadecodigo.whiledlings.boardwalk.game.Lobby;
import org.academiadecodigo.whiledlings.boardwalk.game.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port = 2929;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public void initServer(){

        try {
            serverSocket = new ServerSocket(port);
            waitForConnection();
        } catch (IOException e) {
            System.out.println("! New IO exception: " + e.getMessage());
        }

    }

    private void waitForConnection() throws IOException {

        while(true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new Lobby(clientSocket));
        }

    }
}
