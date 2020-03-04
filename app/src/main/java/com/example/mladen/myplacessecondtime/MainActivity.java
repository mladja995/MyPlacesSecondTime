package com.example.mladen.myplacessecondtime;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    /*Bez ovog ne vidimo tri tacke u gornjem desnom uglu
    * koje predstavljaju opadajuci meni*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    static int NEW_PLACE = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.show_map_item)
        {
            Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.new_place_item)
        {
            //Toast.makeText(this, "New Place!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, EditMyPlaceActivity.class);
            startActivityForResult(i, NEW_PLACE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "New place added", Toast.LENGTH_SHORT).show();
        }
    }

}
