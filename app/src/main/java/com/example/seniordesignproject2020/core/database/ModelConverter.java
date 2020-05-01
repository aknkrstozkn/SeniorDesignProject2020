package com.example.seniordesignproject2020.core.database;

import android.content.ContentValues;

import com.example.seniordesignproject2020.core.Color;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.scan_types.ScanType;
import com.example.seniordesignproject2020.core.scan_types.Train;

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

    protected ContentValues train(Train train)
    {
        ContentValues values = new ContentValues();
        try
        {
            Color base_color = train.base_color;
            Color test_color = train.test_color;
            double label = train.label;

            values.put(DataBase.BASE_RED, base_color.red);
            values.put(DataBase.BASE_GREEN, base_color.green);
            values.put(DataBase.BASE_BLUE, base_color.blue);
            values.put(DataBase.TEST_RED, test_color.red);
            values.put(DataBase.TEST_GREEN, test_color.green);
            values.put(DataBase.TEST_BLUE, test_color.blue);
            values.put(DataBase.LABEL, label);
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
