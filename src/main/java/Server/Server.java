package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static int port;
    public static LinkedList<ServerThread> serverList = new LinkedList<>();
    public static Logger log;

    public static void main(String[] args) throws IOException {
        port = setPort();
        log = Logger.getInstance();
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server Started");
        log.log("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerThread(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    public static int setPort() {
        try (FileReader reader = new FileReader("settings.txt");
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                port = Integer.parseInt(line);
            }
        } catch (IOException e) {
            log.log(e.getMessage());
        }
        return port;
    }
}
