package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MonitorSide extends AppCompatActivity {
    TextView email,phone;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_side);

        User user = SharedPrefManagerSl.getInstance(this).getUser();
        getSupportActionBar().setTitle(user.getLastName() + "  " + user.getFirstName());
        getSupportActionBar().setSubtitle(user.getUserType());
        Log.i("Id_cuurent :: ", Integer.toString(user.getId()));
        email = findViewById(R.id.emailSlrside);
        phone = findViewById(R.id.phoneSlrside);
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        findViewById(R.id.btnLogoutsalarieSide).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            SharedPrefManagerSl.getInstance(getApplicationContext()).logout();
            }
        });



    }
    /*
    public void listerClient(View view){
        Log.i("Lister", " All Client ");
        Intent intent = new Intent(this, ListClientsSl.class);
        startActivity(intent);

    }*/
    public void aboutMe(View view){
        Intent intent = new Intent(this, ModiferSl.class);
        startActivity(intent);
    }


    /*
    public void testBtn(View view) {
        Intent intent = new Intent(this, ListerClientTime.class);
        startActivity(intent);
    }*/

    public void listerTache(View view) {
        Log.i("Test", "Is clicked");
        Intent intent = new  Intent(this, MonitorTasks.class);
        startActivity(intent);
    }

    public void makePresence(View view) {
        Log.i("Test", "Make Presence is work");
        Intent intent = new Intent(this, CalenderMinitorSide.class);
        startActivity(intent);
    }
}