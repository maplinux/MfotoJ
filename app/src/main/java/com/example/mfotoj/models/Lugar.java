package com.example.mfotoj.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;




public class Lugar implements Parcelable
{
    public Date dateAndTime;
    public String comments;
    public String imageUri;


    public Lugar(){
    }

    public Lugar(Parcel in) {
        dateAndTime = new Date(in.readLong());
        comments = in.readString();
        imageUri = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(dateAndTime.getTime());
        dest.writeString(comments);
        dest.writeString(imageUri);
    }
    public static Parcelable.Creator<Lugar> CREATOR = new
            Parcelable.Creator<Lugar>() {
                public Lugar createFromParcel(Parcel in) {
                    return new Lugar(in);
                }
                public Lugar[] newArray(int size) {
                    return new Lugar[size];
                }
            };
}
