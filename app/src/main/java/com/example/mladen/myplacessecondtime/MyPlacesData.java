package com.example.mladen.myplacessecondtime;

import java.util.ArrayList;

public class MyPlacesData {

    private ArrayList<MyPlace> myPlaces;
    private MyPlacesData(){
        myPlaces = new ArrayList<MyPlace>();
        myPlaces.add(new MyPlace("Place A"));
        myPlaces.add(new MyPlace("Place B"));
        myPlaces.add(new MyPlace("Place C"));
        myPlaces.add(new MyPlace("Place D"));
        myPlaces.add(new MyPlace("Place E"));
       /* MyPlace place = new MyPlace("Place A");
        place.setLongitude("55");
        place.setLatitude("44");
        myPlaces.add(place);*/

    }

    private static class SingletonHolder{
        public static final MyPlacesData instance = new MyPlacesData();
    }

    public static MyPlacesData getInstance(){
        return SingletonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces(){
        return this.myPlaces;
    }

    public void addNewPlace(MyPlace place){
        myPlaces.add(place);
    }

    public MyPlace getPlace(int index){
        return myPlaces.get(index);
    }

    public void deletePlace(int index){
        myPlaces.remove(index);
    }

}
