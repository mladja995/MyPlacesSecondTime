package com.example.mladen.myplacessecondtime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPlacesList extends AppCompatActivity {
    private ArrayList<String> places;
    Handler guiThread;
    Context context;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places_list);



        places = new ArrayList<String>();
        places.add("Tvrdjava");
        places.add("Cair");
        places.add("Park Svetog Save");
        places.add("Trg Kralja Milana");
        ListView myPlacesList = (ListView)findViewById(R.id.my_places_list);
        /*myPlacesList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places));*/
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));

        myPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle postionBundle = new Bundle();
                postionBundle.putInt("position", i);
                Intent in = new Intent(MyPlacesList.this, ViewMyPlaceActivity.class);
                in.putExtras(postionBundle);
                startActivity(in);
            }
        });

        myPlacesList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)contextMenuInfo;

                MyPlace place = MyPlacesData.getInstance().getPlace(info.position);
                contextMenu.setHeaderTitle(place.getName());
                contextMenu.add(0, 1, 1, "View place");
                contextMenu.add(0, 2, 2, "Edit place");
                contextMenu.add(0, 3, 3, "Delete place");
                contextMenu.add(0, 4, 4, "Show on map");
                contextMenu.add(0, 5, 5, "Upload place");





            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ListView myPlacesList = (ListView)findViewById(R.id.my_places_list);
        myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Bundle positionBundle = new Bundle();
        positionBundle.putInt("position", info.position);
        Intent i = null;
        if(item.getItemId() == 1)
        {
            i = new Intent(this, ViewMyPlaceActivity.class);
            i.putExtras(positionBundle);
            startActivity(i);
        }
        else if(item.getItemId() == 2)
        {
            i = new Intent(this, EditMyPlaceActivity.class);
            i.putExtras(positionBundle);
            startActivityForResult(i, 1);
        }
        else if(item.getItemId() == 3)
        {
            MyPlacesData.getInstance().deletePlace(info.position);
            ListView myPlacesList = (ListView)findViewById(R.id.my_places_list);
            myPlacesList.setAdapter(new ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.getInstance().getMyPlaces()));
        }
        else if(item.getItemId() == 4)
        {
            i = new Intent(this, MyPlacesMapsActivity.class);
            i.putExtra("newstate", 10);
            MyPlace place = MyPlacesData.getInstance().getPlace(info.position);
            i.putExtra("lat", place.getLatitude());
            i.putExtra("lon", place.getLongitude());
            startActivityForResult(i, 2);
        }
        else if(item.getItemId() == 5)
        {
            ExecutorService transThread = Executors.newSingleThreadExecutor();
            final int position = info.position;
            transThread.submit(new Runnable(){
                @Override
                public void run()
                {
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    guiStartProgressDialog("Sending place", "Sending" + place.getName());
                    try
                    {
                        final String message = MyPlacesHTTPHelper.sendMyPlace(place);
                        guiNotifyUser(message);


                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    guiDismissProgressDialog();
                }
            });
        }


        return super.onContextItemSelected(item);
    }

    private void guiDismissProgressDialog() {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    private void guiNotifyUser(final String message) {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void guiStartProgressDialog(final String title, final String message) {
        guiThread.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        });
    }

    /*Bez ovog ne vidimo tri tacke u gornjem desnom uglu
     * koje predstavljaju opadajuci meni*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_my_places_list, menu);
        return true;
    }

    static int NEW_PLACE = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.show_map_item)
        {
            //Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MyPlacesMapsActivity.class);
            i.putExtra("state", MyPlacesMapsActivity.SHOW_MAP);
            startActivity(i);
        }
        else if(id == R.id.new_place_item)
        {
            //Toast.makeText(this, "New Place!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, EditMyPlaceActivity.class);
            startActivity(i);
        }

        else if(id == R.id.about_item)
        {
            //Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, About.class);
            startActivity(i);
        }
        else if(id == R.id.settings_item)
        {
            Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}
