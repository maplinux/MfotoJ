package com.example.mfotoj.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;




public class Drink implements Parcelable
{
    public Date dateAndTime;
    public String comments;
    public String imageUri;


    public Drink(){
    }

    public Drink(Parcel in) {
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
    public static Parcelable.Creator<Drink> CREATOR = new
            Parcelable.Creator<Drink>() {
                public Drink createFromParcel(Parcel in) {
                    return new Drink(in);
                }
                public Drink[] newArray(int size) {
                    return new Drink[size];
                }
            };
}
