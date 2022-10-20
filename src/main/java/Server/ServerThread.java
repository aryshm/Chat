package Server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    public static Logger log = Logger.getInstance();
    private String nick;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = in.readLine();
            nick = word.substring(13);
            try {
                out.write(word + "\n");
                out.flush();
            } catch (IOException ignored) {
            }
            try {
                while (true) {
                    word = in.readLine();
                    if (word.equalsIgnoreCase("/exit")) {
                        this.downService();
                        break;
                    } else if (word.contains("/nick")) {
                        String[] array = word.split("-", 5);
                        for (ServerThread vr : Server.serverList) {
                            if (vr.nick.equals(array[3])) {
                                String[] tmp = word.split("'", 3);
                                vr.send("Whisper from " + tmp[1] + ": " + array [4]);
                            }
                        }
                    } else {
                        log.log(word);
                        System.out.println(word);
                        for (ServerThread vr : Server.serverList) {
                            vr.send(word);
                        }
                    }
                }
            } catch (NullPointerException ignored) {
            }
        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerThread vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}

