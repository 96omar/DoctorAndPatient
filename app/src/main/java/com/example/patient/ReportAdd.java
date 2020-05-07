package com.example.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patient.loginandreg.DcLogin;
import com.example.patient.loginandreg.PatientLogin;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ReportAdd extends AppCompatActivity {
    private Spinner spin;
    EditText current_date, dc_id, dc_name, p_id, p_name, p_age, p_addrs, p_diss, p_operation,
            p_pulse, p_bi, p_temp, p_resp, p_others,
            new_dc_examin_add_firebase, final_diag_add_to_examin;
    TextView  dc_id_other, dc_name_other, date_of_last_examin, examination_data_retrive;
    Button search_patient, save_report;
    String p_id_code, p_id_ret, p_addr_ret, p_diss_ret, p_gendr_ret, p_age_ret, p_name_ret, p_oper_ret,
            dc_id_old = "", dc_name_old = "", date_old = "", data_examination_old = "", diagno_old = "",
            p_pulse_ret = "", p_temp_ret = "", p_bi_ret = "", p_resp_ret = "", p_other_ret = "";
    //defining firebase
    Firebase firebase;
    String r_total, cha;
    char first_dc_litter;
    boolean invalid = false;
    private NotificationManager mNotificationManager;


    private static final String TAG = DcLogin.class.getSimpleName();
    DatabaseReference databaseReference, rootRef;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);

        search_patient = (Button) findViewById(R.id.btn_search);
        save_report = (Button) findViewById(R.id.btn_save);
        current_date = (EditText) findViewById(R.id.date);
        dc_id = (EditText) findViewById(R.id.dc_id_add);
        dc_name = (EditText) findViewById(R.id.dc_name_add);
        p_id = (EditText) findViewById(R.id.paient_id_add);
        p_name = (EditText) findViewById(R.id.p_name_add);
        p_age = (EditText) findViewById(R.id.p_age_add);
        p_addrs = (EditText) findViewById(R.id.p_addresses_add);
        p_diss = (EditText) findViewById(R.id.p_diss_add);
        p_operation = (EditText) findViewById(R.id.p_operation_add);
        p_pulse = (EditText) findViewById(R.id.pulse);
        p_bi = (EditText) findViewById(R.id.bi);
        p_temp = (EditText) findViewById(R.id.temp);
        p_resp = (EditText) findViewById(R.id.resp);
        p_others = (EditText) findViewById(R.id.other);
        dc_id_other = (TextView) findViewById(R.id.dc_id_other);
        dc_name_other = (TextView) findViewById(R.id.dc_name_other);
        date_of_last_examin = (TextView) findViewById(R.id.p_date_retrive);
        examination_data_retrive = (TextView) findViewById(R.id.prev_content);
        new_dc_examin_add_firebase = (EditText) findViewById(R.id.dc_current_examin);
        final_diag_add_to_examin = (EditText) findViewById(R.id.dc_dia);

        // get random num for id
        final int min = 20;
        final int max = 80;
        final int random = new Random().nextInt((max - min) + 1) + min;
        final int random2 = new Random().nextInt((max - min) + 1) + min;
        final int random3 = new Random().nextInt((max - min) + 1) + min;
        final int random4 = new Random().nextInt((max - min) + 1) + min;
        int sum = random + random2 + random3 + random4;
        final String r_sum = String.valueOf(sum).toString();

        // Add Current date in Add report
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm");
        String currentDateandTime = sdf.format(new Date());
        current_date.setText(currentDateandTime);

        /**spiner code */
        spin = (Spinner) findViewById(R.id.Spinner);
        List<String> list = new ArrayList<String>();
        String item1 = "Male";
        String item2 = "Female";
        list.add(item1);
        list.add(item2);
        //spin.setBackgroundColor(c);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down);
        spin.setAdapter(dataAdapter);

        //return p id and
        search_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(ReportAdd.this);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                // to get id for patient
                p_id_code = p_id.getText().toString();

                /**firebase retrive code**/
                /** Database Connection */
                databaseReference = FirebaseDatabase.getInstance().getReference();
                rootRef = databaseReference.child("Patient" + p_id_code);

                /** get name and make a validation */
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("Patient" + p_id_code).exists()) {
                            /** retrieve user from database */
                            rootRef.child("P_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    /** Get map of users in datasnapshot */
                                    String id = dataSnapshot.getValue(String.class);
                                    p_id_ret = id.toString().trim();
                                    System.out.println(p_id_ret);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    /**  Failed to read value */
                                    Log.e(TAG, "Failed to read user", databaseError.toException());
                                }
                            });
//get data remaining
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Patient" + p_id_code).exists()) {
                                        /** retrieve user from database */
                                        rootRef.child("P_Name").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    /** Get map of users in datasnapshot */
                                                    String name = dataSnapshot.getValue(String.class);
                                                    p_name_ret = name.toString().trim();
                                                    System.out.println(p_name_ret);
                                                } else {
                                                    p_name_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Age").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    /** Get map of users in datasnapshot */
                                                    String age = dataSnapshot.getValue(String.class);
                                                    p_age_ret = age.toString().trim();
                                                    System.out.println(p_age_ret);
                                                } else {
                                                    p_age_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });

                                        rootRef.child("P_Chronic_Disease").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String diss = dataSnapshot.getValue(String.class);
                                                    p_diss_ret = diss.toString().trim();
                                                    System.out.println(p_diss_ret);
                                                } else {
                                                    p_diss_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Gender").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String gender = dataSnapshot.getValue(String.class);
                                                    p_gendr_ret = gender.toString().trim();
                                                    System.out.println(p_gendr_ret);
                                                } else {
                                                    p_gendr_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Address").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String addre = dataSnapshot.getValue(String.class);
                                                    p_addr_ret = addre.toString().trim();
                                                    System.out.println(p_addr_ret);
                                                } else {
                                                    p_addr_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Previous_Operations").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String oper = dataSnapshot.getValue(String.class);
                                                    p_oper_ret = oper.toString().trim();
                                                    System.out.println(p_oper_ret);
                                                } else {
                                                    p_oper_ret = "";
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Vital_Processes").child("P_Pulse").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String pulse = dataSnapshot.getValue(String.class);
                                                    p_pulse_ret = pulse.toString().trim();
                                                    System.out.println(p_pulse_ret);
                                                } else {
                                                    p_pulse_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Vital_Processes").child("P_Temperature").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String temp = dataSnapshot.getValue(String.class);
                                                    p_temp_ret = temp.toString().trim();
                                                    System.out.println(p_temp_ret);
                                                } else {
                                                    p_temp_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Vital_Processes").child("P_BI").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String bi = dataSnapshot.getValue(String.class);
                                                    p_bi_ret = bi.toString().trim();
                                                    System.out.println(p_bi_ret);
                                                } else {
                                                    p_bi_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Vital_Processes").child("P_Resp").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String resp = dataSnapshot.getValue(String.class);
                                                    p_resp_ret = resp.toString().trim();
                                                    System.out.println(p_resp_ret);
                                                } else {
                                                    p_resp_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Vital_Processes").child("P_Others").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String other = dataSnapshot.getValue(String.class);
                                                    p_other_ret = other.toString().trim();
                                                    System.out.println(p_other_ret);
                                                } else {
                                                    p_other_ret = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Provisional_Diagnosis").child("Dc_id").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String dc_id = dataSnapshot.getValue(String.class);
                                                    dc_id_old = dc_id.toString().trim();
                                                    System.out.println(dc_id_old);
                                                } else {
                                                    dc_id_old = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Provisional_Diagnosis").child("Dc_Name").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String dc_name = dataSnapshot.getValue(String.class);
                                                    dc_name_old = dc_name.toString().trim();
                                                    System.out.println(dc_name_old);
                                                } else {
                                                    dc_name_old = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Provisional_Diagnosis").child("Last_Report_Date").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String date = dataSnapshot.getValue(String.class);
                                                    date_old = date.toString().trim();
                                                    System.out.println(date_old);
                                                } else {
                                                    date_old = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Provisional_Diagnosis").child("Last_Data_Added").child("Examination").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String examin = dataSnapshot.getValue(String.class);
                                                    data_examination_old = examin.toString().trim();
                                                    System.out.println(data_examination_old);
                                                } else {
                                                    data_examination_old = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                        rootRef.child("P_Report").child("P_Provisional_Diagnosis").child("Last_Data_Added").child("Diagnosis").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                /** Get map of users in datasnapshot */
                                                if (dataSnapshot.exists()) {
                                                    String diagno = dataSnapshot.getValue(String.class);
                                                    diagno_old = diagno.toString().trim();
                                                    System.out.println(diagno_old);
                                                } else {
                                                    diagno_old = "";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                /**  Failed to read value */
                                                Log.e(TAG, "Failed to read user", databaseError.toException());
                                            }
                                        });
                                    } else {

                                        System.out.println("Failed to read user ID");
                                        Log.e(TAG, "Failed to read user ID");
                                        Toast.makeText(getApplicationContext(), "Patient id Is Not Exists", Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    /** Failed to read value */
                                    Log.e(TAG, "Failed to read user", databaseError.toException());
                                    //Toast.makeText(getApplicationContext(), "Patient Data Is Not Exists", Toast.LENGTH_SHORT).show();

                                }


                            });

                        } else {

                            System.out.println("Failed to read user ID");
                            Log.e(TAG, "Failed to read user ID");
                            //Toast.makeText(getApplicationContext(), "Patient id Is Not Exists", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        /** Failed to read value */
                        Log.e(TAG, "Failed to read user", databaseError.toException());
                        //Toast.makeText(getApplicationContext(), "Patient Data Is Not Exists", Toast.LENGTH_SHORT).show();

                    }

                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (p_id.getText().toString().equals(p_id_ret)) {

                            p_name.setText(p_name_ret);
                            p_age.setText(p_age_ret);
                            p_addrs.setText(p_addr_ret);
                            int spinnerPosition = dataAdapter.getPosition(p_gendr_ret);
                            spin.setSelection(spinnerPosition);
                            if (!p_diss_ret.equals("")) {
                                p_diss.setText(p_diss_ret);

                            } else {
                                p_diss.setText("No");
                            }
                            if (!p_oper_ret.equals("")) {
                                p_operation.setText(p_oper_ret);
                            } else {
                                p_operation.setText("No");
                            }
                            if (!p_pulse_ret.equals("")) {
                                p_pulse.setText(p_pulse_ret);

                            } else {
                                p_pulse.setText("-");
                            }
                            if (!p_bi_ret.equals("")) {
                                p_bi.setText(p_bi_ret);

                            } else {
                                p_bi.setText("-");
                            }
                            if (!p_resp_ret.equals("")) {
                                p_resp.setText(p_resp_ret);

                            } else {
                                p_resp.setText("-");
                            }
                            if (!p_other_ret.equals("")) {
                                p_others.setText(p_other_ret);

                            } else {
                                p_others.setText("-");
                            }
                            if (!p_temp_ret.equals("")) {
                                p_temp.setText(p_temp_ret);

                            } else {
                                p_temp.setText("-");
                            }
                            if (!dc_id_old.equals("")) {
                                dc_id_other.setText(dc_id_old);

                            } else {
                                dc_id_other.setText("-");
                            }
                            if (!dc_name_old.equals("")) {
                                dc_name_other.setText(dc_name_old);

                            } else {
                                dc_name_other.setText("-");
                            }
                            if (!date_old.equals("")) {
                                date_of_last_examin.setText(date_old);

                            } else {
                                date_of_last_examin.setText("-");
                            }
                            if (!diagno_old.equals("") && !data_examination_old.equals("")) {
                                examination_data_retrive.setText("-The Last Patient Examination \n\n\t\t" + "-"+data_examination_old + "\n\n"
                                                                    +"-The Last Patient Diagnosis \n\n\t\t" + "-"+ diagno_old);

                            } else {
                                examination_data_retrive.setText("-");
                            }
                            if (!diagno_old.equals("")) {
                                final_diag_add_to_examin.setText(diagno_old);

                            } else {
                                final_diag_add_to_examin.setText("-");
                            }
                            if (!data_examination_old.equals("")) {
                                new_dc_examin_add_firebase.setText(data_examination_old);

                            } else {
                                new_dc_examin_add_firebase.setText("-");
                            }


                            /** Else IF  field value and Password field value are not match any value stored in database */
                        } else if (!p_id.getText().toString().equals(p_id_ret)) {

                            if ((p_id.getText().toString()).isEmpty()) {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "Please, Enter Patient Id To Start Search ", Toast.LENGTH_SHORT).show();

                            } else {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "No Patient Data Hear Please Ensure From Patient Id or Try to Have A New Report For This Patient", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                }, 8000);


            }
        });
        save_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**firebase code**/
                Firebase.setAndroidContext(ReportAdd.this);
                firebase = new Firebase("https://patient-10742.firebaseio.com/");

                if (p_id.getText().toString().equals(p_id_ret)) {
                    if (!(current_date.getText().toString()).equalsIgnoreCase(null)
                            && (current_date.getText().toString()).length() > 0
                            && !(dc_id.getText().toString()).equalsIgnoreCase(null)
                            && (dc_id.getText().toString()).length() > 0
                            && !(dc_name.getText().toString()).equalsIgnoreCase(null)
                            && (dc_name.getText().toString()).length() > 0
                            && !(p_id.getText().toString()).equalsIgnoreCase(null)
                            && (p_id.getText().toString()).length() > 0
                            && !(p_name.getText().toString()).equalsIgnoreCase(null)
                            && (p_name.getText().toString()).length() > 0
                            && !(p_age.getText().toString()).equalsIgnoreCase(null)
                            && (p_age.getText().toString()).length() > 0
                            && !(p_addrs.getText().toString()).equalsIgnoreCase(null)
                            && (p_addrs.getText().toString()).length() > 0
                            && !(p_diss.getText().toString()).equalsIgnoreCase(null)
                            && (p_diss.getText().toString()).length() > 0
                            && !(p_operation.getText().toString()).equalsIgnoreCase(null)
                            && (p_operation.getText().toString()).length() > 0
                            && !(p_pulse.getText().toString()).equalsIgnoreCase(null)
                            && (p_pulse.getText().toString()).length() > 0
                            && !(p_bi.getText().toString()).equalsIgnoreCase(null)
                            && (p_bi.getText().toString()).length() > 0
                            && !(p_temp.getText().toString()).equalsIgnoreCase(null)
                            && (p_temp.getText().toString()).length() > 0
                            && !(p_resp.getText().toString()).equalsIgnoreCase(null)
                            && (p_resp.getText().toString()).length() > 0
                            && !(p_others.getText().toString()).equalsIgnoreCase(null)
                            && (p_others.getText().toString()).length() > 0
                            && !(new_dc_examin_add_firebase.getText().toString()).equalsIgnoreCase(null)
                            && (new_dc_examin_add_firebase.getText().toString()).length() > 0
                            && !(final_diag_add_to_examin.getText().toString()).equalsIgnoreCase(null)
                            && (final_diag_add_to_examin.getText().toString()).length() > 0) {
                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" + p_id_code);

                        Firebase firebase_id = firebase.child("P_ID");
                        firebase_id.setValue(p_id_code);

                        Firebase firebase_name = firebase.child("P_Name");
                        firebase_name.setValue(p_name.getText().toString());

                        Firebase firebase_major = firebase.child("P_Address");
                        firebase_major.setValue(p_addrs.getText().toString());

                        Firebase firebase_age = firebase.child("P_Age");
                        firebase_age.setValue(p_age.getText().toString());

                        Firebase firebase_gender = firebase.child("P_Gender");
                        firebase_gender.setValue(String.valueOf(spin.getSelectedItem()));

                        Firebase firebase_diss = firebase.child("P_Chronic_Disease");
                        firebase_diss.setValue(p_diss.getText().toString());

                        Firebase firebase_oper = firebase.child("P_Previous_Operations");
                        firebase_oper.setValue(p_operation.getText().toString());

                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" +
                                p_id_code + "/P_Report" + "/P_Vital_Processes");
                        Firebase firebase_pulse = firebase.child("P_Pulse");
                        firebase_pulse.setValue(p_pulse.getText().toString());
                        Firebase firebase_bi = firebase.child("P_BI");
                        firebase_bi.setValue(p_bi.getText().toString());
                        Firebase firebase_temp = firebase.child("P_Temperature");
                        firebase_temp.setValue(p_temp.getText().toString());
                        Firebase firebase_resp = firebase.child("P_Resp");
                        firebase_resp.setValue(p_resp.getText().toString());
                        Firebase firebase_other = firebase.child("P_Others");
                        firebase_other.setValue(p_others.getText().toString());

                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" +
                                p_id_code + "/P_Report" + "/P_Provisional_Diagnosis");
                        Firebase firebase_dc_id = firebase.child("Dc_id");
                        firebase_dc_id.setValue(dc_id.getText().toString());
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(dc_name.getText().toString());
                        Firebase firebase_last_date = firebase.child("Last_Report_Date");
                        firebase_last_date.setValue(current_date.getText().toString());
                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" +
                                p_id_code + "/P_Report" + "/P_Provisional_Diagnosis" + "/Last_Data_Added");
                        Firebase firebase_data_examin = firebase.child("Examination");
                        firebase_data_examin.setValue(new_dc_examin_add_firebase.getText().toString());
                        Firebase firebase_dia = firebase.child("Diagnosis");
                        firebase_dia.setValue(final_diag_add_to_examin.getText().toString());

                        Toast.makeText(getApplicationContext(), "Report Added Successfully Dc " + dc_name.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(getApplicationContext(), DcMainActivity.class);
                        startActivity(n);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please, Check If Some Data Is Empty To Save Report",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!(current_date.getText().toString()).equalsIgnoreCase(null)
                            && (current_date.getText().toString()).length() > 0
                            && !(dc_id.getText().toString()).equalsIgnoreCase(null)
                            && (dc_id.getText().toString()).length() > 0
                            && !(dc_name.getText().toString()).equalsIgnoreCase(null)
                            && (dc_name.getText().toString()).length() > 0
                            && !(p_name.getText().toString()).equalsIgnoreCase(null)
                            && (p_name.getText().toString()).length() > 0
                            && !(p_age.getText().toString()).equalsIgnoreCase(null)
                            && (p_age.getText().toString()).length() > 0
                            && !(p_addrs.getText().toString()).equalsIgnoreCase(null)
                            && (p_addrs.getText().toString()).length() > 0
                            && !(p_diss.getText().toString()).equalsIgnoreCase(null)
                            && (p_diss.getText().toString()).length() > 0
                            && !(p_operation.getText().toString()).equalsIgnoreCase(null)
                            && (p_operation.getText().toString()).length() > 0
                            && !(p_pulse.getText().toString()).equalsIgnoreCase(null)
                            && (p_pulse.getText().toString()).length() > 0
                            && !(p_bi.getText().toString()).equalsIgnoreCase(null)
                            && (p_bi.getText().toString()).length() > 0
                            && !(p_temp.getText().toString()).equalsIgnoreCase(null)
                            && (p_temp.getText().toString()).length() > 0
                            && !(p_resp.getText().toString()).equalsIgnoreCase(null)
                            && (p_resp.getText().toString()).length() > 0
                            && !(p_others.getText().toString()).equalsIgnoreCase(null)
                            && (p_others.getText().toString()).length() > 0
                            && !(new_dc_examin_add_firebase.getText().toString()).equalsIgnoreCase(null)
                            && (new_dc_examin_add_firebase.getText().toString()).length() > 0
                            && !(final_diag_add_to_examin.getText().toString()).equalsIgnoreCase(null)
                            && (final_diag_add_to_examin.getText().toString()).length() > 0) {
                        // get first character from name
                        first_dc_litter = p_name.getText().charAt(0);
                        cha = String.valueOf(first_dc_litter).toString();
                        r_total = cha + "_" + r_sum;
                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" + r_total);

                        Firebase firebase_id = firebase.child("P_ID");
                        firebase_id.setValue(r_total);

                        Firebase firebase_name = firebase.child("P_Name");
                        firebase_name.setValue(p_name.getText().toString());

                        Firebase firebase_pass = firebase.child("P_Password");
                        firebase_pass.setValue(r_total);

                        Firebase firebase_major = firebase.child("P_Address");
                        firebase_major.setValue(p_addrs.getText().toString());

                        Firebase firebase_age = firebase.child("P_Age");
                        firebase_age.setValue(p_age.getText().toString());

                        Firebase firebase_gender = firebase.child("P_Gender");
                        firebase_gender.setValue(String.valueOf(spin.getSelectedItem()));

                        Firebase firebase_diss = firebase.child("P_Chronic_Disease");
                        firebase_diss.setValue(p_diss.getText().toString());

                        Firebase firebase_oper = firebase.child("P_Previous_Operations");
                        firebase_oper.setValue(p_operation.getText().toString());

                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" +
                                r_total + "/P_Report" + "/P_Vital_Processes");
                        Firebase firebase_pulse = firebase.child("P_Pulse");
                        firebase_pulse.setValue(p_pulse.getText().toString());
                        Firebase firebase_bi = firebase.child("P_BI");
                        firebase_bi.setValue(p_bi.getText().toString());
                        Firebase firebase_temp = firebase.child("P_Temperature");
                        firebase_temp.setValue(p_temp.getText().toString());
                        Firebase firebase_resp = firebase.child("P_Resp");
                        firebase_resp.setValue(p_resp.getText().toString());
                        Firebase firebase_other = firebase.child("P_Others");
                        firebase_other.setValue(p_others.getText().toString());

                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" +
                                r_total + "/P_Report" + "/P_Provisional_Diagnosis");
                        Firebase firebase_dc_id = firebase.child("Dc_id");
                        firebase_dc_id.setValue(dc_id.getText().toString());
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(dc_name.getText().toString());
                        Firebase firebase_last_date = firebase.child("Last_Report_Date");
                        firebase_last_date.setValue(current_date.getText().toString());
                        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient" +
                                r_total + "/P_Report" + "/P_Provisional_Diagnosis" + "/Last_Data_Added");
                        Firebase firebase_data_examin = firebase.child("Examination");
                        firebase_data_examin.setValue(new_dc_examin_add_firebase.getText().toString());
                        Firebase firebase_dia = firebase.child("Diagnosis");
                        firebase_dia.setValue(final_diag_add_to_examin.getText().toString());
                        addNotification();
                        Toast.makeText(getApplicationContext(), "Report Added Successfully Dc " + dc_name.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(getApplicationContext(), DcMainActivity.class);
                        startActivity(n);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please, Check If Some Data Is Empty To Save Report",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void addNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        Intent ii = new Intent(this, DcMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Patient ID And Pass is : " + r_total);
        bigText.setBigContentTitle("Please Inform you\'r patient His/Her Id And Pass");
        bigText.setSummaryText("ID And Pass Notification");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
