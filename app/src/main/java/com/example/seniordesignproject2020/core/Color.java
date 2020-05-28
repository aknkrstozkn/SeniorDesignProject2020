package com.example.seniordesignproject2020.core;

import android.os.Parcel;
import android.os.Parcelable;

public class Color implements Parcelable {

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

    @Override
    public String toString()
    {
        return String.valueOf(red) + ", "
                + String.valueOf(green) + ", "
                + String.valueOf(blue);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.red);
        dest.writeInt(this.green);
        dest.writeInt(this.blue);
        dest.writeInt(this.alpha);
    }

    protected Color(Parcel in) {
        this.red = in.readInt();
        this.green = in.readInt();
        this.blue = in.readInt();
        this.alpha = in.readInt();
    }

    public static final Parcelable.Creator<Color> CREATOR = new Parcelable.Creator<Color>() {
        @Override
        public Color createFromParcel(Parcel source) {
            return new Color(source);
        }

        @Override
        public Color[] newArray(int size) {
            return new Color[size];
        }
    };
}
