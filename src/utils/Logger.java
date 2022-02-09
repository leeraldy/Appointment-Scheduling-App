package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Logger Class: Handles anything that needs to be put in the log.
 *
 * @author Hussein Coulibaly
 */
public class Logger {

    private static final String logPath = "login_activity.txt";

    /**
     * Retains all Log of user log in activity in the text file format
     *
     * @param userName userName of logged user
     * @param successBool returns Boolean to show successful log in
     * @throws IOException
     */
    public static void auditLogin(String userName, Boolean successBool) throws IOException {
        try {

            BufferedWriter logger = new BufferedWriter(new FileWriter(logPath, true));
            logger.append(ZonedDateTime.now(ZoneOffset.UTC).toString() + " UTC-LOGIN ATTEMPT-USERNAME: " + userName +
                    " LOGIN SUCCESSFUL: " + successBool.toString() + "\n");
            logger.flush();
            logger.close();
        }
        catch (IOException error) {
            error.printStackTrace();
        }

    }
}
