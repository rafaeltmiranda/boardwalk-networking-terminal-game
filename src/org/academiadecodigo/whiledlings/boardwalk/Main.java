package org.academiadecodigo.whiledlings.boardwalk;

import org.academiadecodigo.whiledlings.boardwalk.server.Server;

public class Main {

    public static void main(String[] args) {
        System.out.println("Threads in main: " + Thread.activeCount());
        Server server = new Server();
        server.initServer();

    }

}
