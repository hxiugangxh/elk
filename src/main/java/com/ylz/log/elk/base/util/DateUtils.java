package com.ylz.log.elk.base.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formate(Date date, String formate, Integer day) {
        DateFormat df = new SimpleDateFormat(formate);

        date = org.apache.commons.lang.time.DateUtils.addDays(date, day);

        return df.format(date);
    }
}
