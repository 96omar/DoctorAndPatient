package com.example.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.patient.loginandreg.DcLogin;
import com.example.patient.loginandreg.PatientLogin;
import com.google.android.material.bottomappbar.BottomAppBar;

public class MainActivity extends AppCompatActivity {
    //defining SharedPreferences
    private static final String TAG = DcLogin.class.getSimpleName();
    public static final String PREFS_NAME_DC = "LoginPrefsDc";
    public static final String PREFS_NAME_P = "LoginPrefsP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView card1 = (CardView) findViewById(R.id.crd_1);
        CardView card2 = (CardView) findViewById(R.id.crd_2);
        /** session open */
        SharedPreferences settings = getSharedPreferences(PREFS_NAME_DC, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(getApplicationContext(), DcLogin.class);
            startActivity(intent);
            finish();
        }
        SharedPreferences settings2 = getSharedPreferences(PREFS_NAME_P, 0);
        if (settings2.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(getApplicationContext(), PatientLogin.class);
            startActivity(intent);
            finish();
        }
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME_DC, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("logged", "logged");
                editor.commit();
                Intent n = new Intent(getApplicationContext(), DcLogin.class);
                startActivity(n);
                finish();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME_P, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("logged", "logged");
                editor.commit();
                Intent n = new Intent(getApplicationContext(), PatientLogin.class);
                startActivity(n);
                finish();
            }
        });
    }
}
