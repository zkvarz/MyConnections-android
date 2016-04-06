package com.example.myconnections_android.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myconnections_android.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button googleMapButton = (Button) findViewById(R.id.googleMapButton);
        googleMapButton.setOnClickListener(this);
        Button googleMapAnimation = (Button) findViewById(R.id.googleMapAnimation);
        googleMapAnimation.setOnClickListener(this);
        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(this);
        Button chatButton = (Button) findViewById(R.id.chatButton);
        chatButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.googleMapButton:
                Intent mapAnimationActivity = new Intent(this, MapAnimationActivity.class);
                startActivity(mapAnimationActivity);
                break;
            case R.id.googleMapAnimation:
                Intent intent = new Intent(this, GoogleAnimationActivity.class);
                startActivity(intent);
                break;
            case R.id.calendarButton:
                startActivity(new Intent(this, CalendarActivity.class));
                break;
            case R.id.chatButton:
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }
}
