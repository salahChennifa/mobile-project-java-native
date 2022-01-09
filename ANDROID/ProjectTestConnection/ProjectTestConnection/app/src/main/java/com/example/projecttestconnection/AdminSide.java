package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminSide extends AppCompatActivity {
    TextView email ;
    TextView phone ;
    ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_side);



        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        imageView = findViewById(R.id.imageView);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();
        getSupportActionBar().setTitle(user.getLastName() + "  " + user.getFirstName());
        getSupportActionBar().setSubtitle(user.getUserType());

        //setting the values to the textviews


        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        //textViewGender.setText(user.getGender());

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
    }
    public void listerClt(View view){

        // TODO : here we want to change the path of activity
        Log.i("button Clet", "Work");
        Intent intent = new Intent(this, ListeClients.class);
        startActivity(intent);
    }
    public void listerSlr(View view){
        Log.i("button Slr", "Work");
        Intent intent = new Intent(this, ListerMonitor.class);
        startActivity(intent);

    }

    public void addClient(View view) {
        Log.i("Test", "Add Client");
        Intent intent = new Intent(this, AjouterClient.class);
        startActivity(intent);
    }
}