package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import com.example.lazlo.Sql.DBHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class review extends AppCompatActivity {
DBHelper dbHelper;
SharedPreferences spf;
Double randUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        dbHelper = new DBHelper(this);
    }

    private String[] obtainDayRange(){
        Calendar calendar = Calendar.getInstance(HouseOfCommons.locale);
        int mMonth = calendar.get(Calendar.MONTH);
        int dDay = calendar.get(Calendar.DAY_OF_MONTH);
        int yYear = calendar.get(Calendar.YEAR);

        String startOfDay = String.format(HouseOfCommons.locale,"%d-%02d-%02d%s%02d:%02d",yYear,mMonth,dDay,"T",0,1);
        String endOfDay = String.format(HouseOfCommons.locale,"%d-%02d-%02d%s%02d:%02d",yYear,mMonth,dDay,"T",0,0);

        String[] dayRange = new String[2];
        dayRange[0] = startOfDay;
        dayRange[1] = endOfDay;

        return dayRange;

    }
    private int getCountOfTotalTasksPerDay(Double randUserId, String[] dayRange){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        for (String range: dayRange){
            localDateTimes.add(LocalDateTime.parse(range));
        }
        Cursor toTalTasksPerDayCount = dbHelper.getCountOfTasksPerDay(randUserId,localDateTimes.get(0),localDateTimes.get(1));
        int TotalTasksPerDay = 0;
        if (toTalTasksPerDayCount != null && toTalTasksPerDayCount.moveToFirst()){
            TotalTasksPerDay = toTalTasksPerDayCount.getInt(toTalTasksPerDayCount.getColumnIndexOrThrow("count"));
        }
        return TotalTasksPerDay;
    }
    private int getCountOfCompletedTasksPerDay(Double randUserId, String[] dayRange){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        for (String range: dayRange){
            localDateTimes.add(LocalDateTime.parse(range));
        }
        Cursor toTalCompletedTasksPerDayCount = dbHelper.getCountOfCompletedTasksPerDay(randUserId,localDateTimes.get(0),localDateTimes.get(1));
        int TotalCompletedTasksPerDay = 0;
        if (toTalCompletedTasksPerDayCount != null && toTalCompletedTasksPerDayCount.moveToFirst()){
            TotalCompletedTasksPerDay = toTalCompletedTasksPerDayCount.getInt(toTalCompletedTasksPerDayCount.getColumnIndexOrThrow("count"));
        }
        return TotalCompletedTasksPerDay;
    }
    private int getCountOfPendingTasksPerDay(Double randUserId, String[] dayRange){
        ArrayList<LocalDateTime> localDateTimes = new ArrayList<>();
        for (String range: dayRange){
            localDateTimes.add(LocalDateTime.parse(range));
        }
        Cursor toTalPendingTasksPerDayCount = dbHelper.getCountOfPendingTasksPerDay(randUserId,localDateTimes.get(0),localDateTimes.get(1));
        int TotalPendingTasksPerDay = 0;
        if (toTalPendingTasksPerDayCount != null && toTalPendingTasksPerDayCount.moveToFirst()){
            TotalPendingTasksPerDay = toTalPendingTasksPerDayCount.getInt(toTalPendingTasksPerDayCount.getColumnIndexOrThrow("count"));
        }
        return TotalPendingTasksPerDay;
    }
    private int getDowntimeFromTimeTracker(Double randUserId){
        //TODO:create review table that includes last time review was done, the current time; to be used in establishing the review period
        /*Review needs to be done daily, however on days that one is not able to do so. Currently assign 0, for now
        Future iteration might check alarm to see what time one slept to obtain breaks
        * */
        int downtime = 0;
        return downtime ;
    }
}