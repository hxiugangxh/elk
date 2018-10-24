package com.ylz.log.elk.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formate(Date date, String formate, Integer day) {
        DateFormat df = new SimpleDateFormat(formate);

        date = org.apache.commons.lang.time.DateUtils.addDays(date, day);

        return df.format(date);
    }

    public static Date parse(String dateStr, String formate) {
        DateFormat df = new SimpleDateFormat(formate);

        Date date = new Date();

        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
