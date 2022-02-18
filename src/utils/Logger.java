package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Logger Class: Manages anything that needs to be put in the log.
 *
 * @author Hussein Coulibaly
 */
public class Logger {

    private static final String FILENAME = "log.txt";

    public static void log(String userName, Boolean success) {
        try {

            BufferedWriter logger = new BufferedWriter(new FileWriter(FILENAME, true));
            logger.append(ZonedDateTime.now(ZoneOffset.UTC).toString() + " UTC-LOGIN ATTEMPT: " + userName +
                    " LOGIN SUCCESSFUL: " + success.toString() + "\n");
            logger.flush();
            logger.close();
        }
        catch (IOException error) {
            error.printStackTrace();
        }

    }
}
