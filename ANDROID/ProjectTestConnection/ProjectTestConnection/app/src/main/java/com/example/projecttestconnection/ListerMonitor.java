package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListerMonitor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recycleViewAdapter;
    private List<User> listMonitors;
    static public final String  ID = "ID_1";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lister_monitor);

        recyclerView = findViewById(R.id.recycleviewlistMonitor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListerMonitor.this));
        listMonitors = new ArrayList<>();
        callservice();

        ItemClickSupport.addTo( recyclerView, R.layout.activity_lister_monitor).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i("Test", " index " + position);

                // todo let work on the calendar to add task to the monitor :
                User monitor =  MyRecyclerViewAdapterMonitor.monitorsList.get(position);
                id = monitor.getId();
                Log.i("Id monitor :", id+"");

                Bundle extras = new Bundle();
                //extras.putString(LISTMONITOR, jsonMonitors);
                extras.putString(ID, Integer.toString(id));
                Intent intent1 = new Intent(ListerMonitor.this, CalendarAdminMonitor.class);
                intent1.putExtras(extras);
                startActivity(intent1);
            }
        });

    }

    private void callservice(){
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=listOfMonitors",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("TEST RES", response);
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("monitors");
                            for (int i = 0; i< jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String idMonitor = object.getString("userID");
                                String fullName = object.getString("userFName") + " " + object.getString("userLName");
                                Log.i(" full Name", fullName);
                                Log.i("Monitr" + i , "id : " + idMonitor + "fullName" + fullName);
                                listMonitors.add(new User(Integer.parseInt(idMonitor),object.getString("userFName"), object.getString("userLName"), object.getString("userPhone")));
                            }
                            Log.i("size of list : ", listMonitors.size()+"");
                            recycleViewAdapter = new MyRecyclerViewAdapterMonitor(listMonitors, ListerMonitor.this);
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
                })
        {
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading ...");
        progress.setCancelable(false);
        progress.show();
    }


}