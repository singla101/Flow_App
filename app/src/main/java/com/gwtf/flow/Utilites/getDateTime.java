package com.gwtf.flow.Utilites;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getDateTime {

    public static String getDate () {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTime () {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTimes () {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Long getMilies() {
        return System.currentTimeMillis();
    }

}
