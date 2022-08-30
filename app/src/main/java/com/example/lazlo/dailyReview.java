package com.example.lazlo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class dailyReview extends AppCompatActivity {

MaterialButton startTimer, pauseTimer, resumeTimer, completeTimer,btnSubmitHrsOfSleep,proceedToGetReportOfDaysReport,
        submitShiftStartAndEndBtn,reviewYourDayBtn, timeTrackerCardBtn;
MaterialTextView lostTime, previousLostTime, shiftLengthTextView, breaksTextView
        , downtimeTextView, idealCycleTimeTextView, totalCountTextView, rejectCountTextView,showHrsOfSleepTxtView,hoursOfSleepReceivedHelper;
TextInputEditText lostTimeReasonTxt;
String reason;
int state,seconds,finalTotalTasks,finalPendingTasksPerDay,selectedHrsOfSleep,selectedMinOfSleep;
DBHelper dbHelper;
Double randUserId;
SharedPreferences spf;
boolean running, wasRunning;
String stopWatch,selected_time;

DrawerLayout timeTrackerDrawerLayout;
NavigationView timeTrackerNavigationView;
ActionBarDrawerToggle timeTrackerActionBarDrawerToggle;
MaterialAutoCompleteTextView sleepHrsAutoCompleteTextView, sleepMinAutoCompleteTextView;
LinearLayout durationOfSleepLayout,durationOfSleepLayoutContainer,trackLostTimeLayout,setupShiftTimeLayout,
        oeeRequirementsLayout,successSetupShiftStart,hoursOfSleepReceivedViewContainer;
Chip trackLostTime, trackBreaksChip,setupShiftDurationChip,proceedToReviewChip;
TextInputLayout sleepHrsTextInputLayout, sleepMinTextInputLayout,setupShiftStartTextInputLayout;
MaterialCardView reviewYourDayCardView,timeTrackerCard;
MaterialAutoCompleteTextView setupShiftStartAutocompleteView;
MaterialDivider reviewDayDivider;

Long finalDownTime,finalBreak,finalIdealCycleTime;
MaterialCardView oeeResults,oeeResultsOverall;
MaterialTextView Availabilty, pPerformance, qQuality,overallOeePercentage;

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


    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
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
        LocalDateTime[] dayRange = obtainDayRange();
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        Collections.addAll(localDateTimes, dayRange);
        Cursor lastRecordDateTimeCursor = null;
        try {
            lastRecordDateTimeCursor = dbHelper.getHoursOfSleepDetails(randUserId,HouseOfCommons.getDateFromLocalDateTime(localDateTimes.get(0)).getTime()
                    , HouseOfCommons.getDateFromLocalDateTime(localDateTimes.get(1)).getTime());
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
                sleepLayout.setVisibility(View.GONE);
            }else{
                sleepLayout.setVisibility(View.VISIBLE);
            }
        }else if(lastDateTime == null ){
            sleepLayout.setVisibility(View.VISIBLE);
        }


    }
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_review);

        //initialize the db helper class
        dbHelper = new DBHelper(this);

        //process the action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Daily review");


        //obtain userId
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        //parent drawer layout
        timeTrackerDrawerLayout = findViewById(R.id.timeTrackerDrawerLayout);

        //navigation view
        timeTrackerNavigationView = findViewById(R.id.timeTrackerNavigationView);


        //top buttons
        reviewYourDayBtn = findViewById(R.id.reviewYourDayExtendedFloatingBtn);
        timeTrackerCardBtn = findViewById(R.id.timeTrackerCardExtendedFloatingBtn);


        //individual cardView available

        //review your day
        reviewYourDayCardView = findViewById(R.id.reviewYourDayCardView);
        reviewYourDayCardView.setVisibility(View.GONE);


        //track your time
        timeTrackerCard = findViewById(R.id.timeTrackerCard);
        timeTrackerCard.setVisibility(View.GONE);

        //layout for tracking time
        trackLostTimeLayout = findViewById(R.id.trackLostTimeLayout);
        trackLostTimeLayout.setVisibility(View.VISIBLE);

        //views to show timer times
        lostTime = findViewById(R.id.lostTime);
        previousLostTime = findViewById(R.id.previousLostTime);

        //buttons to activate timer
        startTimer = findViewById(R.id.startTimer);
        pauseTimer = findViewById(R.id.pauseTimer);
        resumeTimer = findViewById(R.id.resumeTimer);
        completeTimer = findViewById(R.id.completeTimer);

        //setup hours of sleep layout and parentLayout
        durationOfSleepLayoutContainer = findViewById(R.id.durationOfSleepLayoutContainer);
        durationOfSleepLayoutContainer.setVisibility(View.GONE);
        durationOfSleepLayout = findViewById(R.id.durationOfSleepLayout);

        //hours of sleep input(AutoCompleteTextViews)
        sleepHrsAutoCompleteTextView = findViewById(R.id.sleepHrs);
        sleepMinAutoCompleteTextView =findViewById(R.id.sleepMin);

        //hours of sleep input(TextInputLayouts)
        sleepHrsTextInputLayout = findViewById(R.id.sleepHrsTextLayout);
        sleepMinTextInputLayout = findViewById(R.id.sleepMinTextLayout);

        //submit hours of sleep btn
        btnSubmitHrsOfSleep = findViewById(R.id.submitHrsOfSleep);

        //show hours of sleep view plus view to show that sleep can only be tracked after 12 hours
        hoursOfSleepReceivedViewContainer = findViewById(R.id.hoursOfSleepReceivedViewContainer);
        showHrsOfSleepTxtView = findViewById(R.id.hoursOfSleepReceivedView);
        hoursOfSleepReceivedHelper = findViewById(R.id.hoursOfSleepReceivedHelper);

        //chips to navigate between trackingLostTime and trackingHrsOfSleep
        trackLostTime = findViewById(R.id.trackLostTimeChip);
        trackBreaksChip = findViewById(R.id.trackBreaksChip);

        //chips to navigate between setting startOfShift/day and proceeding to review day
        setupShiftDurationChip = findViewById(R.id.setupShiftDurationChip);
        proceedToReviewChip = findViewById(R.id.proceedToReviewChip);

        //setup shift time layout
        setupShiftTimeLayout =  findViewById(R.id.setupShiftTimeLayout);
        setupShiftTimeLayout.setVisibility(View.GONE);

        //input layout and autoComplete view to setup startOf Shift/day
        setupShiftStartAutocompleteView = findViewById(R.id.setupShiftStartAutocompleteView);
        setupShiftStartTextInputLayout = findViewById(R.id.setupShiftStartTextInputLayout);

        //btn to submit start of shift
        submitShiftStartAndEndBtn = findViewById(R.id.submitShiftStartAndEndBtn);


        //view to show on successful shift setup
        //successSetupShiftStart.findViewById(R.id.successSetupShiftStart);
        //successSetupShiftStart.setVisibility(View.GONE);

        //container layout for oee requirements
        oeeRequirementsLayout = findViewById(R.id.oeeRequirementsLayout);
        oeeRequirementsLayout.setVisibility(View.GONE);

        //individual oee requirements
        shiftLengthTextView = findViewById(R.id.shiftLength);
        breaksTextView = findViewById(R.id.breaks);
        downtimeTextView = findViewById(R.id.downtime);
        idealCycleTimeTextView = findViewById(R.id.idealCycleTime);
        totalCountTextView =findViewById(R.id.totalCount);
        rejectCountTextView = findViewById(R.id.rejectCount);

        //divider separating oee requirements and btn to proceed and do the calculations
        reviewDayDivider = findViewById(R.id.reviewDayDivider);
        reviewDayDivider.setVisibility(View.GONE);

        //btn to proceed and do the calculations for the oee
        proceedToGetReportOfDaysReport = findViewById(R.id.proceedToGetReportOfDaysReport);

        //oeeResults
        oeeResults = findViewById(R.id.oeeResults);
        oeeResults.setVisibility(View.GONE);
        oeeResultsOverall = findViewById(R.id.oeeResultsOverall);
        oeeResultsOverall.setVisibility(View.GONE);
        Availabilty = findViewById(R.id.Availabilty);
        pPerformance = findViewById(R.id.Performance);
        qQuality = findViewById(R.id.Quality);
        overallOeePercentage = findViewById(R.id.overallOeePercentage);




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


        //restore saved instance when page is resumed
        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }


        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        timeTrackerActionBarDrawerToggle = new ActionBarDrawerToggle(this,timeTrackerDrawerLayout,R.string.open_drawer,R.string.close_drawer);

        //setup lister for clicks
        timeTrackerDrawerLayout.addDrawerListener(timeTrackerActionBarDrawerToggle);
        timeTrackerActionBarDrawerToggle.syncState();


        //process navigation view clicks
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

        //process btn to show trackTime layout
        timeTrackerCardBtn.setOnClickListener(view -> {
            if (timeTrackerCard.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(timeTrackerCard, new AutoTransition());
                timeTrackerCardBtn.setBackgroundColor(getColor(R.color.black));
                timeTrackerCardBtn.setTextColor(getColor(R.color.orange));
                reviewYourDayBtn.setBackgroundColor(getColor(R.color.white));
                reviewYourDayBtn.setTextColor(getColor(R.color.black));
                timeTrackerCard.setVisibility(View.VISIBLE);
                reviewYourDayCardView.setVisibility(View.GONE);
            }
            else{
                TransitionManager.beginDelayedTransition(timeTrackerCard, new AutoTransition());
                timeTrackerCard.setVisibility(View.GONE);
                timeTrackerCardBtn.setBackgroundColor(getColor(R.color.white));
                timeTrackerCardBtn.setTextColor(getColor(R.color.black));
            }
        });

        //process button to show layout of reviewing your day
        /*
         * proved essential to populate views on click of this top level button because otherwise, the view weren't populated on time*/
        reviewYourDayBtn.setOnClickListener(view -> {

            //check if the correct view is shown, if not show it . First part is when the shadowing view is currently show
            //second part of statement is when the shadowing layout isn't shown, plus the required view is hidden too
            if (reviewYourDayCardView.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(reviewYourDayCardView, new AutoTransition());

                reviewYourDayCardView.setVisibility(View.VISIBLE);
                timeTrackerCard.setVisibility(View.GONE);

                //setup colors and backgrounds to show button is clicked
                reviewYourDayBtn.setBackgroundColor(getColor(R.color.black));
                reviewYourDayBtn.setTextColor(getColor(R.color.orange));
                timeTrackerCardBtn.setBackgroundColor(getColor(R.color.white));
                timeTrackerCardBtn.setTextColor(getColor(R.color.black));


            }else{
                TransitionManager.beginDelayedTransition(reviewYourDayCardView, new AutoTransition());
                reviewYourDayCardView.setVisibility(View.GONE);

                //setup colors and backgrounds to show button is clicked
                reviewYourDayBtn.setBackgroundColor(getColor(R.color.white));
                reviewYourDayBtn.setTextColor(getColor(R.color.black));
            }

            //initialize cursor earlier on so that they are available widely
            Cursor downtimeCursor = null, breaksCursor = null;

            //make db hit to obtain downtime
            try {
                downtimeCursor = dbHelper.getDowntimeDurationFromTimeTracker(randUserId,HouseOfCommons.getDateFromLocalDateTime(obtainDayRange()[0]).getTime(),HouseOfCommons.getDateFromLocalDateTime(obtainDayRange()[1]).getTime());
            }catch (Exception e){
                e.printStackTrace();
            }

            //make db hit to obtain hours of sleep
            try {
                breaksCursor = dbHelper.getHoursOfSleepDetails(randUserId,HouseOfCommons.getDateFromLocalDateTime(obtainDayRange()[0]).getTime(),HouseOfCommons.getDateFromLocalDateTime(obtainDayRange()[1]).getTime());
            }catch (Exception e){
                e.printStackTrace();
            }

            //assign value from db
            Long breaks = null,downtime = null;

            if (downtimeCursor != null && downtimeCursor.moveToFirst()){
                downtime = downtimeCursor.getLong(downtimeCursor.getColumnIndexOrThrow("lostDuration"));
            }
            if (breaksCursor != null && breaksCursor.moveToFirst()){
                breaks = breaksCursor.getLong(breaksCursor.getColumnIndexOrThrow("HoursOfSleep"));
            }


            //check for nulls which might brick the application else show values obtained
            if (downtime == null){
                //kind of an error form
                downtimeTextView.setText(R.string.lost_time_zero_);
                downtimeTextView.setTextColor(getColor(R.color.orange));

                downtime = Long.parseLong("0");
            }else{
                finalDownTime = downtime;
                downtimeTextView.setText(String.format((HouseOfCommons.locale),"%s : %s","Downtime during the day",downtime));
                downtimeTextView.setTextColor(getColor(R.color.black));
            }

            //check for nulls which might brick the application else show values obtained
            if(breaks == null){
                breaks = Long.parseLong("0");

                //kind of an error form
                breaksTextView.setText(R.string.sleep_must_be_accounted_for);
                breaksTextView.setTextColor(getColor(R.color.orange));

                //as sleep is a must, therefore must be accounted for, hide button to proceed until so
                proceedToGetReportOfDaysReport.setVisibility(View.GONE);
                reviewDayDivider.setVisibility(View.GONE);

            }else{

                //if break(sleep duration) is present proceed

                //obtain value for future use in calculating oee
                finalBreak = breaks;

                //format amount of sleep obtained
                String hrUnits,minUnits;
                long hrs = 0;
                long min = 0;

                long l = Long.parseLong(String.valueOf(breaks)) % 3600;
                if (Long.parseLong(String.valueOf(breaks)) >= 3600){
                    hrs = Long.parseLong(String.valueOf(breaks)) / 3600;
                    min = l / 60;
                }else if(Long.parseLong(String.valueOf(breaks)) > 60){
                    min = l / 60;
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

                //show sleep duration when present
                String formattedBreak = String.format(HouseOfCommons.locale,"%s %d %s %d %s","Total duration of breaks : ",hrs,hrUnits,min,minUnits);
                breaksTextView.setText(formattedBreak);
                breaksTextView.setTextColor(getColor(R.color.black));

                //as requirements are fulfilled to do the calculation, show button to proceed
                proceedToGetReportOfDaysReport.setVisibility(View.VISIBLE);
                reviewDayDivider.setVisibility(View.VISIBLE);
            }

            //obtain a sum of lost time
            long totalLostDurationPlusDowntime;
            totalLostDurationPlusDowntime = breaks + downtime;


            //obtain totalTasks per day
            Cursor totalTasksPerDayCursor = null;
            try {
                totalTasksPerDayCursor = dbHelper.getCountOfTasksPerDay(randUserId, obtainDayRange()[0],obtainDayRange()[1]);
            }catch(Exception e){
                e.printStackTrace();
            }

            int totalTasksPerDay = 0;
            if (totalTasksPerDayCursor != null && totalTasksPerDayCursor.moveToFirst()){
                totalTasksPerDay = totalTasksPerDayCursor.getInt(totalTasksPerDayCursor.getColumnIndexOrThrow("count"));
            }

            long idealCycleTime;
            long differenceBetweenShiftDurationMinusTotalLostDurationPlusDowntime;
            differenceBetweenShiftDurationMinusTotalLostDurationPlusDowntime  = ((24 * 3600) - totalLostDurationPlusDowntime);
            Log.i("diff",""+differenceBetweenShiftDurationMinusTotalLostDurationPlusDowntime);

            if (totalTasksPerDay != 0){
                idealCycleTime  = differenceBetweenShiftDurationMinusTotalLostDurationPlusDowntime / totalTasksPerDay;

                //obtain value for oee calculation only after ensuring no null values
                finalIdealCycleTime = idealCycleTime;
                finalTotalTasks = totalTasksPerDay;

                //process ideal cycle tim e for population
                int hrs;
                int min;
                hrs = (int) (idealCycleTime / 3600);
                min = (int) ((idealCycleTime % 3600) / 60);

                String idealCycleTime_str = String.format(HouseOfCommons.locale,"%s %d %s %d %s","The ideal task time : ",hrs, "hours", min, "minutes");
                idealCycleTimeTextView.setText(idealCycleTime_str);


                //populate shift length view
                shiftLengthTextView.setText(String.format(HouseOfCommons.locale,"%s","The day has 24 hours"));

                //populate total count and pending count views
                totalCountTextView.setText(String.format((HouseOfCommons.locale),"%s : %d %s","Total tasks of the day",totalTasksPerDay,"tasks"));
                int PendingTasksPerDay = getCountOfPendingTasksPerDay(randUserId, obtainDayRange());
                finalPendingTasksPerDay = PendingTasksPerDay;
                rejectCountTextView.setText(String.format((HouseOfCommons.locale),"%s : %d %s","Pending tasks of the day",PendingTasksPerDay,"tasks"));


            }else{
                //if no tasks created for the day show this
                totalCountTextView.setText(R.string.zero_tasks_);
                rejectCountTextView.setText(R.string.zero_tasks_);

                //also hide button to proceed as requirements have not been met
                reviewDayDivider.setVisibility(View.GONE);
                proceedToGetReportOfDaysReport.setVisibility(View.GONE);
            }
        });


        //function to process timer
        runTimer();


        //on page load, show start button only
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

                reason = Objects.requireNonNull(lostTimeReasonTxt.getText()).toString().trim();

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

        //process button to submit time slept through the day
        btnSubmitHrsOfSleep.setOnClickListener(view -> {


            String hrsOfSleep_str = sleepHrsAutoCompleteTextView.getText().toString().trim();
            String minOfSleep_str = sleepMinAutoCompleteTextView.getText().toString().trim();


            if (!hrsOfSleep_str.isEmpty()){
                if (minOfSleep_str.isEmpty()){
                    minOfSleep_str = "0";
                }

                //convert the hours of sleep input by user to a uniform format long seconds
                long hrsOfSleep_long = Long.parseLong(hrsOfSleep_str) * 3600;
                long minOfSleep = Long.parseLong(minOfSleep_str) * 60;
                long durationOfSleep = hrsOfSleep_long + minOfSleep;

                //make a db hit to input hours of sleep
                boolean success = false;
                try {
                    success = dbHelper.insertHoursOfSleep(randUserId,new Date().getTime(),durationOfSleep);
                }catch (Exception e){
                    e.printStackTrace();
                }

                //ensure correct grammar
                String hrUnits;
                String minUnits = "Minutes";

                if (hrsOfSleep_long > 1){
                    hrUnits = "Hours";
                }else{
                    hrUnits = "Hour";
                }

                //if db hit was successful, show view with hours setup above
                if (success){
                    showHrsOfSleepTxtView.setVisibility(View.VISIBLE);
                    showHrsOfSleepTxtView.setText(String.format(HouseOfCommons.locale,"%d %s %d %s",selectedHrsOfSleep,hrUnits, selectedMinOfSleep, minUnits ));

                    //hide layout with views to enter hours of sleep
                    durationOfSleepLayout.setVisibility(View.GONE);

                    //hide layout showing limit in setting up sleep time, possible only after 12 hrs
                    hoursOfSleepReceivedHelper.setVisibility(View.GONE);
                }
            }else{

                //show textInputLayout error when an empty value on click of the submit button
                sleepHrsTextInputLayout.setErrorEnabled(true);
                sleepHrsTextInputLayout.setError("Press and choose a number from the dropdown");
            }


        });

        //chip to make tracking lost time layout to be visible
        trackLostTime.setOnClickListener(view -> {

            //setup a transition
            TransitionManager.beginDelayedTransition(trackLostTimeLayout, new AutoTransition());
            durationOfSleepLayoutContainer.setVisibility(View.GONE);
            trackLostTimeLayout.setVisibility(View.VISIBLE);

            //set colors when clicked and un-clicked
            trackLostTime.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.black)));
            trackLostTime.setTextColor(ColorStateList.valueOf(getColor(R.color.orange)));
            trackBreaksChip.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.black)));
            trackBreaksChip.setTextColor(ColorStateList.valueOf(getColor(R.color.white)));

        });


        //chip to show layout for tracking sleeping hours
        trackBreaksChip.setOnClickListener(view -> {

            //set colors when clicked and un-clicked
            trackBreaksChip.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.black)));
            trackBreaksChip.setTextColor(ColorStateList.valueOf(getColor(R.color.orange)));
            trackLostTime.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.black)));
            trackLostTime.setTextColor(ColorStateList.valueOf(getColor(R.color.white)));

            Cursor hoursOfSleepCursor = null;
            String hoursOfSleep = null;
            LocalDateTime[] dayRange = obtainDayRange();

            //make db hit to obtains hours of sleep
            try {
                hoursOfSleepCursor = dbHelper.getHoursOfSleepDetails(randUserId,HouseOfCommons.getDateFromLocalDateTime(dayRange[0]).getTime(), HouseOfCommons.getDateFromLocalDateTime(dayRange[1]).getTime());
            }catch (Exception e){
                e.printStackTrace();
            }

            if (hoursOfSleepCursor != null && hoursOfSleepCursor.moveToLast()){
                hoursOfSleep = hoursOfSleepCursor.getString(hoursOfSleepCursor.getColumnIndexOrThrow("HoursOfSleep"));
            }

            //only show layout when there's a already setup sleep Hours value <------------??
            if (hoursOfSleep == null){
                hoursOfSleepReceivedViewContainer.setVisibility(View.GONE);
            }


            //when the submit button to save the input for hours of sleep in a day, the below container is made GONE, thus when it's gone in
            //this case means that the sleep values are set, so present a message saying that sleep setup can only be done after a 12 hr limit
            if (durationOfSleepLayoutContainer.getVisibility() == View.GONE){

                //format the received seconds to hours and minutes
                long hrs = 0;
                long min = 0;

                if (hoursOfSleep != null && Long.parseLong(hoursOfSleep) >= 3600){
                    hrs = Long.parseLong(hoursOfSleep) / 3600;
                    min = (Long.parseLong(hoursOfSleep ) % 3600) / 60;
                }else if(hoursOfSleep != null && Long.parseLong(hoursOfSleep) > 60){
                    min = Long.parseLong(hoursOfSleep ) % 3600;
                }

                //ensure correct statements are put out, if hours more than 1 then it's hours not hour...etc
                String hrUnits,minUnits;

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

                String text="Tracking of sleeping hours can only be done after a 12 hour limit";

                hoursOfSleepReceivedHelper.setText(String.format(HouseOfCommons.locale,"%s\n%s %d %s %d %s",
                        text,"\n\n\nTime slept today : ",hrs,hrUnits,min,minUnits));

                //why? helper is in it
                durationOfSleepLayoutContainer.setVisibility(View.VISIBLE);
                hoursOfSleepReceivedHelper.setVisibility(View.VISIBLE);


                //hide un-required views
                trackLostTimeLayout.setVisibility(View.GONE);
                showHrsOfSleepTxtView.setVisibility(View.GONE);

            }else {
                //if the layout to setup sleep isn't hidden, then that means that it still requires input thus show it
                trackLostTimeLayout.setVisibility(View.GONE);
                durationOfSleepLayout.setVisibility(View.VISIBLE);
            }

            //if the trackTime layout is visible, onClick show the sleep layout and when sleep layout is shown, hide the hoursOfSleepReceived container
            if (trackLostTimeLayout.getVisibility() == View.VISIBLE){
                TransitionManager.beginDelayedTransition(durationOfSleepLayout, new AutoTransition());
                durationOfSleepLayout.setVisibility(View.VISIBLE);
                trackLostTimeLayout.setVisibility(View.GONE);

                //if view to setup is visible, don't show view saying hours of sleep can only be setup after 12 hrs
                if (durationOfSleepLayout.getVisibility() == View.VISIBLE){
                    hoursOfSleepReceivedViewContainer.setVisibility(View.GONE);
                }
            }

        });



        //setup layoutVisibility on chip taps. Layouts involved are the review your day pre-report and setup shift length layout
        //setupShiftTimeLayout oeeRequirementsLayout
        setupShiftDurationChip.setOnClickListener(view -> {
            if (setupShiftTimeLayout.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(setupShiftTimeLayout, new AutoTransition());
                setupShiftTimeLayout.setVisibility(View.VISIBLE);
                oeeRequirementsLayout.setVisibility(View.GONE);
                proceedToGetReportOfDaysReport.setVisibility(View.GONE);
            }else{
                TransitionManager.beginDelayedTransition(setupShiftTimeLayout, new AutoTransition());
                setupShiftTimeLayout.setVisibility(View.GONE);
                proceedToGetReportOfDaysReport.setVisibility(View.GONE);
            }
        });
        proceedToReviewChip.setOnClickListener(view -> {
            if (oeeRequirementsLayout.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(oeeRequirementsLayout,new AutoTransition());
                oeeRequirementsLayout.setVisibility(View.VISIBLE);
                setupShiftTimeLayout.setVisibility(View.GONE);
                reviewDayDivider.setVisibility(View.VISIBLE);
            }else{
                TransitionManager.beginDelayedTransition(oeeRequirementsLayout,new AutoTransition());
                oeeRequirementsLayout.setVisibility(View.GONE);
                reviewDayDivider.setVisibility(View.GONE);
            }
        });

        setupShiftStartAutocompleteView.setOnClickListener(view -> selectTime(setupShiftStartAutocompleteView));
        submitShiftStartAndEndBtn.setOnClickListener(view -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("GMT+3")),HouseOfCommons.locale);
            int yYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int dDay = calendar.get(Calendar.DAY_OF_MONTH);

            LocalDateTime startOfShift = null;

            if(!setupShiftStartAutocompleteView.getText().toString().trim().isEmpty()){
                String[] selectedStartDayTime = setupShiftStartAutocompleteView.getText().toString().trim().split(" ",2);
                String[] hourMinute = selectedStartDayTime[0].split(":",2);

               startOfShift = HouseOfCommons.stringToDate(String.format(HouseOfCommons.locale,"%02d-%02d-%d%s%02d:%02d",dDay,mMonth,yYear," ",Integer.parseInt(hourMinute[0]),Integer.parseInt(hourMinute[1])));

            }else{
                setupShiftStartTextInputLayout.setErrorEnabled(true);
                setupShiftStartTextInputLayout.setError("Tap to select a time");
            }




            boolean success = false;
            try{
                success = insertIntoReviewYourDayTable(randUserId, startOfShift);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(success){
                TransitionManager.beginDelayedTransition(successSetupShiftStart, new AutoTransition());
                setupShiftTimeLayout.setVisibility(View.GONE);
                successSetupShiftStart.setVisibility(View.VISIBLE);
                setupShiftDurationChip.setVisibility(View.GONE);
            }




        });





        //process button to proceed and calculate the oee
        proceedToGetReportOfDaysReport.setOnClickListener(view -> {
            int shiftLength = 24;
            Long breaks = finalBreak; //<------------seconds
            Long downtime;
            if(finalDownTime == null){
                 downtime = Long.parseLong("0"); //<-------seconds
            }else{
                downtime = finalDownTime;
            }
            Long idealCycleTime = finalIdealCycleTime ; //<---------millis
            int totalCount = finalTotalTasks; //<-----int
            int rejectCount = finalPendingTasksPerDay; //<-----int
            Log.i("Final data",breaks +":"+downtime+":"+idealCycleTime+":"+totalCount+":"+rejectCount);

            //calculating the oee
            long plannedProductionTime = (shiftLength * 3600) - breaks;
            long runTime = plannedProductionTime - downtime;
            double goodCount = Double.parseDouble(String.valueOf(totalCount)) - Double.parseDouble(String.valueOf(rejectCount));
            //availability = Run Time / Planned Production Time
            double Availability = Double.parseDouble(String.valueOf(runTime)) / Double.parseDouble(String.valueOf(plannedProductionTime));
            //performance = (Ideal Cycle Time × Total Count) / Run Time
            double Performance = Double.parseDouble(String.valueOf((idealCycleTime * Long.parseLong(String.valueOf(totalCount))))) / Double.parseDouble(String.valueOf(runTime));
            //quality = Good Count / Total Count
            double Quality = Double.parseDouble(String.valueOf(goodCount)) / Double.parseDouble(String.valueOf(totalCount));

            double oee = (Availability * Performance * Quality) * 100;
            Log.i("oee",""+Availability);
            Log.i("oee",""+Performance);
            Log.i("oee",""+Quality);
            Log.i("oee",""+oee);

            String pattern = "##.##";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);

            Availabilty.setText(String.format(HouseOfCommons.locale,"%s : %s%s","Your availability to perform tasks was", decimalFormat.format(Availability * 100),"%" ));
            qQuality.setText(String.format(HouseOfCommons.locale,"%s : %s%s", "The quality of task performed  ",decimalFormat.format(Quality * 100),"%"));
            pPerformance.setText(String.format(HouseOfCommons.locale,"%s : %s%s", "Your performance today was  ",decimalFormat.format(Performance * 100),"%"));

            overallOeePercentage.setText(String.format(HouseOfCommons.locale,"%s%s", decimalFormat.format(oee ),"%"));
            oeeResultsOverall.setVisibility(View.VISIBLE);
            oeeResults.setVisibility(View.VISIBLE);
            reviewYourDayCardView.setVisibility(View.GONE);
            timeTrackerCard.setVisibility(View.GONE);

        });
    }

    //functions to control timer on clicks of button by changing a boolean value
    @SuppressWarnings("unused")
    private void onClickStart(View view){
        running = true;
    }
    @SuppressWarnings("unused")
    private void onClickPause(View view){
        running = false;
    }
    @SuppressWarnings("unused")
    private void onClickResume(View view){
        running = true;
    }
    @SuppressWarnings("unused")
    private  void onClickComplete(View view){running = false;}

    //function that starts a timer incrementing a value per second and populating it live on a view
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

    //insert shiftStart
    private boolean insertIntoReviewYourDayTable(Double randUserId, LocalDateTime shiftStart){
        boolean success = false;
        try {
            success = dbHelper.insertShiftDetails(randUserId, shiftStart, Long.parseLong(String.valueOf(86400000)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    //function to open dialog and select time
    private void selectTime(AutoCompleteTextView autoCompleteTextView){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour1, minute1) -> {
            String formattedHour, formattedMinute;
            if (hour1 < 10){
                formattedHour = "0" + hour1;
            }else{
                formattedHour = "" + hour1;
            }
            if (minute1 < 10 ){
                formattedMinute = "0" + minute1;
            }else{
                formattedMinute = "" + minute1;
            }
            selected_time = formattedHour + ":" + formattedMinute;
            autoCompleteTextView.setText(HouseOfCommons.FormatTime(hour1, minute1));
        },hour, minute,false);
        timePickerDialog.show();
    }

    //insert time lost from timeTracker
    private void insertTimeTrackerDetails(Double randUserId,long dateToday,long duration,String reason){
        boolean b = dbHelper.insertIntoTimeTracker(randUserId,dateToday,duration,reason);
        if(b){
            seconds = 0;
            state = 4;
        }

    }

    //function that provides a formatted array of that day start and end midnight to midnight
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

    //obtain pending tasks from db
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


}