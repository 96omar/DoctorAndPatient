package com.example.patient.loginandreg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.patient.DcMainActivity;
import com.example.patient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DcLogin extends AppCompatActivity {
    //declaring variables
    Button reg,sign_in;
    EditText dc_id,dc_name,dc_pass;

    String dc_id_code,dc_id_ret,dc_pass_ret;
    ProgressDialog progress;

    //defining SharedPreferences and database object
    private static final String TAG = DcLogin.class.getSimpleName();
    public static final String PREFS_NAME = "LoginPrefsDcLogin";
    DatabaseReference databaseReference, rootRef;
    private String deviceID, userName, PasswordUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dc_login);
        //assign ids with xml
        sign_in = (Button) findViewById(R.id.dc_btn_signin);
        reg = (Button) findViewById(R.id.dc_reg_btn_signin);
        dc_id = (EditText) findViewById(R.id.dc_id_text_signin);
        //dc_name = (EditText) findViewById(R.id.dc_name_text_signin);
        dc_pass = (EditText) findViewById(R.id.dc_pass_text_signin);

        /** session open */
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(getApplicationContext(), DcMainActivity.class);
            startActivity(intent);
            finish();
        }

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), DcRegsitration.class);
                startActivity(n);
                finish();
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(DcLogin.this);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                // to get id for dc
                dc_id_code = dc_id.getText().toString();

                /**firebase retrive code**/
                /** Database Connection */
                databaseReference = FirebaseDatabase.getInstance().getReference();
                rootRef = databaseReference.child("Doc" + dc_id_code);

                /** get name and make a validation */
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("Doc" + dc_id_code).exists()) {
                            /** retrieve user from database */
                            rootRef.child("Dc_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    /** Get map of users in datasnapshot */
                                    String id = dataSnapshot.getValue(String.class);
                                    dc_id_ret = id.toString().trim();
                                    System.out.println(dc_id_ret);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    /**  Failed to read value */
                                    Log.e(TAG, "Failed to read user", databaseError.toException());
                                }
                            });

                            /** Retrieve user password from database */
                            rootRef.child("Dc_Password").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    /** Get map of users in datasnapshot */
                                    String pass = dataSnapshot.getValue(String.class);
                                    dc_pass_ret = pass.toString().trim();
                                    System.out.println(dc_pass_ret);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    /** Failed to read value */
                                    Log.e(TAG, "Failed to read user", databaseError.toException());
                                    Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {

                            System.out.println("Failed to read user ID");
                            Log.e(TAG, "Failed to read user ID");
                            Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        /** Failed to read value */
                        Log.e(TAG, "Failed to read user", databaseError.toException());
                        Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (dc_id.getText().toString().equals(dc_id_ret) &&
                                dc_pass.getText().toString().equals(dc_pass_ret)) {

                            /** Open Session */
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("logged", "logged");
                            editor.commit();

                            /** Open Home Screen */
                            Intent n = new Intent(getApplicationContext(), DcMainActivity.class);
                            startActivity(n);
                            finish();

                            /** Else IF email field value and Password field value are not match any value stored in database */
                        } else if (!dc_id.getText().toString().equals(dc_id_ret) ||
                                ! dc_pass.getText().toString().equals(dc_pass_ret)) {

                            if ((dc_id.getText().toString()).isEmpty()
                                    && (dc_pass.getText().toString()).isEmpty()) {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "login Failed ID and Password Field is Empty ", Toast.LENGTH_SHORT).show();

                            } else if ((dc_id.getText().toString()).isEmpty()) {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "login Failed ID Field is Empty ", Toast.LENGTH_SHORT).show();

                            } else if ((dc_pass.getText().toString()).isEmpty()) {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "login Failed Password Field is Empty ", Toast.LENGTH_SHORT).show();

                            } else {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                }, 8000);



            }
        });
    }
}
