package com.example.zoltorc.zot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class ZotLoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject j_object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try{
                                    //Log.e("Email not fetched", "Email has been not fetched");
                                    String email = j_object.getString("email");
                                    //Log.e("Email fetched", "Email has been fetched");
                                    String birthday = j_object.getString("birthday"); // 01/31/1980 format
                                    String fb_id = j_object.getString("id");
                                    String gender = j_object.getString("gender");
                                    String name = j_object.getString("name");
                                    String avatar_url = j_object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    Intent i = new Intent(ZotLoginActivity.this, ZotFacebookResultActivity.class);
                                    i.putExtra("email",email);
                                    i.putExtra("fb_id", fb_id);
                                    i.putExtra("birthday", birthday);
                                    i.putExtra("gender",gender);
                                    i.putExtra("name", name);
                                    i.putExtra("avatar_url", avatar_url);
                                    startActivity(i);
                                }catch(Exception e){
                                    Log.e("Error in Fb", e.toString());
                                    Toast.makeText(ZotLoginActivity.this, e+"Error in Fb fetching =)",
                                        Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(ZotLoginActivity.this, "Cancelation done by user.",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(ZotLoginActivity.this, "Error by Facebook... =)",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
