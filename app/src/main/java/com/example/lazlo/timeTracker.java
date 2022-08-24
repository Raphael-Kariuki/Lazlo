package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Date;

public class timeTracker extends AppCompatActivity {
MaterialButton startTimer, pauseTimer, resumeTimer, completeTimer;
long startTimerTime, pauseTimerTime, resumeTimerTime, completeTimerTime, duration;
MaterialTextView lostTime, previousLostTime;
TextInputEditText lostTimeReasonTxt;
String reason;
ArrayList<timerModel> timerModelArrayList;
int state;
DBHelper dbHelper;
Double randUserId;
SharedPreferences spf;
private int seconds;
private boolean running, wasRunning;
    String stopWatch;

    DrawerLayout timeTrackerDrawerLayout;
    NavigationView timeTrackerNavigationView;
    ActionBarDrawerToggle timeTrackerActionBarDrawerToggle;

    //store page instance when page is paused or destroyed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (!wasRunning){
            wasRunning = running;
            running = false;
        }else {
            wasRunning = running;
            running = true;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning){
            running = true;
        }
    }



    //do nothing on back pressed
    @Override
    public void onBackPressed(){}
    public void onClickStart(View view){
        running = true;
    }
    public void onClickPause(View view){
        running = false;
    }
    public void onClickResume(View view){
        running = true;
    }
    public  void onClickComplete(View view){
        running = false;
        seconds = 0;
    }
    private void runTimer(){
        final Handler handler = new Handler();
        // Call the post() method, passing in a new Runnable.
        // The post() method processes code without a delay,so the code in the Runnable will run almost immediately.
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                stopWatch = String.format(HouseOfCommons.locale,"%d:%02d:%02d",hours, minutes, secs);
                lostTime.setText(stopWatch);

                if (running){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        if (timeTrackerActionBarDrawerToggle.onOptionsItemSelected(menuItem)){
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracker);



        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Time tracker");

        timeTrackerDrawerLayout = findViewById(R.id.timeTrackerDrawerLayout);

        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        timeTrackerActionBarDrawerToggle = new ActionBarDrawerToggle(this,timeTrackerDrawerLayout,R.string.open_drawer,R.string.close_drawer);

        timeTrackerDrawerLayout.addDrawerListener(timeTrackerActionBarDrawerToggle);
        timeTrackerActionBarDrawerToggle.syncState();


        timeTrackerNavigationView = findViewById(R.id.timeTrackerNavigationView);
        timeTrackerNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_account:
                        startActivity(new Intent(getApplicationContext(), editAccount.class));
                        break;
                    case R.id.nav_dashboard:
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        break;
                    case R.id.nav_timeTracker:
                        startActivity(new Intent(getApplicationContext(),timeTracker.class));
                        break;
                    case R.id.nav_addTasks:
                        startActivity(new Intent(getApplicationContext(),AddTasks.class));
                        break;
                    case R.id.nav_pendingTasks:
                        startActivity(new Intent(getApplicationContext(), tasks.class));
                        break;
                    case R.id.nav_completedTasks:
                        startActivity(new Intent(getApplicationContext(), completed.class));
                        break;
                    case R.id.nav_draftTasks:
                        startActivity(new Intent(getApplicationContext(),DraftTasks.class));
                        break;
                    case R.id.nav_security:
                        startActivity(new Intent(getApplicationContext(), inHousePasswordReset.class));
                        break;
                    case R.id.nav_logout:
                        SharedPreferences prf;
                        prf = getSharedPreferences("user_details",MODE_PRIVATE);
                        Intent i = new Intent(getApplicationContext(),Login.class);
                        SharedPreferences.Editor editor = prf.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(i);
                        break;

                }
                return false;
            }
        });

        //restore saved instance when page is resumed
        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();

        dbHelper = new DBHelper(this);

        //obtain userId
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId =Double.parseDouble(spf.getString("randomUserId", null));


        lostTime = findViewById(R.id.lostTime);
        previousLostTime = findViewById(R.id.previousLostTime);

        startTimer = findViewById(R.id.startTimer);
        pauseTimer = findViewById(R.id.pauseTimer);
        resumeTimer = findViewById(R.id.resumeTimer);
        completeTimer = findViewById(R.id.completeTimer);

        pauseTimer.setVisibility(View.INVISIBLE);
        resumeTimer.setVisibility(View.INVISIBLE);
        completeTimer.setVisibility(View.INVISIBLE);

        startTimer.setOnClickListener(view -> {
            onClickStart(view);
            state = 0;
            startTimerTime = new Date().getTime();

            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.VISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });

        pauseTimer.setOnClickListener(view -> {
            onClickPause(view);
            state = 1;
            pauseTimerTime = new Date().getTime();
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.VISIBLE);
            completeTimer.setVisibility(View.VISIBLE);

            duration = (pauseTimerTime - startTimerTime);

            lostTime.setText(HouseOfCommons.returnDuration(duration));
        });
        resumeTimer.setOnClickListener(view -> {
            onClickResume(view);
            state = 2;
            resumeTimerTime = new Date().getTime();
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });
        completeTimer.setOnClickListener(view -> {

            onClickComplete(view);

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
                        state = 3;
                        break;
                    case 2:
                        duration = (pauseTimerTime - startTimerTime) + (completeTimerTime - resumeTimerTime);
                        state = 3;
                        break;
                }
                previousLostTime.setText(String.format((HouseOfCommons.locale),"%s%s","Previous lost time: ",HouseOfCommons.returnDuration(duration)));

                //db hit
                insertTimeTrackerDetails(randUserId,startTimerTime,pauseTimerTime,resumeTimerTime,completeTimerTime,duration,reason);
            });
            alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            //show dialog
            alertDialog.show();



        });
    }
    private void insertTimeTrackerDetails(Double randUserId,long startTimerTime,long pauseTimerTime,long resumeTimerTime,long completeTimerTime,long duration,String reason){
        boolean b = dbHelper.insertIntoTimeTracker(randUserId,startTimerTime,pauseTimerTime,resumeTimerTime,completeTimerTime,duration,reason);
        if(b){
            Toast.makeText(this, "Success" + duration , Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Nuh", Toast.LENGTH_SHORT).show();
        }

    }
}