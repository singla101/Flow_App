package com.gwtf.flow.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

import com.gwtf.flow.model.BookAmountModel;
import com.gwtf.flow.model.BookModel;
import com.gwtf.flow.model.BusinessModel;
import com.gwtf.flow.model.CategoriesModel;
import com.gwtf.flow.model.DataModel;
import com.gwtf.flow.model.PartyModel;
import com.gwtf.flow.model.SingleModel;

import java.util.ArrayList;

public class SqlDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Flow";

    private static final String TBL_BUSINESS = "BUSINESS";
    private static final String TBL_PARTY = "PARTY";
    private static final String TBL_BOOK = "BOOK";
    private static final String TBL_BOOKDATA = "BOOKDATA";
    private static final String TBL_CATEGORIES = "CATEGORIES";
    private static final String TBL_PAYMENTMODE = "PAYMENTMODE";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String TYPE = "TYPE";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";
    private static final String CONTACT_NUMBER = "MOBILE";
    private static final String EMAIL = "EMAIL";
    private static final String AMOUNT = "AMOUNT";
    private static final String REMARK = "REMARK";
    private static final String PAYMENT_MODE = "MODE";
    private static final String PAYMENT_TYPE = "PAYMENT_TYPE";
    private static final String PAYMENT_STATUS = "STATUS";

    public SqlDatabase (Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TBL_BUSINESS
                + " (" +
                ID + " TEXT, " +
                "OWNER " + " TEXT," +
                NAME + " TEXT, " +
                "IMAGE " + " TEXT, " +
                CATEGORY + " TEXT, " +
                TYPE + " TEXT, " +
                DATE + " TEXT, " +
                TIME + " TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TBL_BOOK
                + " (" +
                ID + " TEXT, " +
                NAME + " TEXT, " +
                DATE + " INTEGER, " +
                "BUSINESS " + " TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TBL_PARTY
                + " (" +
                ID + " TEXT, " +
                NAME + " TEXT, " +
                CONTACT_NUMBER + " TEXT, " +
                EMAIL + " TEXT, " +
                TYPE + " TEXT, " +
                DATE + " INTEGER " +
                ")");

        db.execSQL("CREATE TABLE " + TBL_CATEGORIES
                + " (" +
                ID + " TEXT, " +
                NAME + " TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TBL_PAYMENTMODE
                + " (" +
                PAYMENT_MODE + " TEXT " +
                ")");

        db.execSQL("CREATE TABLE " + TBL_BOOKDATA
                + " (" +
                ID + " TEXT, " +
                "BOOKNAME " + " TEXT, " +
                "BOOKID " + " TEXT, " +
                "PARTYNAME " + " TEXT, " +
                "BUSINESSID " + " TEXT, " +
                "PARTYID " + " TEXT, " +
                AMOUNT + " INTEGER, " +
                DATE + " TEXT, " +
                TIME + " TEXT, " +
                REMARK + " TEXT, " +
                CONTACT_NUMBER + " TEXT, " +
                CATEGORY + " TEXT, " +
                PAYMENT_MODE + " TEXT, " +
                PAYMENT_TYPE + " TEXT, " +
                PAYMENT_STATUS + " TEXT " +
                ")");

        db.execSQL("CREATE TABLE PRODUCTS (" + ID + " TEXT," +
                "TITLE TEXT, IMAGE1 TEXT, IMAGE2 TEXT, IMAGE3 TEXT, IMAGE4 TEXT, " +
                "BUSINESSID TEXT, PRICE TEXT, CATEGORY TEXT, LINK TEXT, DESCRIPTION TEXT)");
    }

    public void addProduct(String id, String title, String image1, String image2, String image3, String image4,
                           String businessId, String price, String category, String link, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put("TITLE", title);
        values.put("IMAGE1", image1);
        values.put("IMAGE2", image2);
        values.put("IMAGE3", image3);
        values.put("IMAGE4", image4);
        values.put("BUSINESSID", businessId);
        values.put("PRICE", price);
        values.put("CATEGORY", category);
        values.put("LINK", link);
        values.put("DESCRIPTION", description);
        db.insert("PRODUCTS", null, values);
        db.close();
    }

    public void addBusiness(String id, String name,
                             String businessName, String category,
                             String type, String date,
                             String time ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put("OWNER", name);
        values.put(NAME, businessName);
        values.put(CATEGORY, category);
        values.put(TYPE, type);
        values.put(DATE, date);
        values.put(TIME, time);
        db.insert(TBL_BUSINESS, null, values);
        db.close();
    }

    public void addCategories(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        db.insert(TBL_CATEGORIES, null, values);
        db.close();
    }

    public void addPaymentMode(String mode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PAYMENT_MODE, mode);
        db.insert(TBL_PAYMENTMODE, null, values);
        db.close();
    }

    public void addBooks(String id, String name,
                            Long date, String businessId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("BUSINESS", businessId);
        values.put(ID, id);
        values.put(DATE, date);
        values.put(NAME, name);
        db.insert(TBL_BOOK, null, values);
        db.close();
    }

    public void addParty(String id, String name, String number, String email,
                         String type, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        values.put(CONTACT_NUMBER, number);
        values.put(EMAIL, email);
        values.put(TYPE, type);
        values.put(DATE, amount);
        db.insert(TBL_PARTY, null, values);
        db.close();
    }

    public void updateBookName(String id, String name, long date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(DATE, date);
        db.update(TBL_BOOK, values, ID+"=?", new String[] {id});
        db.close();
    }

    public void updateBusinessCategoryTYpe(String id, String category,
                               String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY, category);
        values.put(TYPE, type);
        db.update(TBL_BUSINESS, values, ID+"=?", new String[] {id});
        db.close();
    }

    public void updateBusinessName(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        db.update(TBL_BUSINESS, values, ID+"=?", new String[] {id});
        db.close();
    }

    public void updateBusinessImage(String id, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IMAGE", image);
        db.update(TBL_BUSINESS, values, ID+"=?", new String[] {id});
        db.close();
    }

    public void deleteBook (String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_BOOK,ID + "=? AND " +NAME+"=?", new String[] {id, name});
        db.close();
    }

    public void deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_BOOKDATA,ID + "=?", new String[] {id});
        db.close();
    }

    public void deleteCategories (String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_CATEGORIES,ID + "=? AND " +NAME+"=?", new String[] {id, name});
        db.close();
    }

    public void deletePaymentMode (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_PAYMENTMODE,PAYMENT_MODE + "=?", new String[] {name});
        db.close();
    }

    public void addData(String id, String bookName, String bookId,
                        String partyName, String partyId, String businessid, int amount, String date,
                        String time, String remark, String contact, String category, String paymentMode,
                        String paymentInOut, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put("BOOKNAME", bookName);
        values.put("BOOKID", bookId);
        values.put("PARTYNAME", partyName);
        values.put("PARTYID", partyId);
        values.put("BUSINESSID", businessid);
        values.put(AMOUNT, amount);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(REMARK, remark);
        values.put(CONTACT_NUMBER, contact);
        values.put(CATEGORY, category);
        values.put(PAYMENT_MODE, paymentMode);
        values.put(PAYMENT_TYPE, paymentInOut);
        values.put(PAYMENT_STATUS, status);
        db.insert(TBL_BOOKDATA, null, values);
        db.close();
    }

    public void updateData(String id, String bookName, String bookId,
                        String partyName, String partyId, String businessid, int amount, String date,
                        String time, String remark, String contact, String category, String paymentMode,
                        String paymentInOut, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put("BOOKNAME", bookName);
        values.put("BOOKID", bookId);
        values.put("PARTYNAME", partyName);
        values.put("PARTYID", partyId);
        values.put("BUSINESSID", businessid);
        values.put(AMOUNT, amount);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(REMARK, remark);
        values.put(CONTACT_NUMBER, contact);
        values.put(CATEGORY, category);
        values.put(PAYMENT_MODE, paymentMode);
        values.put(PAYMENT_TYPE, paymentInOut);
        values.put(PAYMENT_STATUS, status);
        db.update(TBL_BOOKDATA, values, ID+"=?", new String[] {id});
        db.close();
    }


    public ArrayList<BookAmountModel> getBooksAmount (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOKDATA + " WHERE BUSINESSID=?", new String[] {id} );
        ArrayList<BookAmountModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new BookAmountModel(cursor.getInt(6), cursor.getString(13)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<CategoriesModel> getCategories (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_CATEGORIES + " WHERE ID=?", new String[] {id} );
        ArrayList<CategoriesModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new CategoriesModel(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<DataModel> getData (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOKDATA + " WHERE BOOKID=?", new String[] {id} );
        ArrayList<DataModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new DataModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<DataModel> getDataParty (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOKDATA + " WHERE PARTYNAME=?", new String[] {id} );
        ArrayList<DataModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new DataModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<DataModel> getData () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOKDATA, null);
        ArrayList<DataModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new DataModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<DataModel> getBookData (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOKDATA + " WHERE BUSINESSID=?", new String[] {id});
        ArrayList<DataModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new DataModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13),
                        cursor.getString(14)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<PartyModel> getParty () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_PARTY, null );
        ArrayList<PartyModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new PartyModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5)
                        ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<BookAmountModel> getBookAmount (String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOKDATA + " WHERE BOOKID=?", new String[] {id} );
        ArrayList<BookAmountModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new BookAmountModel(cursor.getInt(6), cursor.getString(13)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<SingleModel> getPaymentMode () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_PAYMENTMODE,null);
        ArrayList<SingleModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new SingleModel(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<CategoriesModel> getPaymentsMode() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_PAYMENTMODE,null);
        ArrayList<CategoriesModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new CategoriesModel( "",cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<BusinessModel> getBusiness() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BUSINESS, null );
        ArrayList<BusinessModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new BusinessModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<BusinessModel> getBusines(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BUSINESS + " WHERE ID=?",  new String[] {id});
        ArrayList<BusinessModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new BusinessModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<BookModel> getBook() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOK, null );
        ArrayList<BookModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new BookModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getLong(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    public ArrayList<BookModel> getBooks(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_BOOK + " WHERE BUSINESS=?", new String[] {id} );
        ArrayList<BookModel> experienceModelsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                experienceModelsList.add(new BookModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getLong(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return experienceModelsList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BOOKDATA);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BUSINESS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PARTY);
        db.execSQL("DROP TABLE IF EXISTS " + "PRODUCTS");
        onCreate(db);
    }
}