package com.example.seniordesignproject2020.core;

public class Color {

    public int red;
    public int green;
    public int blue;
    public int alpha;

    public Color(){}

    public Color(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 255;
    }

    public static int color_to_int(Color color)
    {
        return (color.red << 24 | color.green << 16
        | color.blue << 8 | color.alpha);
    }

    public static Color int_to_color(int int_color)
    {
        int red = int_color >> 24 & 0xff;
        int green = int_color >> 16 & 0xff;
        int blue = int_color >> 8 & 0xff;
        int alpha = int_color & 0xff;

        return new Color(red, green, blue, alpha);
    }
}
