package Client;

import Server.Logger;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientThread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String ip;
    private int port;
    private String nickname;
    public static Logger log = Logger.getInstance();

    public ClientThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            log.log("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.typeNickname();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientThread.this.downService();
        }
    }

    private void typeNickname() {
        System.out.print("Type /start-your_nick, where \"your_nick\" is a nickname ");
        try {
            nickname = inputUser.readLine().substring(7);
            out.write("Say hello to " + nickname + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    class ReadMsg extends Thread {

        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = in.readLine();
                    if (msg.equals("/exit")) {
                        ClientThread.this.downService();
                        break;
                    } else {
                        log.log(msg);
                        System.out.println(msg);
                    }
                }
            } catch (IOException e) {
                ClientThread.this.downService();
            }
        }
    }

    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    userWord = inputUser.readLine();
                    if (userWord.equals("/exit")) {
                        out.write("/exit" + "\n");
                        ClientThread.this.downService();
                        break;
                    } else {
                        out.write(" (" + LocalDateTime.now() + ") '" + nickname + "': " + userWord + "\n");
                        log.log(userWord);
                    }
                    out.flush();
                } catch (IOException e) {
                    ClientThread.this.downService();
                }
            }
        }
    }

    public String getNickname() {
        return nickname;
    }
}
