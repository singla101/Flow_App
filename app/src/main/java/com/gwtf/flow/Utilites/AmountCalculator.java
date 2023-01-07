package com.gwtf.flow.Utilites;

import static com.gwtf.flow.Utilites.Constants.Business_Selected;

import android.content.Context;

import com.gwtf.flow.Database.SqlDatabase;
import com.gwtf.flow.model.BookAmountModel;
import com.gwtf.flow.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class AmountCalculator {

    public static int getIn (Context context)  {
        SqlDatabase db = new SqlDatabase(context);
        int total = 0;
        List<BookAmountModel> list = new ArrayList<>();
        list = db.getBooksAmount(Business_Selected);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("IN")) {
                total += list.get(i).getAmount();
            }
        }
        return total;
    }

    public static int getOut (Context context)  {
        SqlDatabase db = new SqlDatabase(context);
        int total = 0;
        List<BookAmountModel> list = new ArrayList<>();
        list = db.getBooksAmount(Business_Selected);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("OUT")) {
                total += list.get(i).getAmount();
            }
        }
        return total;
    }

    public static int getBookIn (Context context, String id)  {
        SqlDatabase db = new SqlDatabase(context);
        int total = 0;
        List<BookAmountModel> list = new ArrayList<>();
        list = db.getBookAmount(id);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("IN")) {
                total += list.get(i).getAmount();
            }
        }
        return total;
    }

    public static int getBookOut (Context context, String id)  {
        SqlDatabase db = new SqlDatabase(context);
        int total = 0;
        List<BookAmountModel> list = new ArrayList<>();
        list = db.getBookAmount(id);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("OUT")) {
                total += list.get(i).getAmount();
            }
        }
        return total;
    }

    public static int getPartyIn (Context context, String id)  {
        SqlDatabase db = new SqlDatabase(context);
        int total = 0;
        List<DataModel> list = new ArrayList<>();
        list = db.getDataParty(id);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPaymentType().equals("IN")) {
                total += Integer.parseInt(list.get(i).getAmount());
            }
        }
        return total;
    }

    public static int getPartyOut (Context context, String id)  {
        SqlDatabase db = new SqlDatabase(context);
        int total = 0;
        List<DataModel> list = new ArrayList<>();
        list = db.getDataParty(id);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPaymentType().equals("OUT")) {
                total += Integer.parseInt(list.get(i).getAmount());
            }
        }
        return total;
    }

}
