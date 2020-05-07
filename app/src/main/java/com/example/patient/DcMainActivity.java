package com.example.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class DcMainActivity extends AppCompatActivity {
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton flotBtnlog;

    public static final String PREFS_NAME_DC = "LoginPrefsDc";
    public static final String PREFS_NAME = "LoginPrefsDcLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_main);
        CardView card1 = (CardView) findViewById(R.id.dr_add_reports);
        CardView card2 = (CardView) findViewById(R.id.dr_view_report);

        /** Code for menu for Floating Button**/
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        flotBtnlog = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);


        flotBtnlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /** ending session and go to login */
                SharedPreferences settings = getSharedPreferences(PREFS_NAME_DC, 0);
                SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                SharedPreferences.Editor editor2 = settings2.edit();
                editor.remove("logged");
                editor2.remove("logged");
                editor.commit();
                editor2.commit();

                Intent profile = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(profile);
                finish();

            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), ReportAdd.class);
                startActivity(n);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), ViewReportForDc.class);
                startActivity(n);
            }
        });
    }
}
