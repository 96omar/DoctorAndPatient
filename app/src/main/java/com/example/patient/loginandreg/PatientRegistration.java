package com.example.patient.loginandreg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.patient.R;
import com.firebase.client.Firebase;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PatientRegistration extends AppCompatActivity {
    //declaring variables
    private Spinner spin;
    Button reg,sign_in;
    EditText p_id,p_name,p_age,p_addrs,p_pass,p_repass,p_diss,p_operation;
    //defining firebase
    Firebase firebase;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;
    boolean invalid = false;
    private NotificationManager mNotificationManager;
    String r_total, cha;
    char first_dc_litter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        //assign ids with xml
        reg = (Button) findViewById(R.id.p_reg_btn);
        sign_in = (Button) findViewById(R.id.p_login_btn);
        //p_id = (EditText) findViewById(R.id.p_id_text);
        p_name = (EditText) findViewById(R.id.p_name_text);
        p_age = (EditText) findViewById(R.id.p_age_text);
        p_addrs = (EditText) findViewById(R.id.p_add_text);
        p_pass = (EditText) findViewById(R.id.p_pass_text);
        p_repass = (EditText) findViewById(R.id.p_repass_text);
        p_diss = (EditText) findViewById(R.id.p_diss_text);
        p_operation = (EditText) findViewById(R.id.p_operation_text);

        /**spiner code */
        spin = (Spinner) findViewById(R.id.p_kind_spinner);
        List<String> list = new ArrayList<String>();
        String item1 = "Male";
        String item2 = "Female";
        list.add(item1);
        list.add(item2);
        //spin.setBackgroundColor(c);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down);
        spin.setAdapter(dataAdapter);

        // get random num for id
        final int min = 20;
        final int max = 80;
        final int random = new Random().nextInt((max - min) + 1) + min;
        final int random2 = new Random().nextInt((max - min) + 1) + min;
        final int random3 = new Random().nextInt((max - min) + 1) + min;
        final int random4 = new Random().nextInt((max - min) + 1) + min;
        int sum = random + random2 + random3 + random4;
        final String r_sum = String.valueOf(sum).toString();


        //Toast.makeText(this, random, Toast.LENGTH_LONG).show();

        /**starting validation code*/
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        /**adding validation to edittexts*/
        awesomeValidation.addValidation(this, R.id.p_name_text, "[a-zA-Z\\s]+", R.string.nameerror);
        //awesomeValidation.addValidation(this, R.id.email_edit_textReg, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.p_pass_text, "((?=.*\\d)(?=.*[a-z]).{6,20})", R.string.passworderror);
        //awesomeValidation.addValidation(this, R.id.phone_number_edit_text, "^[+]?[0-9]{11}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.p_repass_text, R.id.p_pass_text, R.string.err_password_confirmation);
        awesomeValidation.addValidation(this, R.id.p_age_text, Range.closed(20, 70), R.string.invalid_age_trusted);
//        awesomeValidation.addValidation(this, R.id.p_diss_text, "[a-zA-Z\\s]+", R.string.err_major);
        awesomeValidation.addValidation(this, R.id.p_add_text, "[a-zA-Z\\s]+", R.string.addresserror);



        /**firebase code**/
        Firebase.setAndroidContext(this);
        String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        firebase = new Firebase("https://patient-10742.firebaseio.com/Doc");


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        !(p_name.getText().toString()).equalsIgnoreCase(null)
                                && (p_name.getText().toString()).length() > 0
                                && !(p_pass.getText().toString()).equalsIgnoreCase(null)
                                && (p_pass.getText().toString()).length() > 0
                                && !(p_repass.getText().toString()).equalsIgnoreCase(null)
                                && (p_repass.getText().toString()).length() > 0
                                && !(p_addrs.getText().toString()).equalsIgnoreCase(null)
                                && (p_addrs.getText().toString()).length() > 0
                                && !(p_age.getText().toString()).equalsIgnoreCase(null)
                                && (p_age.getText().toString()).length() > 0
                                && (Objects.equals(p_pass.getText().toString(), p_repass.getText().toString()))
                                && awesomeValidation.validate()) {
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
                    firebase_pass.setValue(p_pass.getText().toString());

                    Firebase firebase_major = firebase.child("P_Address");
                    firebase_major.setValue(p_addrs.getText().toString());

                    Firebase firebase_age = firebase.child("P_Age");
                    firebase_age.setValue(p_age.getText().toString());

                    Firebase firebase_gender = firebase.child("P_Gender");
                    firebase_gender.setValue(String.valueOf(spin.getSelectedItem()));

                    if (!(p_diss.getText().toString()).equalsIgnoreCase(null)
                            && (p_diss.getText().toString()).length() > 0
                            &&!(p_operation.getText().toString()).equalsIgnoreCase(null)
                            && (p_operation.getText().toString()).length() > 0){

                        Firebase firebase_diss = firebase.child("P_Chronic_Disease");
                        firebase_diss.setValue(p_diss.getText().toString());

                        Firebase firebase_oper = firebase.child("P_Previous_Operations");
                        firebase_oper.setValue(p_operation.getText().toString());
                    }

                    Toast.makeText(PatientRegistration.this, "Registration Success, Save You\'r Id to login", Toast.LENGTH_SHORT).show();
                    addNotification();
                    Intent n = new Intent(getApplicationContext(), PatientLogin.class);
                    startActivity(n);
                    finish();

                } else if (!awesomeValidation.validate()) {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Data is not valid .. please check your data",
                            Toast.LENGTH_SHORT).show();
                    /** this elses ensure that string is not = null and length == 0 to register*/
                } else {
                    Toast.makeText(PatientRegistration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PatientRegistration.this, PatientRegistration.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), PatientLogin.class);
                startActivity(n);
                finish();
            }
        });

    }
    private void addNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        Intent ii = new Intent(this, DcLogin.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("ID is : " + r_total);
        bigText.setBigContentTitle("Take you\'r ID To Login");
        bigText.setSummaryText("ID Notification");

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
