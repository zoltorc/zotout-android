package com.example.zoltorc.zot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZotFacebookResultActivity extends AppCompatActivity {

    String email, name, fb_id, avatar_url, gender, birthday, display_text;
    TextView tv, after_sign_in_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_result);
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        fb_id = bundle.getString("fb_id");
        birthday = bundle.getString("birthday");
        gender = bundle.getString("gender");
        name = bundle.getString("name");
        avatar_url = bundle.getString("avatar_url");
        display_text = "FB_ID: "+fb_id+"\nName: "+name+"\nEmail: "+email+"\nBirthday: "+birthday+"\nGender: "+gender+"Avatar_URL"+avatar_url;
        tv = (TextView)findViewById(R.id.fb_result);
        after_sign_in_tv = (TextView)findViewById(R.id.after_sign_in_tv);
        try{tv.setText(display_text);}catch(Exception e){System.out.println("Error");}

        try{
            Log.e("Zot sign up", "Sign up process begun.");
            String url="http://192.241.193.113:5000/signup/";
            URL object=new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            JSONObject user_data = new JSONObject();
            JSONObject public_profile = new JSONObject();
            public_profile.put("name", name);
            public_profile.put("birthday", birthday);
            public_profile.put("avatar_url", avatar_url);
            public_profile.put("gender", gender.charAt(0));
            user_data.put("email", email);
            user_data.put("public_profile", public_profile);
            user_data.put("facebook_id", fb_id);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(user_data.toString());
            wr.flush();
            Log.e("Zot sign up done", "Sign up process stopped.");
            Toast.makeText(ZotFacebookResultActivity.this, "Data has been sent.",
                    Toast.LENGTH_LONG).show();
            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                after_sign_in_tv.setText("" + sb.toString());
            } else {
                after_sign_in_tv.setText(con.getResponseMessage());
            }
        }catch(Exception e){ Log.e("Error in sending data", e.toString());
            Toast.makeText(ZotFacebookResultActivity.this, e+"Error in sending data",
                    Toast.LENGTH_LONG).show();}


    }
}
