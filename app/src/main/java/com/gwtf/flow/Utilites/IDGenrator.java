package com.gwtf.flow.Utilites;

import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDGenrator {

    public static String getId (String Type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String date = dateFormat.format(new Date());
        String id = "";
        if (Type.equals("Business")) {
            id = "B";
        } else if (Type.equals("BOOK")) {
            id = "BO";
        } else if (Type.equals("PARTY")) {
            id = "P";
        } else if (Type.equals("DATA")) {
            id = "D";
        } else if (Type.equals("PRODUCT")) {
            id = "PRO";
        }
        id = id + date + "G";
        return id;
    }

}
