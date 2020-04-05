package com.example.seniordesignproject2020.core.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.scan_types.ScanType;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    //TABLE NAME =  RED_SCAN_TABLE
    public final static String RED_SCAN_TABLE = "RED_SCAN_TABLE";
    //TABLE NAME =  SCANS_TABLE
    public final static String SCANS_TABLE = "SCANS_TABLE";
    //TABLES'S PRIMARY-KEY COLUMNS NAME
    public final static String SCAN_ID = "SCAN_ID";
    //TABLE RED_SCAN_TABLE COLUMNS'S NAMES
    public final static String SURFACE_COLOR = "SURFACE_COLOR";
    public final static String TEST_COLOR = "TEST_COLOR";
    //TABLE SCANS_TABLE COLUMNS'S NAMES
    public final static String DATE = "DATE";
    public final static String IMAGE_LOCATION = "IMAGE_LOCATION";
    public final static String RESULT = "RESULT";

    private static ModelConverter model_converter;
    private static DataConverter data_converter;

    private static DataBase single_instance = null;

    public Context context;

    public static DataBase getInstance(@Nullable Context context_)
    {
        if (single_instance == null)
        {
            single_instance = new DataBase(context_);
        }else if(single_instance.context != context_)
        {
            single_instance.close();
            single_instance = new DataBase(context_);
        }
        return single_instance;
    }

    public DataBase(@Nullable Context context) {
        super(context, "database", null, 1);
        model_converter = new ModelConverter();
        data_converter = new DataConverter();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");

        String create_scans_table = "CREATE TABLE " + SCANS_TABLE + "("
                + SCAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " LONG,"
                + IMAGE_LOCATION + " TEXT,"
                + RESULT + " TEXT)";
        db.execSQL(create_scans_table);

        String create_red_scan_table = "CREATE TABLE " + RED_SCAN_TABLE + "("
                + SCAN_ID + " INTEGER REFERENCES " + SCANS_TABLE + "(" + SCAN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE,"
                + SURFACE_COLOR + " INTEGER,"
                + TEST_COLOR + " INTEGER,"
                + "PRIMARY KEY(" + SCAN_ID + "))";
        db.execSQL(create_red_scan_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int add_scans_table(Scan scan)
    {
        int return_value = 0;
        try (SQLiteDatabase db = this.getWritableDatabase())
        {
            ContentValues values = model_converter.scan(scan);
            return_value = (int)db.insert(SCANS_TABLE, null,values);
        }

        return return_value;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void add_red_scan(ScanType scanType, Scan scan)
    {
        int scan_id = add_scans_table(scan);
        try (SQLiteDatabase db = this.getWritableDatabase())
        {
            ContentValues values = model_converter.scan_type(scanType, scan_id);
            db.insert(RED_SCAN_TABLE, null,values);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ScanType get_red_scan(int scan_id)
    {
        try (SQLiteDatabase db = this.getReadableDatabase())
        {
            String selectQuery = "SELECT * FROM " + RED_SCAN_TABLE + " WHERE "
                    + SCAN_ID + " = " + scan_id;
            try (Cursor cursor = db.rawQuery(selectQuery, null)) {
                if (cursor.moveToFirst())
                    return data_converter.red_color_scan(cursor);
                else
                    return null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<Scan> get_scans(){

        List<Scan> scans = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase())
        {
            String scan_query = "SELECT " + "*" + " FROM " + SCANS_TABLE +
                    " ORDER BY " + DATE + " DESC";

            try (Cursor cursor = db.rawQuery(scan_query, null)) {
                if (cursor.moveToFirst()) {
                    do {
                        Scan scan = data_converter.scan(cursor,
                                get_red_scan(cursor.getInt((int)cursor.getColumnIndex(SCAN_ID))));

                        scans.add(scan);
                    } while (cursor.moveToNext());
                    return scans;
                } else
                    return null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void remove_scan(int sca_id)
    {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            String selectQuery = "SELECT * FROM " + SCANS_TABLE + " WHERE " + SCAN_ID + " = " + sca_id;
            try (Cursor cursor = db.rawQuery(selectQuery, null)) {

                if (cursor.getCount() == 1) {
                    db.delete(SCANS_TABLE, SCAN_ID + " = ?", new String[]{String.valueOf(sca_id)});
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Scan get_scan(int scan_id)
    {
        try (SQLiteDatabase db = this.getReadableDatabase())
        {
            String selectQuery = "SELECT * FROM " + SCANS_TABLE + " WHERE "
                    + SCAN_ID + " = " + scan_id;
            try (Cursor cursor = db.rawQuery(selectQuery, null)) {
                if (cursor.moveToFirst())
                    return data_converter.scan(cursor, get_red_scan(scan_id));
                else
                    return null;
            }
        }
    }

}
