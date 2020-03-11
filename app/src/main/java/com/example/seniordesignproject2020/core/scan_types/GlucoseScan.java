package com.example.seniordesignproject2020.core.scan_types;

import android.graphics.Bitmap;

import java.util.Date;

public class GlucoseScan extends ScanType {

    public GlucoseScan(Date date, Bitmap picture,
                       int control_red, int control_green, int control_blue, float control_alpha,
                       int scan_red, int scan_green, int scan_blue, float scan_alpha) {
        super(date, picture, control_red, control_green, control_blue, control_alpha, scan_red, scan_green, scan_blue, scan_alpha);
    }
}
