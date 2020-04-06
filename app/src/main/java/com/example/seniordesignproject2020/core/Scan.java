package com.example.seniordesignproject2020.core;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.example.seniordesignproject2020.core.scan_types.ScanType;

public class Scan implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.date);
        dest.writeParcelable(this.scan_type, flags);
        dest.writeString(this.image_location);
        dest.writeString(this.scan_result);
    }

    protected Scan(Parcel in) {
        this.id = in.readInt();
        this.date = in.readLong();
        this.scan_type = in.readParcelable(ScanType.class.getClassLoader());
        this.image_location = in.readString();
        this.scan_result = in.readString();
    }

    public static final Parcelable.Creator<Scan> CREATOR = new Parcelable.Creator<Scan>() {
        @Override
        public Scan createFromParcel(Parcel source) {
            return new Scan(source);
        }

        @Override
        public Scan[] newArray(int size) {
            return new Scan[size];
        }
    };
}
