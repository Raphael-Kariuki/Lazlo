package com.example.lazlo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Date;

public class timeTracker extends AppCompatActivity {
MaterialButton startTimer, pauseTimer, resumeTimer, completeTimer;
long startTimerTime, pauseTimerTime, resumeTimerTime, completeTimerTime, duration;
MaterialTextView lostTime;
TextInputEditText lostTimeReasonTxt;
String reason;
ArrayList<timerModel> timerModelArrayList;
int state;
DBHelper dbHelper;
Double randUserId;
SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracker);

        dbHelper = new DBHelper(this);

        //obtain userId
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId =Double.parseDouble(spf.getString("randomUserId", null));


        lostTime = findViewById(R.id.lostTime);

        startTimer = findViewById(R.id.startTimer);
        pauseTimer = findViewById(R.id.pauseTimer);
        resumeTimer = findViewById(R.id.resumeTimer);
        completeTimer = findViewById(R.id.completeTimer);

        pauseTimer.setVisibility(View.INVISIBLE);
        resumeTimer.setVisibility(View.INVISIBLE);
        completeTimer.setVisibility(View.INVISIBLE);

        startTimer.setOnClickListener(view -> {

            state = 0;
            startTimerTime = new Date().getTime();
            lostTime.setText(R.string.timer_loading);

            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.VISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });

        pauseTimer.setOnClickListener(view -> {
            state = 1;
            pauseTimerTime = new Date().getTime();
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.VISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });
        resumeTimer.setOnClickListener(view -> {
            state = 2;
            resumeTimerTime = new Date().getTime();
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });
        completeTimer.setOnClickListener(view -> {


            completeTimerTime = new Date().getTime();
            startTimer.setVisibility(View.VISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.INVISIBLE);


            //create dialog to receive users reason for lost time
            AlertDialog.Builder alertDialog= new AlertDialog.Builder(timeTracker.this);

            //establish a view
            final TextInputEditText lostTimeReason = new TextInputEditText(timeTracker.this);

            //set title
            alertDialog.setTitle("Enter a reason");
            //set view
            alertDialog.setView(lostTimeReason);

            //establish a layout
            LinearLayout linearLayout = new LinearLayout(timeTracker.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            //add view to layout
            linearLayout.addView(lostTimeReason);

            //add layout with view to dialog
            alertDialog.setView(linearLayout);

            //setup user interaction
            alertDialog.setPositiveButton("Save", (dialogInterface, i) -> {
                lostTimeReasonTxt = lostTimeReason;
                reason = lostTimeReasonTxt.getText().toString().trim();
                //process duration
                switch (state){
                    case 0:
                        duration = completeTimerTime - startTimerTime;
                        break;
                    case 2:
                        duration = (pauseTimerTime - startTimerTime) + (completeTimerTime - resumeTimerTime);
                        break;
                }
                lostTime.setText(HouseOfCommons.returnDuration(duration));

                //db hit
                insertTimeTrackerDetails(randUserId,startTimerTime,pauseTimerTime,resumeTimerTime,completeTimerTime,duration,reason);
            });
            alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            //show dialog
            alertDialog.show();


            state = 3;

        });
    }
    private void insertTimeTrackerDetails(Double randUserId,long startTimerTime,long pauseTimerTime,long resumeTimerTime,long completeTimerTime,long duration,String reason){
        boolean b = dbHelper.insertIntoTimeTracker(randUserId,startTimerTime,pauseTimerTime,resumeTimerTime,completeTimerTime,duration,reason);
        if(b){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Nuh", Toast.LENGTH_SHORT).show();
        }

    }
}