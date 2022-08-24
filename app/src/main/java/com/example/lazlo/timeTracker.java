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
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
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

import java.util.Date;


public class timeTracker extends AppCompatActivity {
MaterialButton startTimer, pauseTimer, resumeTimer, completeTimer;
long startTimerTime, pauseTimerTime, resumeTimerTime, completeTimerTime;
public MaterialTextView lostTime, previousLostTime;
TextInputEditText lostTimeReasonTxt;
String reason;
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
        savedInstanceState.putInt("state", state);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstance){
        seconds = savedInstance.getInt("seconds");
        running = savedInstance.getBoolean("running");
        wasRunning = savedInstance.getBoolean("wasRunning");
        state = savedInstance.getInt("state");
    }
    @Override
    protected void onPause() {
        super.onPause();
    System.out.println("System has paused" + wasRunning + running + seconds + state);


    }
    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("System has stopped" + wasRunning + running + seconds+ state);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("System has restarted" + wasRunning + running + seconds+ state);

    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("System has started" + wasRunning + running + seconds+ state);

    }
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("System has resumed" + wasRunning + running + seconds+ state);
        if (state == 5){
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.VISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        }else if (state == 1){
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.VISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        }else if (state == 2){
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        }else if (state == 3){
            startTimer.setVisibility(View.VISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.INVISIBLE);
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
        //seconds = 0;
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

   /*
   *  private void getDataFromService(){
        Handler mainHandler = new Handler(Looper.getMainLooper());
        ResultReceiver receiver = new ResultReceiver(mainHandler){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData){
                if (resultCode == timerService.RESULT_OK){
                    String data = resultData.getString(timerService.EXTRA_DATA);
                    Log.i("TAG","Received data : " + data);
                }
            }
        };
        Intent intent = new Intent(timerService.ACTION_GET_DATA);
        intent.putExtra(timerService.EXTRA_RECEIVER, receiver);
        sendBroadcast(intent);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_tracker);

        /*
        Intent intent = new Intent(this, timerService.class);
        startService(intent);
       */
        runTimer();

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
        Intent startService = new Intent(this, timerService.class);
        startService(startService);

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

        startTimer.setVisibility(View.VISIBLE);
        pauseTimer.setVisibility(View.INVISIBLE);
        resumeTimer.setVisibility(View.INVISIBLE);
        completeTimer.setVisibility(View.INVISIBLE);

        startTimer.setOnClickListener(view -> {
            onClickStart(view);
            state = 5;

            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.VISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });

        pauseTimer.setOnClickListener(view -> {
            onClickPause(view);
            state = 1;
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.VISIBLE);
            completeTimer.setVisibility(View.VISIBLE);

        });
        resumeTimer.setOnClickListener(view -> {
            onClickResume(view);
            state = 2;
            startTimer.setVisibility(View.INVISIBLE);
            pauseTimer.setVisibility(View.INVISIBLE);
            resumeTimer.setVisibility(View.INVISIBLE);
            completeTimer.setVisibility(View.VISIBLE);
        });
        completeTimer.setOnClickListener(view -> {
            state = 3;
            onClickComplete(view);

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
            alertDialog.setCancelable(false);

            //setup user interaction
            alertDialog.setPositiveButton("Save", (dialogInterface, i) -> {
                lostTimeReasonTxt = lostTimeReason;
                reason = lostTimeReasonTxt.getText().toString().trim();

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                stopWatch = String.format(HouseOfCommons.locale,"%d:%02d:%02d",hours, minutes, secs);
                previousLostTime.setText(stopWatch);

                //db hit
                insertTimeTrackerDetails(randUserId,new Date().getTime(),seconds,reason);
            });
            alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });

            //show dialog
            alertDialog.show();



        });
    }
    private void insertTimeTrackerDetails(Double randUserId,long dateToday,long duration,String reason){
        boolean b = dbHelper.insertIntoTimeTracker(randUserId,dateToday,duration,reason);
        if(b){
            Toast.makeText(this, "Success" + duration , Toast.LENGTH_SHORT).show();
            seconds = 0;
            state = 4;
        }else{
            Toast.makeText(this, "Nuh", Toast.LENGTH_SHORT).show();
        }

    }
}