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

public class MainActivity extends AppCompatActivity {

    //ProgressBar progressBar;
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            //startActivity(new Intent(this, ProfileActivity.class));
            startActivity(new Intent(this, AdminSide.class));
        }
        if (SharedPrefManagerClt.getInstance(this).isLoggedInClt()){
            finish();
            startActivity(new Intent(this, ClientSide.class));
        }
        if (SharedPrefManagerSl.getInstance(this).isLoggedIn()){
            fileList();
            startActivity(new Intent(this, MonitorSide.class));
        }


       // progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
        /*findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });*/
    }

    private void userLogin() {
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        Log.i("info", username + " : " + password);

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your email");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine


        final ProgressDialog progress = new ProgressDialog(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("Response", response);


                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                ///TODO: I have a problem to show the progress dialog ::
                                //progressDialog.show();
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                //Log.i("Id_current_Monitor : " , Integer.toString(userJson.getInt("userID")));
                                //userJson.getString("userType");
                                if ( userJson.getString("userType").equals("ADMIN")){
                                    User user = new User(
                                            userJson.getInt("userID"),
                                            userJson.getString("userFName"),
                                            userJson.getString("userLName"),
                                            userJson.getString("userPhone"),
                                            userJson.getString("userPasswd"),
                                            userJson.getString("userEmail"),
                                            userJson.getString("userType"));

                                    //storing the user in shared preferences
                                    Log.i("userEmail", user.getEmail());
                                    Log.i("userphone", user.getPhone());

                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    //starting the profile activity
                                    finish();

                                    //TODO : here the change
                                    //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    startActivity(new Intent(getApplicationContext(), AdminSide.class));
                                }else  if ( userJson.getString("userType").equals("MONITOR")){
                                    User user = new User(
                                            userJson.getInt("userID"),
                                            userJson.getString("userFName"),
                                            userJson.getString("userLName"),
                                            userJson.getString("userPhone"),
                                            userJson.getString("userPasswd"),
                                            userJson.getString("userEmail"),
                                            userJson.getString("userType"));
                                    SharedPrefManagerSl.getInstance(getApplicationContext()).userLogin(user);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MonitorSide.class));
                                }
                                else {
                                    Client client = new Client(
                                            userJson.getInt("clientID"),
                                            userJson.getString("fName"),
                                            userJson.getString("clientEmail"),
                                            userJson.getString("passwd"),
                                            userJson.getString("clientPhone"),
                                            userJson.getString("identityNumber"),
                                            userJson.getString("lName"),
                                            userJson.getString("userType")
                                            );

                                    SharedPrefManagerClt.getInstance(getApplicationContext()).clientLogin(client);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), ClientSide.class));
                                }


                                //creating a new user object
                                //  public User(int id, String firstName, String lastName, String phone, String password, String email, String userType)

                            } else {
                                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Invalid Email or password", Toast.LENGTH_SHORT).show();
                            }
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userEmail", username);
                params.put("userPasswd", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Connecting");
        progress.setMessage("Wait while the connection succes...");
        progress.setCancelable(false);
        progress.show();
    }
}