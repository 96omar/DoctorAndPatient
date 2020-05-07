package com.example.patient.loginandreg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.patient.R;
import com.example.patient.ReportAdd;
import com.firebase.client.Firebase;
import com.google.common.collect.Range;

import org.checkerframework.checker.units.UnitsTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DcRegsitration extends AppCompatActivity {

    //declaring variables
    private Spinner spin;
    Button reg, sign_in;
    EditText dc_id, dc_name, dc_age, dc_major, dc_pass, dc_repass;
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
        setContentView(R.layout.activity_dc_regsitration);

        //assign ids with xml
        reg = (Button) findViewById(R.id.dc_reg_btn);
        sign_in = (Button) findViewById(R.id.dc_login_btn);
        //dc_id = (EditText) findViewById(R.id.dc_id_text);
        dc_name = (EditText) findViewById(R.id.dc_name_text);
        dc_age = (EditText) findViewById(R.id.dc_age_text);
        dc_major = (EditText) findViewById(R.id.dc_major_text);
        dc_pass = (EditText) findViewById(R.id.dc_pass_text);
        dc_repass = (EditText) findViewById(R.id.dc_repass_text);


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
        awesomeValidation.addValidation(this, R.id.dc_name_text, "[a-zA-Z\\s]+", R.string.nameerror);
        //awesomeValidation.addValidation(this, R.id.email_edit_textReg, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.dc_pass_text, "((?=.*\\d)(?=.*[a-z]).{6,20})", R.string.passworderror);
        //awesomeValidation.addValidation(this, R.id.phone_number_edit_text, "^[+]?[0-9]{11}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.dc_repass_text, R.id.dc_pass_text, R.string.err_password_confirmation);
        awesomeValidation.addValidation(this, R.id.dc_age_text, Range.closed(20, 70), R.string.invalid_age_trusted);
        awesomeValidation.addValidation(this, R.id.dc_major_text, "[a-zA-Z\\s]+", R.string.err_major);


        /**spiner code */
        spin = (Spinner) findViewById(R.id.dc_kind_spinner);
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

        /**firebase code**/
        Firebase.setAndroidContext(this);
        String deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        firebase = new Firebase("https://patient-10742.firebaseio.com/Doc");


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (
                        !(dc_name.getText().toString()).equalsIgnoreCase(null)
                                && (dc_name.getText().toString()).length() > 0
                                && !(dc_pass.getText().toString()).equalsIgnoreCase(null)
                                && (dc_pass.getText().toString()).length() > 0
                                && !(dc_repass.getText().toString()).equalsIgnoreCase(null)
                                && (dc_repass.getText().toString()).length() > 0
                                && !(dc_major.getText().toString()).equalsIgnoreCase(null)
                                && (dc_major.getText().toString()).length() > 0
                                && !(dc_age.getText().toString()).equalsIgnoreCase(null)
                                && (dc_age.getText().toString()).length() > 0
                                && (Objects.equals(dc_pass.getText().toString(), dc_repass.getText().toString()))
                                && awesomeValidation.validate()) {
                    // get first character from name
                    first_dc_litter = dc_name.getText().charAt(0);
                    cha = String.valueOf(first_dc_litter).toString();
                    r_total = cha + "_" + r_sum;

                    firebase = new Firebase("https://patient-10742.firebaseio.com/Doc" + r_total);

                    Firebase firebase_id = firebase.child("Dc_ID");
                    firebase_id.setValue(r_total);


                    Firebase firebase_name = firebase.child("Dc_Name");
                    firebase_name.setValue(dc_name.getText().toString());

                    Firebase firebase_pass = firebase.child("Dc_Password");
                    firebase_pass.setValue(dc_pass.getText().toString());

                    Firebase firebase_major = firebase.child("Dc_Major");
                    firebase_major.setValue(dc_major.getText().toString());

                    Firebase firebase_age = firebase.child("Dc_Age");
                    firebase_age.setValue(dc_age.getText().toString());

                    Firebase firebase_gender = firebase.child("Dc_Gender");
                    firebase_gender.setValue(String.valueOf(spin.getSelectedItem()));

                    Toast.makeText(DcRegsitration.this, "Registration Success, Save You\'r Id to login", Toast.LENGTH_SHORT).show();
                    addNotification();
                    Intent n = new Intent(getApplicationContext(), DcLogin.class);
                    startActivity(n);
                    finish();

                } else if (!awesomeValidation.validate()) {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Data is not valid .. please check your data",
                            Toast.LENGTH_SHORT).show();
                    /** this elses ensure that string is not = null and length == 0 to register*/
                } else {
                    Toast.makeText(DcRegsitration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DcRegsitration.this, DcRegsitration.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(getApplicationContext(), DcLogin.class);
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
        bigText.bigText("Dc ID is : " + r_total);
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
