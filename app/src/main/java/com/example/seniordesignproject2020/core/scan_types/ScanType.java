package com.example.seniordesignproject2020.core.scan_types;

import android.graphics.Bitmap;

import com.example.seniordesignproject2020.core.Color;

import java.util.Date;

public abstract class ScanType {

    public Color surface_color;
    public Color test_color;

    public ScanType()
    {

    }

    public ScanType(Color surface_color, Color test_color)
    {
        this.surface_color = surface_color;
        this.test_color = test_color;
    }

}
