package com.example.seniordesignproject2020.core.scan_types;

import com.example.seniordesignproject2020.core.Color;

public class Train {

    public Color base_color;
    public Color test_color;
    public double label;

    public Train(Color base_color, Color test_color, double label)
    {
        this.base_color = base_color;
        this.test_color = test_color;
        this.label = label;
    }

    public Train(){}

}
