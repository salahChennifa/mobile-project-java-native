package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AjouterClient extends AppCompatActivity {
    EditText fName, lName, clientEmail, passwd, clientPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ajouter_client);

        fName = findViewById(R.id.fName_clt);
        lName = findViewById(R.id.lName_clt);
        clientEmail = findViewById(R.id.clientEmail_clt);
        passwd = findViewById(R.id.passwd_clt);
        clientPhone = findViewById(R.id.clientPhone_clt);

        findViewById(R.id.buttonchangeClt_clt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterClt();
            }
        });
    }

    private void ajouterClt() {
        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String clientemailtxt = clientEmail.getText().toString();
        String password = passwd.getText().toString();
        String clientphone = clientPhone.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            fName.setError("Please enter your First Name");
            fName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            lName.setError("Please enter your Last Name");
            lName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(clientemailtxt)) {
            clientEmail.setError("Please enter your email");
            clientEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwd.setError("Please enter your password");
            passwd.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(clientphone)) {
            clientPhone.setError("Please enter your phone Number");
            clientPhone.requestFocus();
            return;
        }

        //if everything is fine

        Log.i("INFOR ", firstName + " : " + lastName + " : " + clientemailtxt + " : 'f' " + password + " : " + clientphone + " :  : "  + " : 'A'" + password);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_CLT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("Response", response.toString());

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                ///TODO: I have a problem to show the progress dialog ::
                                //progressDialog.show();
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), AdminSide.class));
                                finish();
                            } else {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientEmail", clientemailtxt);
                params.put("fName", firstName);
                params.put("lName", lastName);
                params.put("clientPhone", clientphone);
                params.put("userPasswd", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}