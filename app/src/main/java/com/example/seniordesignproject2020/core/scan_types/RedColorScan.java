package com.example.seniordesignproject2020.core.scan_types;

import android.os.Parcel;

import com.example.seniordesignproject2020.core.Color;

public class RedColorScan extends ScanType {

    public RedColorScan()
    {
        super();
    }

    public RedColorScan(Color surface_color, Color test_color)
    {
        super(surface_color, test_color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.surface_color, flags);
        dest.writeParcelable(this.test_color, flags);
    }

    protected RedColorScan(Parcel in) {
        this.surface_color = in.readParcelable(Color.class.getClassLoader());
        this.test_color = in.readParcelable(Color.class.getClassLoader());
    }

    public static final Creator<RedColorScan> CREATOR = new Creator<RedColorScan>() {
        @Override
        public RedColorScan createFromParcel(Parcel source) {
            return new RedColorScan(source);
        }

        @Override
        public RedColorScan[] newArray(int size) {
            return new RedColorScan[size];
        }
    };
}
