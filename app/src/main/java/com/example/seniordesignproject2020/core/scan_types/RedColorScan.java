package com.example.seniordesignproject2020.core.scan_types;

import android.graphics.Bitmap;

import java.util.Date;

public class RedColorScan extends ScanType {

    private float milligram;

    public RedColorScan(Date date, Bitmap picture,
                        int control_red, int control_green, int control_blue, float control_alpha,
                        int scan_red, int scan_green, int scan_blue, float scan_alpha,
                        float milligram)
    {
        super(date, picture, control_red, control_green, control_blue, control_alpha, scan_red, scan_green, scan_blue, scan_alpha);
        this.milligram = milligram;
    }

    public void setMilligram(float milligram) {
        this.milligram = milligram;
    }

    public float getMilligram() {
        return milligram;
    }
}
