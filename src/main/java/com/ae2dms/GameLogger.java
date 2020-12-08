
/**
 * @author Zeyu Xiong
 */
package com.ae2dms;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger extends Logger {

    private static final Logger logger = Logger.getLogger("GameLogger");
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Calendar calendar = Calendar.getInstance();

    // Design Pattern: Singleton
    private static GameLogger gameLogger;

    static {
        try {
            gameLogger = new GameLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameLogger getInstance(){
        return gameLogger;
    }

    /**
     * This Method initializes a file handler to record logs
     * @throws IOException FileHandler
     */
    private GameLogger() throws IOException {
        super("com.aes2dms.sokoban", null);

        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + GameEngine.GAME_NAME + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * @param message the message you want to add at the end of the returned string
     * @return a formatted message
     */
    public String createFormattedMessage(String message) {
        return  "\n" + "Time: "+ dateFormat.format(calendar.getTime()) + "\n" + message;
    }

    /**
     * @param message  the message you want to add at the end of the returned info string
     */
    public void info(String message) {
        logger.info(createFormattedMessage(message));
    }

    /**
     * @param message  the message you want to add at the end of the returned warning string
     */
    public void warning(String message) {
        logger.warning(createFormattedMessage(message));
    }


    /**
     * @param message  the message you want to add at the end of the returned severe string
     */
    public void severe(String message) {
        logger.severe(createFormattedMessage(message));
    }
}