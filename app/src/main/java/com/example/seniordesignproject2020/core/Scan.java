package com.example.seniordesignproject2020.core;

import androidx.annotation.Nullable;

import com.example.seniordesignproject2020.core.scan_types.ScanType;

import java.sql.Date;

public class Scan {

    public int id;
    public long date;
    public ScanType scan_type;
    public String image_location;
    public String scan_result;

    public Scan()
    {

    }

    public Scan(@Nullable int id,long date, ScanType scan_type,
                String image_location, String scan_result)
    {
        this.id = id;
        this.date = date;
        this.scan_type = scan_type;
        this.image_location = image_location;
        this.scan_result = scan_result;
    }

}
