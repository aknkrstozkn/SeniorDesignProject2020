package com.example.seniordesignproject2020.core.scan_types;

import android.graphics.Bitmap;

import java.util.Date;

public abstract class ScanType {
    private Date date;
    private Bitmap picture;

    private int control_red;
    private int control_green;
    private int control_blue;
    private float control_alpha;

    private int scan_red;
    private int scan_green;
    private int scan_blue;
    private float scan_alpha;

    public ScanType(Date date, Bitmap picture,
                    int control_red, int control_green, int control_blue, float control_alpha,
                    int scan_red, int scan_green, int scan_blue, float scan_alpha)
    {
        this.date = date;
        this.picture = picture;

        this.control_red = control_red;
        this.control_green = control_green;
        this.control_blue = control_blue;
        this.control_alpha = control_alpha;

        this.scan_red = scan_red;
        this.scan_green = scan_green;
        this.scan_blue = scan_blue;
        this.scan_alpha = scan_alpha;
    }

    public Date getDate() {
        return date;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public int getControl_red() {
        return control_red;
    }

    public int getControl_green() {
        return control_green;
    }

    public int getControl_blue() {
        return control_blue;
    }

    public float getControl_alpha() {
        return control_alpha;
    }

    public int getScan_red() {
        return scan_red;
    }

    public int getScan_green() {
        return scan_green;
    }

    public int getScan_blue() {
        return scan_blue;
    }

    public float getScan_alpha() {
        return scan_alpha;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public void setControl_red(int control_red) {
        this.control_red = control_red;
    }

    public void setControl_green(int control_green) {
        this.control_green = control_green;
    }

    public void setControl_blue(int control_blue) {
        this.control_blue = control_blue;
    }

    public void setControl_alpha(float control_alpha) {
        this.control_alpha = control_alpha;
    }

    public void setScan_red(int scan_red) {
        this.scan_red = scan_red;
    }

    public void setScan_green(int scan_green) {
        this.scan_green = scan_green;
    }

    public void setScan_blue(int scan_blue) {
        this.scan_blue = scan_blue;
    }

    public void setScan_alpha(float scan_alpha) {
        this.scan_alpha = scan_alpha;
    }
}
