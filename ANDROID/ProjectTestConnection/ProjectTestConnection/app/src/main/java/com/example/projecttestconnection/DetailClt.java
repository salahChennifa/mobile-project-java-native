package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DetailClt extends AppCompatActivity {
    private TextView  phone, email;
    private String jsonMonitors;
    static public final String  LISTMONITOR = "LISTMONITORS";
    static public final String  ID = "ID";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_clt);
        Intent intent = getIntent();
         int position = intent.getExtras().getInt(ListeClients.POSITION);
        Client ctl =  MyRecyclerViewAdapterClt.clientList.get(position);
        id = ctl.getClientID();
        getSupportActionBar().setTitle(ctl.getfName() + "  " + ctl.getlName());
        getSupportActionBar().setSubtitle("CLIENT  control status");
        email = findViewById(R.id.email_detail_clt);
        phone = findViewById(R.id.phone_detail_clt);
        email.setText(ctl.getClientEmail());
        phone.setText(ctl.getClientPhone());
        callservice();

    }

    public void seanceActual(View view){
        Log.i("Seance Actual ", "is cliked");
        Log.i("Clinet_ID ", id + "");
        Bundle extras = new Bundle();
        extras.putString(LISTMONITOR, jsonMonitors);
        extras.putString(ID, Integer.toString(id));
        Intent intent1 = new Intent(this, CalenderAdminClient.class);
        intent1.putExtras(extras);
        startActivity(intent1);
    }
    public void modifierSeance(View view){
        Log.i("Modifier", "is clicked");
    }

    // TODO :  Here the list of monitors

    private void callservice(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=listOfMonitors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            jsonMonitors = response;
                            Log.i("Response ", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
            {
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}