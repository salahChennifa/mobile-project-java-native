package com.example.projecttestconnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManagerClt {
    private static SharedPrefManagerClt mInstance;
    private static Context mCtx;


    private static final String SHARED_PREF_NAME_CLT = "client_PART";
    private static final String KEY_FIRSTNAME_CLT = "keyfirstnameclt";
    private static final String KEY_LASTNAME_CLT = "keylastnameclt";
    private static final String KEY_EMAIL_CLT = "keyemailclt";
    private static final String KEY_PHONE_CLT = "keyphoneclt";
    private static final String KEY_PASSWORD_CLT = "keypasswordclt";
    private static final String KEY_USERTYPE_CLT = "keyusertypeclt";
    private static final String KEY_ID_CLT = "keyidclt";
    private static final String KEY_identityNumber_CLT = "keyidentityNumberclt";

    private SharedPrefManagerClt(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManagerClt getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManagerClt(context);
        }
        return mInstance;
    }


    public void clientLogin(Client client) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_CLT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // public User(int id, String firstName, String lastName, String phone, String password, String email, String userType)
        editor.putInt(KEY_ID_CLT, client.getClientID());
        editor.putString(KEY_FIRSTNAME_CLT, client.getfName());
        editor.putString(KEY_LASTNAME_CLT, client.getlName());
        editor.putString(KEY_PHONE_CLT, client.getClientPhone());
        editor.putString(KEY_PASSWORD_CLT, client.getPasswd());
        editor.putString(KEY_EMAIL_CLT, client.getClientEmail());
        editor.putString(KEY_USERTYPE_CLT, client.getUserType());
        editor.putString(KEY_identityNumber_CLT, client.getIdentityNumber());
        editor.apply();
    }

    public boolean isLoggedInClt() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_CLT, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL_CLT, null) != null;
    }
    public Client getClient() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_CLT, Context.MODE_PRIVATE);
        // Client(int clientID, String  fName, String clientEmail, String passwd, String clientPhone, String identityNumber, String lName, String userType)
        return new Client(
                sharedPreferences.getInt(KEY_ID_CLT, -1),
                sharedPreferences.getString(KEY_FIRSTNAME_CLT, null),
                sharedPreferences.getString(KEY_EMAIL_CLT, null),
                sharedPreferences.getString(KEY_PASSWORD_CLT, null),
                sharedPreferences.getString(KEY_PHONE_CLT, null),
                sharedPreferences.getString(KEY_identityNumber_CLT,null),
                sharedPreferences.getString(KEY_LASTNAME_CLT, null),
                sharedPreferences.getString(KEY_USERTYPE_CLT, null)
        );
    }
    public void logoutClt() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME_CLT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, MainActivity.class));
    }


}

