package com.example.mladen.myplacessecondtime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    boolean editMode = true;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);

        try
        {
            Intent listIntent = getIntent();
            Bundle positionBundle = listIntent.getExtras();
            if(positionBundle != null)
                position = positionBundle.getInt("position");
            else
                editMode = false;
        }
        catch(Exception e)
        {
            editMode = false;
        }

        final Button finishedButton = (Button)findViewById(R.id.editmyplace_finish_button);
        Button cancelButton = (Button)findViewById(R.id.editmyplace_cancel_button);
        EditText nameEditText = (EditText)findViewById(R.id.editmyplace_name_edit);

        if(!editMode)
        {
            finishedButton.setEnabled(false);
            finishedButton.setText("Add");
        }
        else if(position >= 0)
        {
            finishedButton.setText("Save");

            MyPlace place = MyPlacesData.getInstance().getPlace(position);

            nameEditText.setText(place.getName());

            EditText descEditText = (EditText)findViewById(R.id.editmyplace_description_edit);
            descEditText.setText(place.getDesc());

            EditText latEditText = (EditText) findViewById(R.id.editmyplace_lat_edit);
            latEditText.setText(place.getLatitude());

            EditText lonEditText = (EditText) findViewById(R.id.editmyplace_lon_edit);
            lonEditText.setText(place.getLongitude());


        }

        finishedButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                finishedButton.setEnabled(editable.length() > 0);
            }
        });

        Button locationButton = (Button)findViewById(R.id.editmyplace_location_button);
        locationButton.setOnClickListener(this);


    }

    /*Bez ovog ne vidimo tri tacke u gornjem desnom uglu
     * koje predstavljaju opadajuci meni*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_edit_my_place, menu);
        return true;
    }

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
        else if(id == R.id.my_places_list_item)
        {
            //Toast.makeText(this, "My Places!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MyPlacesList.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.editmyplace_finish_button:
                EditText etName = (EditText)findViewById(R.id.editmyplace_name_edit);
                String nme = etName.getText().toString();

                EditText etDesc = (EditText)findViewById(R.id.editmyplace_description_edit);
                String desc = etDesc.getText().toString();

                EditText etLat = (EditText)findViewById(R.id.editmyplace_lat_edit);
                String lat = etLat.getText().toString();

                EditText etLon = (EditText)findViewById(R.id.editmyplace_lon_edit);
                String lon = etLon.getText().toString();

                if(!editMode)
                {
                    MyPlace place = new MyPlace(nme, desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                    MyPlacesData.getInstance().addNewPlace(place);
                }
                else
                {
                    MyPlace place = MyPlacesData.getInstance().getPlace(position);
                    place.setName(nme);
                    place.setDesc(desc);
                    place.setLatitude(lat);
                    place.setLongitude(lon);
                }
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.editmyplace_cancel_button:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.editmyplace_location_button:
                Intent i = new Intent(this, MyPlacesMapsActivity.class);
                i.putExtra("state", MyPlacesMapsActivity.SELECT_COORDINATES);
                startActivityForResult(i, 1);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                String lon = data.getExtras().getString("lon");
                EditText lonText = (EditText) findViewById(R.id.editmyplace_lon_edit);
                lonText.setText(lon);

                String lat = data.getExtras().getString("lat");
                EditText latText = (EditText) findViewById(R.id.editmyplace_lat_edit);
                latText.setText(lat);

            }
        }
        catch(Exception e){}
    }
}
