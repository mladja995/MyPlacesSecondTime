package com.example.mladen.myplacessecondtime;

public class MyPlace {

    String name;
    String description;
    String longitude;
    String latitude;
    int ID;

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
    public int getID(){return this.ID;}
    public void setID(int ID){this.ID = ID;}



    @Override
    public String toString()
    {
        return this.name;
    }


}
