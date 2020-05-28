package com.example.seniordesignproject2020.core.scan_types;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.seniordesignproject2020.core.Color;

public class Train implements Parcelable {

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

    protected Train(Parcel in) {
        base_color = in.readParcelable(Color.class.getClassLoader());
        test_color = in.readParcelable(Color.class.getClassLoader());
        label = in.readDouble();
    }

    public static final Creator<Train> CREATOR = new Creator<Train>() {
        @Override
        public Train createFromParcel(Parcel in) {
            return new Train(in);
        }

        @Override
        public Train[] newArray(int size) {
            return new Train[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(base_color, flags);
        dest.writeParcelable(test_color, flags);
        dest.writeDouble(label);
    }
}
