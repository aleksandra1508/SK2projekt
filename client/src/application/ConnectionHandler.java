package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    public static final int MILLIS1000 = 1000;
    public static final String INFO = "INFOR";
    public static final String END = "ENDDD";
    public static final String WIN_WHITE = "WIN:W";
    public static final String WIN_BLACK = "WIN:B";
    public static final String NOBODY = "nobody";
    public static final String WHITE = "white";
    public static final String BLACK = "black";
    private Game game;
    private Socket socket;
    boolean stop;

    public ConnectionHandler(Socket socket, Game gra) {
        this.socket = socket;
        this.game = gra;
        this.stop = false;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            byte[] buffer = new byte[5];
            int count;
            while (!stop) {
                if (game.timeOnEnemy) {
                    count = is.read(buffer);
                    if (buffer[2] == ':') {
                        game.opponentMakeMove(new String(buffer,0,2), new String(buffer,3,2));
                        game.timeOnEnemy = false;
                    } else {
                        String received = new String(buffer,0,5);
                        System.out.println(received);
                        if (received.equals(INFO)) {
                            count = is.read(buffer);
                            received = new String(buffer,0,5);
                            System.out.println(received);
                            if (received.equals(END)) {
                                game.theEnd(NOBODY);
                            } else if (received.equals(WIN_WHITE)) {
                                game.theEnd(WHITE);
                            } else if (received.equals(WIN_BLACK)) {
                                game.theEnd(BLACK);
                            } else {
                                System.out.println("Otrzymano błędną wiadomość...");
                            }
                        }
                    }
                } else if (game.moveToSend) {
                    os.write(game.sending.getBytes());
                    game.timeOnEnemy = true;
                    game.moveToSend = false;
                }
                Thread.sleep(MILLIS1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
