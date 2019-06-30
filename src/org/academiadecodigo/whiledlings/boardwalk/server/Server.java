package org.academiadecodigo.whiledlings.boardwalk.server;

import org.academiadecodigo.whiledlings.boardwalk.game.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int PORT = 2929;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public void initServer(){

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server running");
            System.out.println("Threads after init server: " + Thread.activeCount());
            waitForConnection();
        } catch (IOException e) {
            System.out.println("! New IO exception: " + e.getMessage());
        }

    }

    private void waitForConnection() throws IOException {
        System.out.println("Threads before wait connection" + Thread.activeCount());
        while(true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new Lobby(clientSocket));
        }

    }
}
