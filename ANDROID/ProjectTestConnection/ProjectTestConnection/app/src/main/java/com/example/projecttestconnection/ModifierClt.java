package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class ModifierClt extends AppCompatActivity {
    EditText fName, lName, clientEmail, passwd, clientPhone;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_modifier_clt);
        getSupportActionBar().hide();
        Client client = SharedPrefManagerClt.getInstance(this).getClient();
        getSupportActionBar().setTitle(client.getfName() + "  " + client.getlName());
        getSupportActionBar().setSubtitle(client.getUserType());
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        clientEmail = findViewById(R.id.clientEmail);
        passwd = findViewById(R.id.passwd);
        clientPhone = findViewById(R.id.clientPhone);

        fName.setText(client.getfName());
        lName.setText(client.getlName());
        clientEmail.setText(client.getClientEmail());
        passwd.setText(client.getPasswd());
        clientPhone.setText(client.getClientPhone());
        email = client.getClientEmail();
        password = client.getPasswd();
        findViewById(R.id.buttonchangeClt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifierClt();
                Log.i("fName", ": " + fName.getText().toString());
                Log.i("clientPhone", ":  " + clientPhone.getText().toString());

            }
        });

    }

    private void modifierClt() {

        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String clientemailtxt = clientEmail.getText().toString();
        String passwordF = passwd.getText().toString();
        String clientphone = clientPhone.getText().toString();
        Log.i("info first name", firstName);
        Log.i("info last Name", lastName);
        Log.i("client email ", clientemailtxt);

        //validating inputs
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

        Log.i("INFOR ", firstName + " : " + lastName + " : " + clientemailtxt + " : 'f' " + passwordF + " : " + clientphone + " : " + email + " : " + email + " : 'A'" + password);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_MODCLT,
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

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                Client client = SharedPrefManagerClt.getInstance(ModifierClt.this).getClient();
                                client.setlName(userJson.getString("fName"));
                                client.setfName(userJson.getString("lName"));
                                Log.i("the first name is : ", userJson.get("fName").toString());
                                client.setClientEmail(userJson.getString("clientEmail"));
                                client.setPasswd(userJson.getString("passwd"));
                                client.setClientPhone(userJson.getString("clientPhone"));
                                SharedPrefManagerClt.getInstance(getApplicationContext()).clientLogin(client);
                                finish();
                                startActivity(new Intent(getApplicationContext(), ClientSide.class));
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
                params.put("passwd", passwordF);
                params.put("clientPhone", clientphone);
                params.put("userEmail", email);
                params.put("userPasswd", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}