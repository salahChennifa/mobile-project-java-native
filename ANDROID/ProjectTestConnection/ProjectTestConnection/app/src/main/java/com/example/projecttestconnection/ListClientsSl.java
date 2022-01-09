package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ListClientsSl extends AppCompatActivity {
    private RecyclerView recyclerViewSl;
    private RecyclerView.Adapter recycleViewAdatapterSl;
    private List<Client> clientList;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients_sl);


        recyclerViewSl = findViewById(R.id.recycleviewcltSl);
        recyclerViewSl.setHasFixedSize(true);
        recyclerViewSl.setLayoutManager(new LinearLayoutManager(ListClientsSl.this));
        clientList = new ArrayList<>();
        User user = SharedPrefManagerSl.getInstance(this).getUser();
        id = user.getId();
        /// // TODO: work on the server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LISTCLTS_SL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);

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
                            recycleViewAdatapterSl = new MyRecyclerViewAdapterSlr(clientList, ListClientsSl.this);
                            recyclerViewSl.setAdapter(recycleViewAdatapterSl);

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
                //params.put("userPasswd", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void tapCltSl(View view){
        Intent intent = new Intent(this, DetailCltSlr.class);
        startActivity(intent);
    }


}