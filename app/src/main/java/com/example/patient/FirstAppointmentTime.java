package com.example.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patient.loginandreg.DcLogin;
import com.example.patient.loginandreg.DcRegsitration;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FirstAppointmentTime extends AppCompatActivity {

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calender;
    TextView date_view;
    LinearLayout l_view,l_view2,l_view3,l_view4;
    CheckBox dc_ch1_1, dc_ch1_2, dc_ch1_3, dc_ch1_4,
            dc_ch2_1, dc_ch2_2, dc_ch2_3, dc_ch2_4,
            dc_ch3_1, dc_ch3_2, dc_ch3_3, dc_ch3_4,
            dc_ch4_1, dc_ch4_2, dc_ch4_3, dc_ch4_4;
    TextView t1,t2,t3,t4,txt_name,txt_mark,txt_date,txt_time;
    Button event,back;
    ProgressDialog progress;
    //defining firebase
    Firebase firebase;
    DatabaseReference databaseReference, rootRef;
    private static final String TAG = DcLogin.class.getSimpleName();
    String get_name,get_date,get_time;

    // for checkbox session
    public static final String shrd1 = "shared";
    public static final String swtich = "swtich";
    public static final String swtich2 = "swtich2";
    public static final String swtich3 = "swtich3";
    public static final String swtich4 = "swtich4";

    public static final String shrd1_2 = "shared_2";
    public static final String swtich_2 = "swtich_2";
    public static final String swtich2_2 = "swtich2_2";
    public static final String swtich3_2 = "swtich3_2";
    public static final String swtich4_2 = "swtich4_2";

    public static final String shrd1_3 = "shared_3";
    public static final String swtich_3 = "swtich_3";
    public static final String swtich2_3 = "swtich2_3";
    public static final String swtich3_3 = "swtich3_3";
    public static final String swtich4_3 = "swtich4_3";

    public static final String shrd1_4 = "shared_4";
    public static final String swtich_4 = "swtich_4";
    public static final String swtich2_4 = "swtich2_4";
    public static final String swtich3_4 = "swtich3_4";
    public static final String swtich4_4 = "swtich4_4";

    // variables for action of checkbox change
    private boolean switchOnOff, switchOnOff2, switchOnOff3, switchOnOff4,
            switchOnOff_2, switchOnOff2_2, switchOnOff3_2, switchOnOff4_2,
            switchOnOff_3, switchOnOff2_3, switchOnOff3_3, switchOnOff4_3,
            switchOnOff_4, switchOnOff2_4, switchOnOff3_4, switchOnOff4_4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_appointment_time);

        event = (Button) findViewById(R.id.event);
        back = (Button) findViewById(R.id.go_back);

        dc_ch1_1 = (CheckBox) findViewById(R.id.mark_btn1);
        dc_ch1_2 = (CheckBox) findViewById(R.id.mark_btn2);
        dc_ch1_3 = (CheckBox) findViewById(R.id.mark_btn3);
        dc_ch1_4 = (CheckBox) findViewById(R.id.mark_btn4);

        dc_ch2_1 = (CheckBox) findViewById(R.id.mark_btn1_2);
        dc_ch2_2 = (CheckBox) findViewById(R.id.mark_btn2_2);
        dc_ch2_3 = (CheckBox) findViewById(R.id.mark_btn3_2);
        dc_ch2_4 = (CheckBox) findViewById(R.id.mark_btn4_2);

        dc_ch3_1 = (CheckBox) findViewById(R.id.mark_btn1_3);
        dc_ch3_2 = (CheckBox) findViewById(R.id.mark_btn2_3);
        dc_ch3_3 = (CheckBox) findViewById(R.id.mark_btn3_3);
        dc_ch3_4 = (CheckBox) findViewById(R.id.mark_btn4_3);

        dc_ch4_1 = (CheckBox) findViewById(R.id.mark_btn1_4);
        dc_ch4_2 = (CheckBox) findViewById(R.id.mark_btn2_4);
        dc_ch4_3 = (CheckBox) findViewById(R.id.mark_btn3_4);
        dc_ch4_4 = (CheckBox) findViewById(R.id.mark_btn4_4);

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        txt_date = (TextView) findViewById(R.id.date_ret);
        txt_time = (TextView) findViewById(R.id.time_ret);

        l_view = (LinearLayout)findViewById(R.id.time);
        l_view2 = (LinearLayout)findViewById(R.id.time2);
        l_view3 = (LinearLayout)findViewById(R.id.time3);
        l_view4 = (LinearLayout)findViewById(R.id.time4);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_mark = (TextView) findViewById(R.id.marked);
        txt_name.setText(getIntent().getExtras().getString("name"));


        /**firebase code**/
        Firebase.setAndroidContext(this);
        final String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        firebase = new Firebase("https://patient-10742.firebaseio.com/Patient"+deviceID);

        /**firebase retrive code**/
        /** Database Connection */
        databaseReference = FirebaseDatabase.getInstance().getReference();
        rootRef = databaseReference.child("Patient" + deviceID);
        /** get name and make a validation */
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Patient" + deviceID).exists()) {

                    rootRef.child("Dc_Time").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /** Get map of users in datasnapshot */
                            if (dataSnapshot.exists()) {
                                String time = dataSnapshot.getValue(String.class);
                                get_time = time.toString().trim();
                                System.out.println(get_time);
                            }else {
                                get_time = "";
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            /** Failed to read value */
                            Log.e(TAG, "Failed to read user", databaseError.toException());
                            Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                        }
                    });
                    rootRef.child("Dc_Date").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /** Get map of users in datasnapshot */
                            if (dataSnapshot.exists()) {
                                String date = dataSnapshot.getValue(String.class);
                                get_date = date.toString().trim();
                                System.out.println(get_date);
                            }else{
                                get_date = "";
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            /** Failed to read value */
                            Log.e(TAG, "Failed to read user", databaseError.toException());
                            //Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                        }
                    });

                    rootRef.child("Dc_Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /** Get map of users in datasnapshot */
                            if (dataSnapshot.exists()) {
                                String name = dataSnapshot.getValue(String.class);
                                get_name = name.toString().trim();
                                System.out.println(get_name);
                            }else{
                                get_name = "";
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            /** Failed to read value */
                            Log.e(TAG, "Failed to read user", databaseError.toException());
                            //Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {

                    System.out.println("Failed to read user ID");
                    Log.e(TAG, "Failed to read user ID");
                    //Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /** Failed to read value */
                Log.e(TAG, "Failed to read user", databaseError.toException());
                Toast.makeText(getApplicationContext(), "login Failed!Please Ensure from your data To Login Doctor", Toast.LENGTH_SHORT).show();

            }
        });


        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (l_view.getVisibility() == View.VISIBLE) {
                    if (dc_ch1_1.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t1.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch1_2.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t2.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch1_3.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t3.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch1_4.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t4.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    else {
                        Toast.makeText(FirstAppointmentTime.this, "Please Check An Time To Complete", Toast.LENGTH_SHORT).show();
                    }
                }
                if (l_view2.getVisibility() == View.VISIBLE) {
                    if (dc_ch2_1.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t1.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch2_2.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t2.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch2_3.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t3.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch2_4.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t4.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    else {
                        Toast.makeText(FirstAppointmentTime.this, "Please Check An Time To Complete", Toast.LENGTH_SHORT).show();
                    }
                }
                if (l_view3.getVisibility() == View.VISIBLE) {
                    if (dc_ch3_1.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t1.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch3_2.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t2.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch3_3.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t3.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch3_4.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t4.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    else {
                        Toast.makeText(FirstAppointmentTime.this, "Please Check An Time To Complete", Toast.LENGTH_SHORT).show();
                    }
                }
                if (l_view4.getVisibility() == View.VISIBLE) {
                    if (dc_ch4_1.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t1.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch4_2.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t2.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch4_3.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t3.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    if (dc_ch4_4.isChecked()) {
                        Firebase firebase_dc_name = firebase.child("Dc_Name");
                        firebase_dc_name.setValue(txt_name.getText().toString());
                        Firebase firebase_date = firebase.child("Dc_Date");
                        firebase_date.setValue(date_view.getText().toString());

                        Firebase firebase_time = firebase.child("Dc_Time");
                        firebase_time.setValue(t4.getText().toString());
                        Toast.makeText(FirstAppointmentTime.this, "Appointment Registration Sucsssfully", Toast.LENGTH_SHORT).show();
                        Intent n = new Intent(FirstAppointmentTime.this, PatientMainActivty.class);
                        startActivity(n);
                        finish();
                    }
                    else {
                        Toast.makeText(FirstAppointmentTime.this, "Please Check An Time To Complete", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        progress = new ProgressDialog(FirstAppointmentTime.this);
        progress.setTitle("Loading");
        progress.setMessage("Please, Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
                if ((txt_name.getText().toString()).equals("1-Dr Ahmed Omar"+"\n\t\t"+"Congenital Heart Specialists")) {
                    if (dc_ch1_1.isChecked() || dc_ch1_2.isChecked() || dc_ch1_3.isChecked() || dc_ch1_4.isChecked()) {

                        if (get_name.equals(txt_name.getText().toString())) {
                            calender.setVisibility(View.GONE);
                            txt_mark.setVisibility(View.VISIBLE);
                            back.setVisibility(View.VISIBLE);
                            date_view.setVisibility(View.GONE);
                            event.setVisibility(View.GONE);
                            txt_date.setVisibility(View.VISIBLE);
                            txt_time.setVisibility(View.VISIBLE);
                            txt_date.setText(get_date);
                            txt_time.setText(get_time);
                        }else {
                            Toast.makeText(getApplicationContext(), "Please, Ensure From You\'r Internet Connection", Toast.LENGTH_SHORT).show();
                            Intent n = new Intent(getApplicationContext(), BookingDR.class);
                            startActivity(n);
                            finish();
                        }
                    }
                }  if ((txt_name.getText().toString()).equals("2-Dr Mohamed Ahmed"+"\n\t\t"+"Pediatric Specialist")){
                    if (dc_ch2_1.isChecked() || dc_ch2_2.isChecked() || dc_ch2_3.isChecked() || dc_ch2_4.isChecked()) {

                        if (get_name.equals(txt_name.getText().toString())) {
                            calender.setVisibility(View.GONE);
                            txt_mark.setVisibility(View.VISIBLE);
                            back.setVisibility(View.VISIBLE);
                            date_view.setVisibility(View.GONE);
                            event.setVisibility(View.GONE);
                            txt_date.setVisibility(View.VISIBLE);
                            txt_time.setVisibility(View.VISIBLE);
                            txt_date.setText(get_date);
                            txt_time.setText(get_time);
                        }else {
                            Toast.makeText(getApplicationContext(), "Please, Ensure From You\'r Internet Connection", Toast.LENGTH_SHORT).show();
                            Intent n = new Intent(getApplicationContext(), BookingDR.class);
                            startActivity(n);
                            finish();
                        }

                    }
                }if ((txt_name.getText().toString()).equals("3-Dr Amr Wahbah"+"\n\t\t"+"Internist Specialist")){
                    if (dc_ch3_1.isChecked() || dc_ch3_2.isChecked() || dc_ch3_3.isChecked() || dc_ch3_4.isChecked() ) {

                        if (get_name.equals(txt_name.getText().toString())) {
                            calender.setVisibility(View.GONE);
                            txt_mark.setVisibility(View.VISIBLE);
                            back.setVisibility(View.VISIBLE);
                            date_view.setVisibility(View.GONE);
                            event.setVisibility(View.GONE);
                            txt_date.setVisibility(View.VISIBLE);
                            txt_time.setVisibility(View.VISIBLE);
                            txt_date.setText(get_date);
                            txt_time.setText(get_time);
                        }else {
                            Toast.makeText(getApplicationContext(), "Please, Ensure From You\'r Internet Connection", Toast.LENGTH_SHORT).show();
                            Intent n = new Intent(getApplicationContext(), BookingDR.class);
                            startActivity(n);
                            finish();
                        }
                    }
                }if ((txt_name.getText().toString()).equals("4-Dr Omar Ali"+"\n\t\t"+"Neurologists Specialist")){
                    if (dc_ch4_1.isChecked() || dc_ch4_2.isChecked() || dc_ch4_3.isChecked() || dc_ch4_4.isChecked()) {

                        if (get_name.equals(txt_name.getText().toString())) {
                            calender.setVisibility(View.GONE);
                            txt_mark.setVisibility(View.VISIBLE);
                            back.setVisibility(View.VISIBLE);
                            date_view.setVisibility(View.GONE);
                            event.setVisibility(View.GONE);
                            txt_date.setVisibility(View.VISIBLE);
                            txt_time.setVisibility(View.VISIBLE);
                            txt_date.setText(get_date);
                            txt_time.setText(get_time);
                        }else {
                            Toast.makeText(getApplicationContext(), "Please, Ensure From You\'r Internet Connection", Toast.LENGTH_SHORT).show();
                            Intent n = new Intent(getApplicationContext(), BookingDR.class);
                            startActivity(n);
                            finish();
                        }

                    }
                }
            }
            }, 6000);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), BookingDR.class);
                startActivity(n);
                finish();
            }
        });
        // By ID we can use each component
        // which id is assign in xml file
        // use findViewById() to get the
        // CalendarView and TextView
        calender = (CalendarView) findViewById(R.id.calender);
        date_view = (TextView) findViewById(R.id.date_view);


        // Add Listener in calendar
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            // In this Listener have one method
            // and in this method we will
            // get the value of DAYS, MONTH, YEARS
            public void onSelectedDayChange(
                    @NonNull CalendarView view,
                    int year,
                    int month,
                    int dayOfMonth) {
                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                String Date
                        = dayOfMonth + "/"
                        + (month + 1) + "/" + year;

                // set this date in TextView for Display
                date_view.setText(Date);
                calender.setVisibility(View.GONE);
                //l_view.setVisibility(View.VISIBLE);
                if ((txt_name.getText().toString()).equals("1-Dr Ahmed Omar"+"\n\t\t"+"Congenital Heart Specialists")){
                    l_view.setVisibility(View.VISIBLE);

                }

                if ((txt_name.getText().toString()).equals("2-Dr Mohamed Ahmed"+"\n\t\t"+"Pediatric Specialist")){
                    l_view2.setVisibility(View.VISIBLE);

                }
                if ((txt_name.getText().toString()).equals("3-Dr Amr Wahbah"+"\n\t\t"+"Internist Specialist")){
                    l_view3.setVisibility(View.VISIBLE);


                }
                if ((txt_name.getText().toString()).equals("4-Dr Omar Ali"+"\n\t\t"+"Neurologists Specialist")){
                    l_view4.setVisibility(View.VISIBLE);

                }
            }
        });

        /** this 4 checkbox for save changing checked */
        dc_ch1_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch1_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch1_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch1_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });




        /** this 4 checkbox for save changing checked */
        dc_ch2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch2_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch2_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });


        /** this 4 checkbox for save changing checked */
        dc_ch3_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch3_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch3_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch3_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }});

        /** this 4 checkbox for save changing checked */
        dc_ch4_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch4_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch4_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        dc_ch4_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveData();
            }
        });

        /** this method responsible for load data of check box when open app  */
        loadData();

        /** this method responsible for Update data of check box when open app  */
        update();
    }
    /** this method responsible for save checkbox when checked */
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(shrd1, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(swtich, dc_ch1_1.isChecked());
        editor.putBoolean(swtich2, dc_ch1_2.isChecked());
        editor.putBoolean(swtich3, dc_ch1_3.isChecked());
        editor.putBoolean(swtich4, dc_ch1_4.isChecked());

        editor.putBoolean(swtich_2, dc_ch2_1.isChecked());
        editor.putBoolean(swtich2_2, dc_ch2_2.isChecked());
        editor.putBoolean(swtich3_2, dc_ch2_3.isChecked());
        editor.putBoolean(swtich4_2, dc_ch2_4.isChecked());

        editor.putBoolean(swtich_3, dc_ch3_1.isChecked());
        editor.putBoolean(swtich2_3, dc_ch3_2.isChecked());
        editor.putBoolean(swtich3_3, dc_ch3_3.isChecked());
        editor.putBoolean(swtich4_3, dc_ch3_4.isChecked());

        editor.putBoolean(swtich_4, dc_ch4_1.isChecked());
        editor.putBoolean(swtich2_4, dc_ch4_2.isChecked());
        editor.putBoolean(swtich3_4, dc_ch4_3.isChecked());
        editor.putBoolean(swtich4_4, dc_ch4_4.isChecked());

        editor.commit();
    }

    /** this method responsible for load data of check box when open app  */
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(shrd1, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(swtich, false);
        switchOnOff2 = sharedPreferences.getBoolean(swtich2, false);
        switchOnOff3 = sharedPreferences.getBoolean(swtich3, false);
        switchOnOff4 = sharedPreferences.getBoolean(swtich4, false);

        switchOnOff_2 = sharedPreferences.getBoolean(swtich_2, false);
        switchOnOff2_2 = sharedPreferences.getBoolean(swtich2_2, false);
        switchOnOff3_2 = sharedPreferences.getBoolean(swtich3_2, false);
        switchOnOff4_2 = sharedPreferences.getBoolean(swtich4_2, false);

        switchOnOff_3 = sharedPreferences.getBoolean(swtich_3, false);
        switchOnOff2_3 = sharedPreferences.getBoolean(swtich2_3, false);
        switchOnOff3_3 = sharedPreferences.getBoolean(swtich3_3, false);
        switchOnOff4_3 = sharedPreferences.getBoolean(swtich4_3, false);

        switchOnOff_4 = sharedPreferences.getBoolean(swtich_4, false);
        switchOnOff2_4 = sharedPreferences.getBoolean(swtich2_4, false);
        switchOnOff3_4 = sharedPreferences.getBoolean(swtich3_4, false);
        switchOnOff4_4 = sharedPreferences.getBoolean(swtich4_4, false);
    }

    /** this method responsible for Update data of check box when open app  */
    public void update() {
        dc_ch1_1.setChecked(switchOnOff);
        dc_ch1_2.setChecked(switchOnOff2);
        dc_ch1_3.setChecked(switchOnOff3);
        dc_ch1_4.setChecked(switchOnOff4);

        dc_ch2_1.setChecked(switchOnOff_2);
        dc_ch2_2.setChecked(switchOnOff2_2);
        dc_ch2_3.setChecked(switchOnOff3_2);
        dc_ch2_4.setChecked(switchOnOff4_2);

        dc_ch3_1.setChecked(switchOnOff_3);
        dc_ch3_2.setChecked(switchOnOff2_3);
        dc_ch3_3.setChecked(switchOnOff3_3);
        dc_ch3_4.setChecked(switchOnOff4_3);

        dc_ch4_1.setChecked(switchOnOff_4);
        dc_ch4_2.setChecked(switchOnOff2_4);
        dc_ch4_3.setChecked(switchOnOff3_4);
        dc_ch4_4.setChecked(switchOnOff4_4);
    }
}
