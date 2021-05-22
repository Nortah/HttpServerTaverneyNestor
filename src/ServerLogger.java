import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ServerLogger {
    //Initialise the logger
    static Logger myLogger = Logger.getLogger("ServerLogs");
    static String loggerPath;
    public static Logger getMyLogger()
    {
        //get month and year
        Date date = new Date();
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate localDate = date.toInstant().atZone(timeZone).toLocalDate();
        int year = localDate.getYear();
        String month = localDate.getMonth().toString();
        //set path of the logger
        loggerPath = "c:\\logs\\"+year+month+"serverLogs.log";
        try{
            //Instantiate path of the logger
            FileHandler fh = new FileHandler(loggerPath, true);
            myLogger.addHandler(fh);
            //Format the logs with our cutom class
            MyCustomFormatter formatter = new MyCustomFormatter();
            fh.setFormatter(formatter);

        } catch (IOException e) {
            myLogger.severe(e.getMessage());
        }
        return myLogger;
    }
}
