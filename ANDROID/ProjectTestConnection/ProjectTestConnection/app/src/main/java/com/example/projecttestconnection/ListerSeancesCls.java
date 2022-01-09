package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListerSeancesCls extends AppCompatActivity {
    private RecyclerView recyclerViewSeanceClt;
    private RecyclerView.Adapter recyclerViewAdatapterSeanceClt;
    private List<User> monitorList;
    private  int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister_seances_cls);

        recyclerViewSeanceClt = findViewById(R.id.recycleviewSeancesCls);
        recyclerViewSeanceClt.setHasFixedSize(true);
        recyclerViewSeanceClt.setLayoutManager( new LinearLayoutManager( ListerSeancesCls.this));

        monitorList = new ArrayList<>();
        Client client = SharedPrefManagerClt.getInstance(this).getClient();
        id = client.getClientID();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LIST_SEANCE_CLT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            JSONArray jsonArray = obj.getJSONArray("monitors");
                            for (int i = 0; i< jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                 //public User(String firstName, String lastName, String phone, String email, String isDone, String startDate)
                                monitorList.add(new User(object.getString("userFName"), object.getString("userLName"), object.getString("userPhone"), object.getString("userEmail"),  object.getString("isDone"), object.getString("startDate")));
                            }
                            recyclerViewAdatapterSeanceClt = new MyRecyclerViewAdapterSeanceSlr(monitorList, ListerSeancesCls.this);
                            recyclerViewSeanceClt.setAdapter(recyclerViewAdatapterSeanceClt);

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
                params.put("id_current_client", Integer.toString(id));
                //params.put("userPasswd", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}