package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientPrClients extends AppCompatActivity {
    private RecyclerView recyclerViewMon;
    private RecyclerView.Adapter recyleViewAdapterMon;
    private List<Client> clientList;
    int id;
    String date_time;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pr_clients);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        date_time = extras.getString("TIME");
        Log.i("INFO§§", date_time);
        recyclerViewMon = findViewById(R.id.recycleviewPrCltMonitor);
        recyclerViewMon.setHasFixedSize(true);
        recyclerViewMon.setLayoutManager(new LinearLayoutManager(ClientPrClients.this));
        clientList = new ArrayList<>();
        User user = SharedPrefManagerSl.getInstance(this).getUser();
        id = user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LIST_CLT_MON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("ResponseTest", response);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            JSONArray jsonArray = obj.getJSONArray("clients");
                            for (int i = 0; i< jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                Log.i("id", object.getString("clientID"));
                                //Client(int clientID, String clientEmail, String passwd, String clientPhone, String fName, String lName)
                                clientList.add(new Client(object.getInt("clientID"), object.getString("clientEmail"), object.getString("passwd"), object.getString("clientPhone"), object.getString("fName"), object.getString("lName")));
                            }
                            // :: change here the Adapter for the new list of client//
                            recyleViewAdapterMon = new MyRecyclerViewAdapterClt(clientList, ClientPrClients.this);
                            recyclerViewMon.setAdapter(recyleViewAdapterMon);

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
                }) {
            //id_current_monitor
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_current_monitor", Integer.toString(id));
                /// TODO  : here we want to pass the date form the prevois intent using putExtrat
                params.put("startDate", date_time);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}