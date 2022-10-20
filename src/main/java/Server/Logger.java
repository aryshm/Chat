package Server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    private static Logger logger;
    private static String myFile = "logger.txt";

    protected Logger() {}

    public static Logger getInstance() {
        if (logger == null) {
            synchronized (Logger.class) {
                logger = new Logger();
            }
        }
        return logger;
    }

    public void log(String msg) {
        try (FileWriter writer = new FileWriter(myFile, true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(LocalDateTime.now() + " " + msg + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
