package Model;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LoginLogger {

    private static final String FILENAME = "LoginLog.txt";

    public LoginLogger() { }

    public static void log(String username) {
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
                pw.println(ZonedDateTime.now(ZoneId.of("UTC")) + " " + username);
        } catch (IOException e) {
            System.out.println("Logger Error: " + e.getMessage());
        }

    }
}
