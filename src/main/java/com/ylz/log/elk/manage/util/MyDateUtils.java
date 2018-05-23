package com.ylz.log.elk.manage.util;

import java.util.Calendar;
import java.util.Date;

public class MyDateUtils {

    public static Date calDate(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }
}
