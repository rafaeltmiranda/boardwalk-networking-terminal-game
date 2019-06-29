package org.academiadecodigo.whiledlings.boardwalk.utility;

import java.io.Closeable;
import java.io.IOException;

public class Closer {

    public static void close(Closeable closeable){

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
