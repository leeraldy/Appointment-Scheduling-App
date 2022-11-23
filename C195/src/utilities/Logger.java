package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger Class: Handles logs file.
 *
 * @author Hussein Coulibaly
 */
public class Logger {

    private static final String FILENAME = "login_activity.txt";

    public static void log(String userName, int success) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime local = LocalDateTime.now();
            String timeFormatter = formatter.format(local);

            BufferedWriter logFile = new BufferedWriter(new FileWriter(FILENAME, true));
            logFile.append(timeFormatter + " " + userName +
                    " Successfully signed in" + "\n");
            logFile.flush();
            logFile.close();
        }
        catch (IOException error) {
            error.printStackTrace();
        }

    }
}
