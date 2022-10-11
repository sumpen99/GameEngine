package helper.struct;
import helper.io.IOHandler;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static helper.methods.CommonMethods.stringIsInt;

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

    public static String secondsToTime(float rawSeconds) {
        int hours, hours_residue, minutes, seconds, milliseconds;
        hours = (int) rawSeconds/3600;
        hours_residue = (int) rawSeconds % 3600;
        minutes = hours_residue/60;
        seconds = hours_residue % 60;
        milliseconds = getDecimalPart(rawSeconds);
        return "%d:%d:%d.%d".formatted(hours, minutes, seconds, milliseconds);
    }

    public static int getDecimalPart(float rawSeconds){
        PassedCheck p;
        String[] raw = ("%f".formatted(rawSeconds)).split(",");
        if(raw.length == 2){
            if((p = stringIsInt(raw[1])).passed)return p.iNum;
        }
        return 0;
    }

    public static long getMilliSecondsPast(int year,int month,int day){
        assert year >= 1970 : "Probably Wrong Year";
        long timeElapsed;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "%d-%d-%d".formatted(year,month,day);
        try{
            Date date = sdf.parse(dateString);
            //date.getTime();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            timeElapsed = calendar.getTimeInMillis();
        }
        catch(Exception err){
            IOHandler.logToFile(err.getMessage());
            timeElapsed = 0;
        }
        return timeElapsed;
    }

    public static long getMilliSeconds(){
        long timeElapsed;
        try{
            Calendar calendar = Calendar.getInstance();
            timeElapsed = calendar.getTimeInMillis();
        }
        catch(Exception err){
            IOHandler.logToFile(err.getMessage());
            timeElapsed = 0;
        }
        return timeElapsed;
    }

    public static String getDateFromMilliseconds(long milliSeconds){
        String d = "1900-00-00";
        try{
            Date date = new Date(milliSeconds);
            d = date.toString();
        }
        catch(Exception err){
            IOHandler.logToFile(err.getMessage());
        }
        return d;
    }
}
