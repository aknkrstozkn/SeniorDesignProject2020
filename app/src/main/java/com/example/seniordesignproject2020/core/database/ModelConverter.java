package com.example.seniordesignproject2020.core.database;

import android.content.ContentValues;

import com.example.seniordesignproject2020.core.Color;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.scan_types.ScanType;

public class ModelConverter {

    protected ContentValues scan(Scan scan)
    {
        ContentValues values = new ContentValues();
        try
        {
            values.put(DataBase.DATE, scan.date);
            values.put(DataBase.IMAGE_LOCATION, scan.image_location);
            values.put(DataBase.RESULT, scan.scan_result);
        } catch (Exception e)
        {
            throw e;
        }

        return values;
    }

    protected ContentValues scan_type(ScanType scan_type, int scan_id)
    {

        ContentValues values = new ContentValues();

        try
        {
            values.put(DataBase.SCAN_ID, scan_id);
            values.put(DataBase.SURFACE_COLOR,
                    Color.color_to_int(scan_type.surface_color));
            values.put(DataBase.TEST_COLOR,
                    Color.color_to_int(scan_type.test_color));
        } catch (Exception e)
        {
            throw e;
        }

        return  values;
    }



}
