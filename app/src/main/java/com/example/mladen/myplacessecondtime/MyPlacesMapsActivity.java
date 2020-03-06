package com.example.mladen.myplacessecondtime;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlacesMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 1;

    private int state = 0;
    private boolean selCoorsEnabled = false;
    private LatLng placeLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if(mapBundle != null)
            {
                state = mapBundle.getInt("state");
                if(state == CENTER_PLACE_ON_MAP)
                {
                    String placeLat = mapBundle.getString("lat");
                    String placeLon = mapBundle.getString("lon");
                    placeLoc = new LatLng(Double.parseDouble(placeLat), Double.parseDouble(placeLon));
                }
            }
        }
        catch(Exception e)
        {
            Log.d("Error", "Error reading state");

        }

        setContentView(R.layout.activity_my_places_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
        }
        else
        {
            if(state == SHOW_MAP)
                mMap.setMyLocationEnabled(true);
            else if(state == CENTER_PLACE_ON_MAP)
                setOnMapClickListener();
            else
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
            addMyPlaceMarkers();
        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        addMyPlaceMarkers();

    }




    private HashMap<Marker, Integer> markerPlaceIdMap;
    private void addMyPlaceMarkers()
    {
        ArrayList<MyPlace> places = MyPlacesData.getInstance().getMyPlaces();
        markerPlaceIdMap = new HashMap<Marker, Integer>((int)((double)places.size()*1.2));
        for(int i = 0; i < places.size(); i++)
        {
            MyPlace place = places.get(i);
            if(place.getLongitude() != null && place.getLatitude() != null)
            {
                String lat = place.getLatitude();
                String lon = place.getLongitude();
                LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(loc);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_place));
                markerOptions.title(place.getName());
                Marker marker = mMap.addMarker(markerOptions);
                markerPlaceIdMap.put(marker, i);
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Intent intent = new Intent(MyPlacesMapsActivity.this, ViewMyPlaceActivity.class);
                int i = markerPlaceIdMap.get(marker);
                intent.putExtra("position", i);
                startActivity(intent);
                return true;
            }
        });
    }

    private void setOnMapClickListener()
    {
        if(mMap != null)
        {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                @Override
                public void onMapClick(LatLng latLng)
                {
                    if(state == SELECT_COORDINATES && selCoorsEnabled) {
                        String lon = Double.toString(latLng.longitude);
                        String lat = Double.toString(latLng.latitude);
                        Intent locationIntent = new Intent();
                        locationIntent.putExtra("lon", lon);
                        locationIntent.putExtra("lat", lat);
                        setResult(Activity.RESULT_OK, locationIntent);
                        finish();
                    }
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_ACCESS_FINE_LOCATION:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(state == SHOW_MAP)
                        mMap.setMyLocationEnabled(true);
                    else if(state == CENTER_PLACE_ON_MAP)
                        setOnMapClickListener();
                    else
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
                    //addMyPlaceMarkers();
                }
                return;
            }
        }
    }



    /*Bez ovog ne vidimo tri tacke u gornjem desnom uglu
     * koje predstavljaju opadajuci meni*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            menu.add(0, 1, 1, "Select Coordinates");
            menu.add(0, 2, 2, "Cancel");
            return super.onCreateOptionsMenu(menu);
        }

        else {
            getMenuInflater().inflate(R.menu.menu_map, menu);
            return true;
        }
    }

    static int NEW_PLACE = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(state == SELECT_COORDINATES && !selCoorsEnabled)
        {
            if(id == 1)
            {
                selCoorsEnabled = true;
                Toast.makeText(this, "Select coordinates", Toast.LENGTH_SHORT).show();
            }
            else if(id == 2)
            {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
        else {
            if (id == R.id.new_place_item) {
                //Toast.makeText(this, "New Place!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, EditMyPlaceActivity.class);
                startActivityForResult(i, NEW_PLACE);
            } else if (id == R.id.my_places_list_item) {
                //Toast.makeText(this, "My Places!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MyPlacesList.class);
                startActivity(i);
            } else if (id == R.id.about_item) {
                //Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, About.class);
                startActivity(i);
            } else if (id == R.id.settings_item) {
                Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
