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

public class ModiferSl extends AppCompatActivity {
    EditText fName, lName, salareeEmail, passwd, salareePhone;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_modifer_sl);

        User user = SharedPrefManagerSl.getInstance(this).getUser();
        email = user.getEmail();
        password = user.getPassword();
        fName = findViewById(R.id.fNamesl);
        lName = findViewById(R.id.lNamesl);
        salareeEmail = findViewById(R.id.slEmail);
        passwd = findViewById(R.id.slpasswd);
        salareePhone = findViewById(R.id.slPhone);

        fName.setText(user.getFirstName());
        lName.setText(user.getLastName());
        salareeEmail.setText(user.getEmail());
        passwd.setText(user.getPassword());
        salareePhone.setText(user.getPhone());


        findViewById(R.id.buttonchangeSl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here modifier client::
                modiferSl();
            }
        });

    }

    private void modiferSl() {
        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String slmailtxt = salareeEmail.getText().toString();
        String passwordF = passwd.getText().toString();
        String slPhone = salareePhone.getText().toString();
        Log.i("info first name", firstName);
        Log.i("info last Name", lastName);
        Log.i("client email ", slmailtxt);
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
        if (TextUtils.isEmpty(slmailtxt)) {
            salareeEmail.setError("Please enter your email");
            salareeEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passwordF)) {
            passwd.setError("Please enter your password");
            passwd.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(slPhone)) {
            salareePhone.setError("Please enter your phone Number");
            salareePhone.requestFocus();
            return;
        }

        Log.i("INFOR ", firstName + " : " + lastName + " : " + slmailtxt + " : 'f' " + passwordF + " : " + slPhone + " : " + " : " + email + " : 'A'" + password);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_MODSL,
                new Response.Listener<String>() {
                    //$stmt->bind_param("sssssss",$fName, $lName, $clientEmail, $passwd, $clientPhone,$userEmail, $userPasswd);
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
                                JSONObject userJson = obj.getJSONObject("user");
                                User salare = SharedPrefManagerSl.getInstance(ModiferSl.this).getUser();
                                salare.setFirstName(userJson.getString("userFName"));
                                salare.setLastName(userJson.getString("userLName"));
                                salare.setEmail(userJson.getString("slEmail"));
                                salare.setPassword(userJson.getString("passwd"));
                                salare.setPhone(userJson.getString("userPhone"));

                                SharedPrefManagerSl.getInstance(getApplicationContext()).userLogin(salare);
                                finish();
                                startActivity(new Intent(getApplicationContext(), MonitorSide.class));
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
                params.put("slEmail", slmailtxt);
                params.put("userFName", firstName);
                params.put("userLName", lastName);
                params.put("passwd", passwordF);
                params.put("userPhone", slPhone);
                params.put("userEmail", email);
                params.put("userPasswd", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}