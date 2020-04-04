package com.example.seniordesignproject2020.core.scan_types;

import android.graphics.Bitmap;

import com.example.seniordesignproject2020.core.Color;

import java.util.Date;

public class RedColorScan extends ScanType {

    public RedColorScan()
    {
        super();
    }

    public RedColorScan(Color surface_color, Color test_color)
    {
        super(surface_color, test_color);
    }
}
