package com.example.patient;

import androidx.appcompat.app.AppCompatActivity;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patient.loginandreg.DcLogin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewReportForDc extends AppCompatActivity {

    //declartation for variables
    LinearLayout l_hide_view;
    ImageButton search;
    TextView t_hide, t_dr_id, t_Dr_name, t_dr_last_date, t_dr_data, t_examin, t_diagno,
            t_operation, t_name, t_age, t_addr, t_kind, t_diss, chart_detals;
    Button view_another_report;
    EditText get_id;
    PieChartView pieChartView;

    String p_id_code, p_id_ret, p_addr_ret, p_diss_ret, p_gendr_ret, p_age_ret, p_name_ret, p_oper_ret,
            dc_id_old = "", dc_name_old = "", date_old = "", data_examination_old = "", diagno_old = "",
            p_pulse_ret = "", p_temp_ret = "", p_bi_ret = "", p_resp_ret = "", p_other_ret = "";
    private static final String TAG = DcLogin.class.getSimpleName();
    DatabaseReference databaseReference, rootRef;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_for_dc);

        //assign to xml activity
        pieChartView = findViewById(R.id.chart);
        l_hide_view = (LinearLayout) findViewById(R.id.show_patient_report);
        search = (ImageButton) findViewById(R.id.search_btn);
        t_hide = (TextView) findViewById(R.id.txt_hide);
        view_another_report = (Button) findViewById(R.id.view_another_report);
        get_id = (EditText) findViewById(R.id.id_for_patient_report_dc);
        t_dr_id = (TextView) findViewById(R.id.dr_id_view);
        t_Dr_name = (TextView) findViewById(R.id.dr_name_view);
        t_dr_last_date = (TextView) findViewById(R.id.dr_patdate_view);
        t_dr_data = (TextView) findViewById(R.id.dr_patdata_view);
        t_examin = (TextView) findViewById(R.id.dr_patexamin_view);
        t_diagno = (TextView) findViewById(R.id.dr_patdia_view);
        t_operation = (TextView) findViewById(R.id.oper_patient_ret);
        t_name = (TextView) findViewById(R.id.name_patient_ret);
        t_age = (TextView) findViewById(R.id.age_patient_ret);
        t_addr = (TextView) findViewById(R.id.adr_patient_ret);
        t_kind = (TextView) findViewById(R.id.kind_patient_ret);
        t_diss = (TextView) findViewById(R.id.diss_patient_ret);
        chart_detals = (TextView) findViewById(R.id.view_vital_data);




        /**Button Click Search for patient Report**/
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(ViewReportForDc.this);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                // to get id for patient
                p_id_code = get_id.getText().toString();

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
                        if (get_id.getText().toString().equals(p_id_ret)) {
                            t_hide.setVisibility(View.GONE);
                            l_hide_view.setVisibility(View.VISIBLE);
                            t_name.setText(p_name_ret);
                            t_age.setText(p_age_ret);
                            t_addr.setText(p_addr_ret);
                            t_kind.setText(p_gendr_ret);
                            t_diss.setText(p_diss_ret);
                            t_operation.setText(p_oper_ret);
                            chart_detals.setText("-Pulse: "+p_pulse_ret+"\n"
                                                + "-BI.P: " + p_bi_ret + "\n"
                                                + "-Resp: "+p_resp_ret + "\n"
                                                + "-Temp: "+p_temp_ret + "\n"
                                                + "-Other: "+p_other_ret);
                            t_dr_id.setText(dc_id_old);
                            t_Dr_name.setText(dc_name_old);
                            t_dr_last_date.setText(date_old);
                            t_dr_data.setText("-The Last Patient Examination \n\n\t\t" + "-" + data_examination_old + "\n\n"
                                        + "-The Last Patient Diagnosis \n\n\t\t" + "-" + diagno_old);
                            t_diagno.setText(diagno_old);
                            t_examin.setText(data_examination_old);


                            String before = p_bi_ret.split("/")[0]; // "Before"
                            String after = p_bi_ret.split("/")[1];

                            int i1=Integer.parseInt(before);
                            int i2=Integer.parseInt(after);
                            int i3=Integer.parseInt(p_pulse_ret);
                            int i4=Integer.parseInt(p_resp_ret);
                            int i5=Integer.parseInt(p_temp_ret);

                            /**Pie Chart Code**/
                            List pieData = new ArrayList<>();
                            pieData.add(new SliceValue(i4, Color.GRAY).setLabel("Resp: "+p_resp_ret));
                            pieData.add(new SliceValue(i1, Color.RED).setLabel("BI.P: "+before));
                            pieData.add(new SliceValue(i2, Color.GREEN).setLabel("BI.P: "+after));
                            pieData.add(new SliceValue(i3, Color.BLUE).setLabel("Pulse: "+p_pulse_ret));
                            pieData.add(new SliceValue(i5, Color.LTGRAY).setLabel("Temp: "+p_temp_ret));

                            PieChartData pieChartData = new PieChartData(pieData);
                            pieChartData.setHasLabels(true).setValueLabelTextSize(12);
//        pieChartData.setHasCenterCircle(true).setCenterText1("Vital Processes").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartView.setPieChartData(pieChartData);

                            /** Else IF  field value and Password field value are not match any value stored in database */
                        } else if (!get_id.getText().toString().equals(p_id_ret)) {

                            if ((get_id.getText().toString()).isEmpty()) {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "Please, Enter Patient Id To Start Search ", Toast.LENGTH_SHORT).show();

                            } else {
                                /** Error message */
                                Toast.makeText(getApplicationContext(), "No Patient Data Hear Please Ensure From Patient Id", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                }, 8000);

            }
        });

        /**Button Click to view another patient Report**/
        view_another_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_hide.setVisibility(View.VISIBLE);
                l_hide_view.setVisibility(View.GONE);
                get_id.setText("");
            }
        });

    }
}
