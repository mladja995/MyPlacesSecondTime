package com.example.mladen.myplacessecondtime;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MyPlace {

    public String name;
    public String description;
    public String longitude;
    public String latitude;
    //int ID;
    @Exclude
    public String key;

    public MyPlace(){}

    public MyPlace(String nme, String desc)
    {
        this.name = nme;
        this.description = desc;
    }

    public MyPlace(String nme)
    {
        this(nme, "");
    }

    public String getName(){return this.name;}
    public String getDesc(){return this.description;}
    public void setName(String nme){this.name = nme;}
    public void setDesc(String desc){this.description = desc;}
    public String getLongitude(){return this.longitude;}
    public String getLatitude(){return this.latitude;}
    public void setLongitude(String longitude){this.longitude = longitude;}
    public void setLatitude(String latitude){this.latitude = latitude;}
    //public int getID(){return this.ID;}
    //public void setID(int ID){this.ID = ID;}



    @Override
    public String toString()
    {
        return this.name;
    }


}
