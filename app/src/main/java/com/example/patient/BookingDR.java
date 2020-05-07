package com.example.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.patient.classes.MyRecyclerViewAdapter;

import java.util.ArrayList;

public class BookingDR extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_d_r);
        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("1-Dr Ahmed Omar"+"\n\t\t"+"Congenital Heart Specialists");
        animalNames.add("2-Dr Mohamed Ahmed"+"\n\t\t"+"Pediatric Specialist");
        animalNames.add("3-Dr Amr Wahbah"+"\n\t\t"+"Internist Specialist");
        animalNames.add("4-Dr Omar Ali"+"\n\t\t"+"Neurologists Specialist");


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(BookingDR.this, animalNames);
        adapter.setClickListener(BookingDR.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
      if ((adapter.getItem(position)).equals(adapter.getItem(position))){
          Intent n = new Intent(getApplicationContext(), FirstAppointmentTime.class);
          n.putExtra("name",adapter.getItem(position));
          startActivity(n);
      }
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
