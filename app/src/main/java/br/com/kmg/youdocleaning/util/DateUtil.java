package br.com.kmg.youdocleaning.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {


    public static String getDateStringFromDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateTime = formatter.format(date);
        return dateTime;
    }

    public static String getDateStringTimePeriodFromDate(Date dateStart, Date dateEnd){
        if(dateStart == null || dateEnd == null){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm");
        String start = formatter.format(dateStart);
        String end = formatter.format(dateEnd);
        return start + " - " + end;
    }
}
