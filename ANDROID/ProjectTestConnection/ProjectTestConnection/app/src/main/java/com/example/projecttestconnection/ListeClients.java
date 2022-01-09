package com.example.projecttestconnection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeClients extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleViewAdapter;
    private List<Client> clientsList;
    static public String POSITION = "POSITION";
    static public final String  LISTMONITOR = "LISTMONITORS";
    static public final String  ID = "ID";
    private String jsonMonitors;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_liste_clients);
        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListeClients.this));
        clientsList = new ArrayList<>();

        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LISTCLT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //progressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            JSONArray jsonArray = obj.getJSONArray("clients");

                            for (int i = 0; i< jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                Log.i("id", object.getString("clientID"));
                                clientsList.add(new Client(object.getInt("clientID"), object.getString("clientEmail"), object.getString("passwd"), object.getString("clientPhone"), object.getString("fName"), object.getString("lName")));
                            }
                            recycleViewAdapter = new MyRecyclerViewAdapterClt(clientsList, ListeClients.this);
                            recyclerView.setAdapter(recycleViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading ...");
        progress.setCancelable(false);
        progress.show();
        callservice();
       // RecyclerView recyclerView_1 = findViewById(R.id.recycleview);
        ItemClickSupport.addTo( recyclerView, R.layout.activity_liste_clients).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("Test", " index " + position);

                // TODO here the to change direct to the calendar
                Client ctl =  MyRecyclerViewAdapterClt.clientList.get(position);
                id = ctl.getClientID();

                Bundle extras = new Bundle();
                extras.putString(LISTMONITOR, jsonMonitors);
                extras.putString(ID, Integer.toString(id));
                Intent intent1 = new Intent(ListeClients.this, CalenderAdminClient.class);
                intent1.putExtras(extras);
                startActivity(intent1);
            }
        });


    }

    private void callservice(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=listOfMonitors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("TEST RES", response);
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

