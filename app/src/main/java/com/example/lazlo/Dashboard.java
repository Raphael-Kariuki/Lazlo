package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Dashboard extends AppCompatActivity {
MaterialAutoCompleteTextView monthsSelectionDropDownOnDashBoard;
String selectedMonth;
Integer monthIndex;
MaterialTextView totalTasksPerMonth, totalCompletedTasksPerMonth, totalPendingTasksPerMonth;
Integer totalTasksPerMonth_int, totalCompletedTasksPerMonth_int, totalPendingTasksPerMonth_int,totalTasks;
DBHelper dbHelper;
SharedPreferences spf;
Double randUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dbHelper = new DBHelper(this);



        //obtain userId to be used in obtain user task stats
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        totalTasksPerMonth = findViewById(R.id.totalTasksPerMonth);
        totalCompletedTasksPerMonth = findViewById(R.id.totalTasksCompletedPerMonth);
        totalPendingTasksPerMonth = findViewById(R.id.totalTasksPendingPerMonth);

        totalTasksPerMonth.setText("" + populateTotalTasksView(randUserId));

        monthsSelectionDropDownOnDashBoard = findViewById(R.id.monthsSelectionDropDownOnDashBoard);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Months, android.R.layout.simple_dropdown_item_1line);
        monthsSelectionDropDownOnDashBoard.setAdapter(adapter);
        monthsSelectionDropDownOnDashBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = (String) adapterView.getItemAtPosition(i);
                monthIndex = i;
                //getSumOfTasksPerMonthForDashBoard
                totalTasksPerMonth.setText("" + populateTotalTasksPerMonthView(randUserId,monthIndex + 1));

            }
        });

    }
    public LocalDate[] getMonthBasedStats(int indexOfMonth){
        String newMonthIndex;
        LocalDate[] dateRanges = new LocalDate[2];

        //for proper date formatting, add zero before month
        if(indexOfMonth <= 9){
            newMonthIndex = "0" + indexOfMonth;
        }else{
            newMonthIndex = "" + indexOfMonth;
        }
        //obtain the current year
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);

        //setup date ranges
        String rangeStart = "01-" + newMonthIndex + "-" + mYear + " 23:59";
        String rangeEnd = "31-" + newMonthIndex + "-" + mYear + " 23:59";
        System.out.println(rangeStart + " : " + rangeEnd);



        dateRanges[0] = stringToDate(rangeStart);
        dateRanges[1] = stringToDate(rangeEnd);
        System.out.println(dateRanges[0] + " : " + dateRanges[1]);
        return dateRanges;

    }
    public Cursor getTotalTasksCountPerMonth(Double randUserid,LocalDate startDate, LocalDate endDate){
        Cursor cursor = null;
        try {
            cursor   = dbHelper.getSumOfTasksPerMonthForDashBoard(randUserid,startDate,endDate);
            System.out.println("Success getting count");
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getTotalTasksCount(Double randUserid){
        Cursor cursor = null;
        try {
            cursor   = dbHelper.getSumOfTasksForDashBoard(randUserid);
            System.out.println("Success getting count");
        }catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }

    public int populateTotalTasksView(Double randUserId){
        Cursor cursor = getTotalTasksCount(randUserId);
        if (cursor.moveToFirst()){
            totalTasks = cursor.getInt(cursor.getColumnIndexOrThrow("sumTotalTasks"));
        }
        return totalTasks;
    }

    public int populateTotalTasksPerMonthView(Double randUserId, int monthIndex){
        LocalDate[] range = getMonthBasedStats(monthIndex);
        LocalDate startDate, endDate;
        startDate = range[0];
        endDate = range[1];
        Cursor cursor = getTotalTasksCountPerMonth(randUserId,startDate, endDate);
        if (cursor.moveToFirst()){
            totalTasksPerMonth_int = cursor.getInt(cursor.getColumnIndexOrThrow("sumTotalTasksPerMonth"));
        }
        return totalTasksPerMonth_int;
    }
    public LocalDate stringToDate(String date){
        LocalDate newDate = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try{
            newDate = getDateFromString(date, dateTimeFormatter);
        }catch(Exception e){
            e.printStackTrace();
        }
        return newDate;
    }
    public static LocalDate getDateFromString(String string, DateTimeFormatter dateTimeFormatter){
        LocalDate date = LocalDate.parse(string, dateTimeFormatter);
        return date;
    }

}