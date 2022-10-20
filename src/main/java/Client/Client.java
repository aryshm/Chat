package Client;

import Server.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Client {

    public static String ip = "localhost";
    public static Logger log = Logger.getInstance();
    public static int port = setPort();

    public static void main(String[] args) {
        new ClientThread(ip, port);
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