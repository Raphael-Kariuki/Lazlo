package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class dailyReview extends AppCompatActivity {
MaterialButton startTimer, pauseTimer, resumeTimer, completeTimer;
public MaterialTextView lostTime, previousLostTime, shiftLengthTextView, breaksTextView
        , downtimeTextView, idealCycleTimeTextView, totalCountTextView, rejectCountTextView;
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
MaterialAutoCompleteTextView sleepHrsAutoCompleteTextView, sleepMinAutoCompleteTextView;
MaterialButton btnSubmitHrsOfSleep;
MaterialTextView showHrsOfSleepTxtView,hoursOfSleepReceivedHelper;
Integer selectedHrsOfSleep,selectedMinOfSleep;
LinearLayout durationOfSleepLayout,durationOfSleepLayoutContainer,trackLostTimeLayout;
Chip trackLostTime, trackBreaksChip;
TextInputLayout sleepHrsTextInputLayout, sleepMinTextInputLayout;

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
    private void showTrackSleepLayout(Double randUserId, LinearLayout sleepLayout){
        Cursor lastRecordDateTimeCursor = null;
        try {
            lastRecordDateTimeCursor = dbHelper.getHoursOfSleepDetails(randUserId);
        }catch (Exception e){
            e.printStackTrace();
        }
        String lastDateTime = null;
        if (lastRecordDateTimeCursor != null && lastRecordDateTimeCursor.moveToLast()){
            lastDateTime = lastRecordDateTimeCursor.getString(lastRecordDateTimeCursor.getColumnIndexOrThrow("DateToRecord"));
        }
        System.out.println(lastDateTime);
        if (lastDateTime != null && Long.parseLong(lastDateTime) > 0){
            long twelveHoursLimit = Long.parseLong(lastDateTime) + (12 * 3600000);
            long dateNow = new Date().getTime();
            if ( dateNow < twelveHoursLimit){
                Log.i("Limit","Not passed");
                sleepLayout.setVisibility(View.GONE);
            }else{
                Log.i("Limit","Passed");
                sleepLayout.setVisibility(View.VISIBLE);
            }
        }else if(lastDateTime == null ){
            sleepLayout.setVisibility(View.VISIBLE);
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_review);

        dbHelper = new DBHelper(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Daily review");
        //obtain userId
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        sleepHrsTextInputLayout = findViewById(R.id.sleepHrsTextLayout);
        sleepMinTextInputLayout = findViewById(R.id.sleepMinTextLayout);

        durationOfSleepLayoutContainer = findViewById(R.id.durationOfSleepLayoutContainer);
        durationOfSleepLayout = findViewById(R.id.durationOfSleepLayout);
        durationOfSleepLayoutContainer.setVisibility(View.GONE);
        trackLostTimeLayout = findViewById(R.id.trackLostTimeLayout);
        trackLostTimeLayout.setVisibility(View.VISIBLE);

        //chips to navigate between trackingLostTime and trackingHrsOfSleep
        trackLostTime = findViewById(R.id.trackLostTimeChip);
        trackBreaksChip = findViewById(R.id.trackBreaksChip);


        sleepHrsAutoCompleteTextView = findViewById(R.id.sleepHrs);
        sleepMinAutoCompleteTextView =findViewById(R.id.sleepMin);
        btnSubmitHrsOfSleep = findViewById(R.id.submitHrsOfSleep);
        showHrsOfSleepTxtView = findViewById(R.id.hoursOfSleepReceivedView);
        hoursOfSleepReceivedHelper = findViewById(R.id.hoursOfSleepReceivedHelper);

        shiftLengthTextView = findViewById(R.id.shiftLength);
        breaksTextView = findViewById(R.id.breaks);
        downtimeTextView = findViewById(R.id.downtime);
        idealCycleTimeTextView = findViewById(R.id.idealCycleTime);
        totalCountTextView =findViewById(R.id.totalCount);
        rejectCountTextView = findViewById(R.id.rejectCount);

        timeTrackerDrawerLayout = findViewById(R.id.timeTrackerDrawerLayout);

        //supply arguments to function to hide sleep layout if 12 hrs limit hasn't been surpassed
        showTrackSleepLayout(randUserId,durationOfSleepLayout);







        //populate sleepHrs and sleepMin autoCompleteTextViews
        ArrayAdapter<CharSequence> sleepHrsAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.hoursOfSleep, android.R.layout.simple_dropdown_item_1line);
        sleepHrsAutoCompleteTextView.setAdapter(sleepHrsAdapter);
        ArrayAdapter<CharSequence> sleepMinAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.minutesOfSleep,android.R.layout.simple_dropdown_item_1line);
        sleepMinAutoCompleteTextView.setAdapter(sleepMinAdapter);

        //obtain hrs and min of sleep
        sleepHrsAutoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> selectedHrsOfSleep = Integer.parseInt(String.valueOf(adapterView.getItemAtPosition(i))));
        sleepMinAutoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> selectedMinOfSleep = Integer.parseInt(String.valueOf(adapterView.getItemAtPosition(i))));

        trackLostTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                durationOfSleepLayoutContainer.setVisibility(View.GONE);
                trackLostTimeLayout.setVisibility(View.VISIBLE);
            }
        });
        trackBreaksChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (durationOfSleepLayoutContainer.getVisibility() == View.GONE){
                    String text="Tracking of sleeping hours can only be done after a 12 hour limit";
                    String hrUnits,minUnits;
                    String hoursOfSleep = null;
                    long hrs = 0;
                    long min = 0;

                    Cursor hoursOfSleepCursor = null;
                    try {
                        hoursOfSleepCursor = dbHelper.getHoursOfSleepDetails(randUserId);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (hoursOfSleepCursor != null && hoursOfSleepCursor.moveToLast()){
                        hoursOfSleep = hoursOfSleepCursor.getString(hoursOfSleepCursor.getColumnIndexOrThrow("HoursOfSleep"));
                    }
                    Log.i("sleep",hoursOfSleep);
                    if (hoursOfSleep != null && Long.parseLong(hoursOfSleep) >= 3600){
                        hrs = Long.parseLong(hoursOfSleep) / 3600;
                        min = (Long.parseLong(hoursOfSleep ) % 3600) / 60;
                    }else if(hoursOfSleep != null && Long.parseLong(hoursOfSleep) > 60){
                        min = Long.parseLong(hoursOfSleep ) % 3600;
                    }
                    

                    if (hrs > 1){
                        hrUnits = "Hours";
                    }else{
                        hrUnits = "Hour";
                    }
                    if (min > 1){
                        minUnits = "Minutes";
                    }else{
                        minUnits = "Minute";
                    }


                    Log.i("Post visible", "view");
                    hoursOfSleepReceivedHelper.setText(String.format(HouseOfCommons.locale,"%s\n%s %d %s %d %s",
                            text,"Time slept today : ",hrs,hrUnits,min,minUnits));
                    durationOfSleepLayoutContainer.setVisibility(View.VISIBLE);
                    trackLostTimeLayout.setVisibility(View.GONE);
                    hoursOfSleepReceivedHelper.setVisibility(View.VISIBLE);

                }else {
                    trackLostTimeLayout.setVisibility(View.GONE);
                    durationOfSleepLayoutContainer.setVisibility(View.VISIBLE);
                }

            }
        });



        btnSubmitHrsOfSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String hrsOfSleep_str = sleepHrsAutoCompleteTextView.getText().toString().trim();
                String minOfSleep_str = sleepMinAutoCompleteTextView.getText().toString().trim();


                if (!hrsOfSleep_str.isEmpty()){
                    if (minOfSleep_str.isEmpty()){
                        minOfSleep_str = "0";
                    }

                    long hrsOfSleep_long = Long.parseLong(hrsOfSleep_str) * 3600;
                    long minOfSleep = Long.parseLong(minOfSleep_str) * 60;
                    long durationOfSleep = hrsOfSleep_long + minOfSleep;

                    boolean success = false;

                    try {
                        success = dbHelper.insertHoursOfSleep(randUserId,new Date().getTime(),durationOfSleep);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    String hrUnits;
                    String minUnits = "Minutes";

                    if (hrsOfSleep_long > 1){
                        hrUnits = "Hours";
                    }else{
                        hrUnits = "Hour";
                    }

                    if (success){
                        showHrsOfSleepTxtView.setVisibility(View.VISIBLE);
                        showHrsOfSleepTxtView.setText(String.format(HouseOfCommons.locale,"%d %s %d %s",selectedHrsOfSleep,hrUnits, selectedMinOfSleep, minUnits ));
                        durationOfSleepLayout.setVisibility(View.GONE);
                    }
                }else{
                    sleepHrsTextInputLayout.setErrorEnabled(true);
                    sleepHrsTextInputLayout.setError("Press and choose a number from the dropdown");
                }


            }
        });


       
        breaksTextView.setText(getBreaksFromHoursOfSleep(randUserId));
        String downTime = getDowntimeFromTimeTracker(randUserId, obtainDayRange());
        downtimeTextView.setText(String.format((HouseOfCommons.locale),"%s : %s","Downtime during the day",downTime));
        int TotalTasksPerDay = getCountOfTotalTasksPerDay(randUserId,obtainDayRange());
        totalCountTextView.setText(String.format((HouseOfCommons.locale),"%s : %d %s","Total tasks of the day",TotalTasksPerDay,"tasks"));
        int PendingTasksPerDay = getCountOfPendingTasksPerDay(randUserId, obtainDayRange());
        rejectCountTextView.setText(String.format((HouseOfCommons.locale),"%s : %d %s","Pending tasks of the day",PendingTasksPerDay,"tasks"));


        runTimer();



       

        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        timeTrackerActionBarDrawerToggle = new ActionBarDrawerToggle(this,timeTrackerDrawerLayout,R.string.open_drawer,R.string.close_drawer);

        timeTrackerDrawerLayout.addDrawerListener(timeTrackerActionBarDrawerToggle);
        timeTrackerActionBarDrawerToggle.syncState();


        timeTrackerNavigationView = findViewById(R.id.timeTrackerNavigationView);
        timeTrackerNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_account:
                    startActivity(new Intent(getApplicationContext(), editAccount.class));
                    break;
                case R.id.nav_dashboard:
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                    break;
                case R.id.nav_timeTracker:
                    startActivity(new Intent(getApplicationContext(), dailyReview.class));
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
        });

        //restore saved instance when page is resumed
        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        Intent startService = new Intent(this, timerService.class);
        startService(startService);



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
            AlertDialog.Builder alertDialog= new AlertDialog.Builder(dailyReview.this);

            //establish a view
            final TextInputEditText lostTimeReason = new TextInputEditText(dailyReview.this);

            //set title
            alertDialog.setTitle("Enter a reason");
            //set view
            alertDialog.setView(lostTimeReason);

            //establish a layout
            LinearLayout linearLayout = new LinearLayout(dailyReview.this);
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
            alertDialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

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
    private LocalDateTime[] obtainDayRange(){
        Calendar calendar = Calendar.getInstance(HouseOfCommons.locale);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int dDay = calendar.get(Calendar.DAY_OF_MONTH);
        int yYear = calendar.get(Calendar.YEAR);

        String startOfDay = String.format(HouseOfCommons.locale,"%02d-%02d-%d%s%02d:%02d",dDay,mMonth,yYear," ",0,1);
        String endOfDay = String.format(HouseOfCommons.locale,"%02d-%02d-%d%s%02d:%02d",dDay,mMonth,yYear," ",23,59);

        LocalDateTime[] dayRange = new LocalDateTime[2];
        dayRange[0] = HouseOfCommons.stringToDate(startOfDay);
        dayRange[1] = HouseOfCommons.stringToDate(endOfDay);

        return dayRange;

    }
    private int getCountOfTotalTasksPerDay(Double randUserId, LocalDateTime[] dayRange){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        Cursor toTalTasksPerDayCountCursor = null;
        Collections.addAll(localDateTimes, dayRange);
        System.out.println(localDateTimes.get(0) +""+ localDateTimes.get(1));
        try {
            toTalTasksPerDayCountCursor = dbHelper.getCountOfTasksPerDay(randUserId,localDateTimes.get(0),localDateTimes.get(1));
        }catch (Exception e){
            e.printStackTrace();
        }
        int TotalTasksPerDay = 0;
        if (toTalTasksPerDayCountCursor != null && toTalTasksPerDayCountCursor.moveToFirst()){
            TotalTasksPerDay = toTalTasksPerDayCountCursor.getInt(toTalTasksPerDayCountCursor.getColumnIndexOrThrow("count"));
            toTalTasksPerDayCountCursor.close();
        }
        return TotalTasksPerDay;
    }
    private int getCountOfCompletedTasksPerDay(Double randUserId, LocalDateTime[] dayRange){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        Collections.addAll(localDateTimes, dayRange);
        Cursor toTalCompletedTasksPerDayCount = null;
        try {
            toTalCompletedTasksPerDayCount = dbHelper.getCountOfCompletedTasksPerDay(randUserId,localDateTimes.get(0),localDateTimes.get(1));
            
        }catch (Exception e){
            e.printStackTrace();
        }
        int TotalCompletedTasksPerDay = 0;
        if (toTalCompletedTasksPerDayCount != null && toTalCompletedTasksPerDayCount.moveToFirst()){
            TotalCompletedTasksPerDay = toTalCompletedTasksPerDayCount.getInt(toTalCompletedTasksPerDayCount.getColumnIndexOrThrow("count"));
            toTalCompletedTasksPerDayCount.close();
        }
        return TotalCompletedTasksPerDay;
    }
    private int getCountOfPendingTasksPerDay(Double randUserId, LocalDateTime[] dayRange){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        Collections.addAll(localDateTimes, dayRange);
        Cursor toTalPendingTasksPerDayCount = dbHelper.getCountOfPendingTasksPerDay(randUserId,localDateTimes.get(0),localDateTimes.get(1));
        int TotalPendingTasksPerDay = 0;
        if (toTalPendingTasksPerDayCount != null && toTalPendingTasksPerDayCount.moveToFirst()){
            TotalPendingTasksPerDay = toTalPendingTasksPerDayCount.getInt(toTalPendingTasksPerDayCount.getColumnIndexOrThrow("count"));
            toTalPendingTasksPerDayCount.close();
        }
        return TotalPendingTasksPerDay;
    }
    private String getDowntimeFromTimeTracker(Double randUserId, LocalDateTime[] dayRange){
        //TODO:create review table that includes last time review was done, the current time; to be used in establishing the review period
        /*Review needs to be done daily, however on days that one is not able to do so. Currently assign 0, for now
        Future iteration might check alarm to see what time one slept to obtain breaksTextView
        * */
        ArrayList<Long> dayRangeInMillis = new ArrayList<>();
        for (LocalDateTime range: dayRange){
            dayRangeInMillis.add(HouseOfCommons.getDateFromLocalDateTime(range).getTime());
        }
        Cursor downtimeCursor = null;
        try {
            downtimeCursor = dbHelper.getDowntimeDurationFromTimeTracker(randUserId,dayRangeInMillis.get(0), dayRangeInMillis.get(1));
        }catch (Exception e){
            e.printStackTrace();
        }
        int downTime = 0;
        while (downtimeCursor != null && downtimeCursor.moveToNext()){
            downTime = downTime + downtimeCursor.getInt(downtimeCursor.getColumnIndexOrThrow("lostDuration"));
            
        }
        downtimeCursor.close();

        int formattedLostDurationMin,formattedLostDurationSec, formattedLostDurationHrs;
        String formattedLostDuration, hourUnits;

        if(downTime > 3600 ){
            formattedLostDurationHrs = downTime /3600;
            if (formattedLostDurationHrs > 1){
                hourUnits = "Hours";
            }else {
                hourUnits = "Hour";
            }
            formattedLostDurationMin = downTime / 60;
            formattedLostDurationSec = downTime % 60;
            formattedLostDuration = String.format(HouseOfCommons.locale,"%d %s %d %s %d %s",formattedLostDurationHrs, hourUnits,formattedLostDurationMin,"Minute", formattedLostDurationSec,"Seconds");
            return formattedLostDuration;
        }if (downTime > 60){
        formattedLostDurationMin = downTime / 60;
        formattedLostDurationSec = downTime % 60;
        formattedLostDuration = String.format(HouseOfCommons.locale, "%d %s %d %s", formattedLostDurationMin, "Minute", formattedLostDurationSec, "Seconds");
        return formattedLostDuration;
        }
        else
            formattedLostDuration = String.format(HouseOfCommons.locale,"%d %s",downTime,"seconds");
            return formattedLostDuration;
    }
    private String getBreaksFromHoursOfSleep(Double randUserId){
        String hrUnits,minUnits;
        String hoursOfSleep = null;
        long hrs = 0;
        long min = 0;

        Cursor hoursOfSleepCursor = null;
        try {
            hoursOfSleepCursor = dbHelper.getHoursOfSleepDetails(randUserId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (hoursOfSleepCursor != null && hoursOfSleepCursor.moveToLast()){
            hoursOfSleep = hoursOfSleepCursor.getString(hoursOfSleepCursor.getColumnIndexOrThrow("HoursOfSleep"));
        }
        Log.i("sleep",hoursOfSleep);
        if (hoursOfSleep != null && Long.parseLong(hoursOfSleep) >= 3600){
            hrs = Long.parseLong(hoursOfSleep) / 3600;
            min = (Long.parseLong(hoursOfSleep ) % 3600) / 60;
        }else if(hoursOfSleep != null && Long.parseLong(hoursOfSleep) > 60){
            min = Long.parseLong(hoursOfSleep ) % 3600;
        }


        if (hrs > 1){
            hrUnits = "Hours";
        }else{
            hrUnits = "Hour";
        }
        if (min > 1){
            minUnits = "Minutes";
        }else{
            minUnits = "Minute";
        }

        return String.format(HouseOfCommons.locale,"%s %d %s %d %s","Total duration of breaks : ",hrs,hrUnits,min,minUnits);
    }
}