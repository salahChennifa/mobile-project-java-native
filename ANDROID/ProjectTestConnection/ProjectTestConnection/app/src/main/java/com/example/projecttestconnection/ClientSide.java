package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ClientSide extends AppCompatActivity {
    TextView email;
    TextView phone;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_side);

        if (!SharedPrefManagerClt.getInstance(this).isLoggedInClt()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        email = findViewById(R.id.emailcltside);
        phone = findViewById(R.id.phonecltside);
        imageView = findViewById(R.id.imageViewcltSide);

        Client client = SharedPrefManagerClt.getInstance(this).getClient();
        getSupportActionBar().setTitle(client.getfName() + "  " + client.getlName());
        getSupportActionBar().setSubtitle(client.getUserType());

        email.setText(client.getClientEmail());
        phone.setText(client.getClientPhone());

        findViewById(R.id.btnLogoutcltside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SharedPrefManagerClt.getInstance(getApplicationContext()).logoutClt();
            }
        });



    }

    public void modifierInfor(View view) {
        Log.i("info", " is Clicked");
        Intent intent = new Intent(this, ModifierClt.class);
        startActivity(intent);
    }

   /* public void listerCltMonSc(View view) {
        Log.i("Lister Side Clt", " Is clicked");
        Intent intent = new Intent(this, ListerSeancesCls.class);
        startActivity(intent);
    }*/

    public void showSc(View view) {
        Log.i("Test", "showSeance");
        Intent intent = new Intent(this, CalenderClientSide.class);
        startActivity(intent);
    }
}