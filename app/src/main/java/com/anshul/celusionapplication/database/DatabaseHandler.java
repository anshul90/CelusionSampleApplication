package com.anshul.celusionapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "demoApplication";

    // Table name
    private static final String TABLE_CUSTOMERS = "customers_table";
    private static final String TABLE_ORDERS = "orders_table";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String MEMBER_ID = "member_id";
    private static final String CONTACT_NAME = "contact_name";
    private static final String COMPANY_NAME = "company_name";
    private static final String COUNTRY = "country";
    private static final String POSTAL_CODE = "postal_code";
    private static final String PHONE = "phone";

    // ORDERS Table Columns names
    private static final String ORDER_ID = "order_id";
    private static final String ORDER_DATE = "order_date";
    private static final String SHIP_COUNTRY = "ship_country";
    private static final String SHIP_ADDRESS = "ship_address";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + MEMBER_ID + " TEXT not null,"
                + CONTACT_NAME + " TEXT," + COMPANY_NAME + " TEXT," + COUNTRY + " TEXT,"
                + PHONE + " TEXT," + POSTAL_CODE + " TEXT" + ")";
        db.execSQL(CREATE_CUSTOMER_TABLE);

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ORDER_ID + " TEXT," + SHIP_ADDRESS + " TEXT," + SHIP_COUNTRY + " TEXT,"
                + ORDER_DATE + " TEXT," + MEMBER_ID + " TEXT not null" + ")";
        db.execSQL(CREATE_ORDER_TABLE);

    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
        // Create tables again
        onCreate(db);
    }

    // Adding new Customers
    public void insertNewCustomer(String MEMBER_ID, String CONTACT_NAME, String COMPANY_NAME,
                                  String COUNTRY, String PHONE, String POSTAL_CODE) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("member_id", MEMBER_ID);
        values.put("contact_name", CONTACT_NAME);
        values.put("company_name", COMPANY_NAME);
        values.put("country", COUNTRY);
        values.put("phone", PHONE);
        values.put("postal_code", POSTAL_CODE);
        db.insert(TABLE_CUSTOMERS, null, values);
        db.close();
    }

    //Get All Members
    public Cursor getMembersData(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor_members = db.rawQuery(query, null);
        return cursor_members;
    }

    //Get All Orders
    public Cursor getOrdersData(String query, String CustomerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor_members = db.rawQuery(query, new String[]{CustomerId});
        return cursor_members;
    }

    //Delete Members
    public void deleteMembers(Context context) {
        DatabaseHandler dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int i =
                    db.delete(TABLE_CUSTOMERS, null, null);
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    //Delete Members
    public void deleteOrders(Context context) {
        DatabaseHandler dbHelper = new DatabaseHandler(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int i =
                    db.delete(TABLE_ORDERS, null, null);
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    // Adding new Orders
    public void insertNewOrder(String MEMBER_ID, String ORDER_ID, String ORDER_DATE,
                               String SHIP_COUNTRY, String SHIP_ADDRESS) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("member_id", MEMBER_ID);
        values.put("order_id", ORDER_ID);
        values.put("order_date", ORDER_DATE);
        values.put("ship_country", SHIP_COUNTRY);
        values.put("ship_address", SHIP_ADDRESS);
        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }
}
