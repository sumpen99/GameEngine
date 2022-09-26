package helper.struct;
import helper.io.IOHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SMDateTime {
    private static SMDateTime self = null;
    private static boolean isSet = false;

    public SMDateTime(){
        assert !SMDateTime.isSet :"SMDateTime is already set!";
        SMDateTime.setInstance();
    }

    private static void setInstance(){
        SMDateTime.isSet = true;
    }

    public static void initSMDateTime(){
        self = new SMDateTime();
        if(self == null){
            self = new SMDateTime();
        }
    }

    public static String getTime(){return self.servePattern("HH:mm:ss");}

    public static String getDate(){return self.servePattern("yyyy-MM-dd");}

    public static String getDateTime(){return self.servePattern("yyyy-MM-dd HH:mm:ss");}

    private String servePattern(String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZoneId z = ZoneId.of("Europe/Stockholm");
        ZonedDateTime zdt = ZonedDateTime.now(z);
        return zdt.format(formatter);
    }
}
