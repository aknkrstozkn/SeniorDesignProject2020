package com.example.seniordesignproject2020.core.database;

import android.database.Cursor;

import com.example.seniordesignproject2020.core.Color;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.scan_types.RedColorScan;
import com.example.seniordesignproject2020.core.scan_types.ScanType;

public class DataConverter {

    protected ScanType red_color_scan(Cursor cursor)
    {
        ScanType scan_type = new RedColorScan();
        try {
            scan_type.surface_color = Color.int_to_color(
                    cursor.getInt(cursor.getColumnIndex(DataBase.SURFACE_COLOR))
            );
            scan_type.test_color = Color.int_to_color(
                    cursor.getInt(cursor.getColumnIndex(DataBase.TEST_COLOR))
            );
        } catch (Exception e)
        {
            throw e;
        }
        return scan_type;
    }

    protected Scan scan(Cursor cursor, ScanType scan_type)
    {
        Scan scan = new Scan();
        try {
            scan.id = cursor.getInt(cursor.getColumnIndex(DataBase.SCAN_ID));
            scan.date = cursor.getInt(cursor.getColumnIndex(DataBase.DATE));
            scan.image_location = cursor.getString(
                    cursor.getColumnIndex(DataBase.IMAGE_LOCATION));
            scan.scan_result = cursor.getString(
                    cursor.getColumnIndex(DataBase.RESULT));
            scan.scan_type = scan_type;
        } catch (Exception e)
        {
            throw e;
        }

        return scan;
    }
}
