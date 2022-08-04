package com.example.lazlo;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {
    MaterialAutoCompleteTextView monthsSelectionDropDownOnDashBoard;
    String selectedMonth;
    Integer monthIndex;
    MaterialTextView totalTasksPerMonth, totalCompletedTasksPerMonth, totalPendingTasksPerMonth;
    Integer totalTasksPerMonth_int, totalCompletedTasksPerMonth_int, totalPendingTasksPerMonth_int, totalTasks, totalCompletedTasks_int, totalPendingTasks_int;
    DBHelper dbHelper;
    SharedPreferences spf;
    Double randUserId;
    AppCompatButton btnMonthlySpendingView, btnCustomSpendingView;
    TableLayout monthlyTable;
    LinearLayout customViewLayout, pieChartLayout;
    MaterialTextView Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sept, Oct, Nov, Dec;
    TextInputEditText startDuration_choice, endDuration_choice;
    TextInputLayout startDateLayout, endDateLayout;
    DatePickerDialog datePickerDialog, datePickerDialog2;

    String selectedStart_duration, selectedEnd_duration;
    MaterialButton btnShowPredictedSpending;
    LocalDateTime selectedStart_duration_String, selectedEnd_duration_String;
    TextView sumTotalView;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView showSpendingListView;
    PieChart pieChart;
    BarChart barChart;
    FloatingActionButton hamburger_menu;
    NumberFormat numberFormat;

    public static LocalDateTime getDateFromString(String string, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(string, dateTimeFormatter);
    }


    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        numberFormat = NumberFormat.getCurrencyInstance(new Locale("en","KE"));


        hamburger_menu = findViewById(R.id.hamburger_menu);
        hamburger_menu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Account.class);
            startActivity(intent);
        });

        dbHelper = new DBHelper(this);

        //obtain pieChart
        pieChart = findViewById(R.id.spendingViewPieChart);
        pieChartLayout = findViewById(R.id.pieChartLayout);

        //obtain barChart
        barChart = findViewById(R.id.spendingViewBarChart);

        //custom view processing
        startDuration_choice = findViewById(R.id.startDateInput);
        endDuration_choice = findViewById(R.id.endDateInput);

        startDateLayout = findViewById(R.id.startDateLayout);
        endDateLayout = findViewById(R.id.endDateLayout);

        sumTotalView = findViewById(R.id.SumTotalView);
        showSpendingListView = findViewById(R.id.showSpendingListView);

        final Calendar calendar = Calendar.getInstance();
        int sYear = calendar.get(Calendar.YEAR);
        int sMonth = calendar.get(Calendar.MONTH);
        int sDay = calendar.get(Calendar.DAY_OF_MONTH);
        int eYear = calendar.get(Calendar.YEAR);
        int eMonth = calendar.get(Calendar.MONTH);
        int eDay = calendar.get(Calendar.DAY_OF_MONTH);

        startDuration_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set startDatePicker dialog
                datePickerDialog = new DatePickerDialog(Dashboard.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String formattedMonth, formattedDay;
                        if (monthOfYear + 1 <= 9) {
                            formattedMonth = "0" + (monthOfYear + 1);
                        } else {
                            formattedMonth = String.valueOf(monthOfYear + 1);
                        }
                        if (dayOfMonth < 10) {
                            formattedDay = "0" + dayOfMonth;
                        } else {
                            formattedDay = String.valueOf(dayOfMonth);
                        }
                        startDuration_choice.setText(formattedDay + "-" + formattedMonth + "-" + year);
                    }
                }, sYear, sMonth, sDay);
                datePickerDialog.show();

            }
        });
        endDuration_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set endDatePicker dialog
                datePickerDialog2 = new DatePickerDialog(Dashboard.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        String formattedMonth, formattedDay;
                        if (monthOfYear + 1 <= 9) {
                            formattedMonth = "0" + (monthOfYear + 1);
                        } else {
                            formattedMonth = String.valueOf(monthOfYear + 1);
                        }
                        if (dayOfMonth < 10) {
                            formattedDay = "0" + dayOfMonth;
                        } else {
                            formattedDay = String.valueOf(dayOfMonth);
                        }
                        endDuration_choice.setText(formattedDay + "-" + formattedMonth + "-" + year);
                    }
                }, eYear, eMonth, eDay);
                datePickerDialog2.show();
            }
        });

        btnShowPredictedSpending = findViewById(R.id.btnShowPredictedSpending);

        btnShowPredictedSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedStart_duration = startDuration_choice.getText().toString().trim();
                selectedEnd_duration = endDuration_choice.getText().toString().trim();

                if (!selectedStart_duration.isEmpty()) {
                    if (!selectedEnd_duration.isEmpty()) {
                        startDateLayout.setErrorEnabled(false);
                        endDateLayout.setErrorEnabled(false);

                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");

                        selectedStart_duration_String = getDateFromString(selectedStart_duration + " 00:01", dateTimeFormatter);
                        selectedEnd_duration_String = getDateFromString(selectedEnd_duration + " 00:01", dateTimeFormatter);

                        sumTotalView.setText("" + numberFormat.format(populateSpendingView(randUserId,selectedStart_duration_String, selectedEnd_duration_String)));
                        populateSpendingDetails(selectedStart_duration_String, selectedEnd_duration_String);

                    } else {
                        endDateLayout.setErrorEnabled(true);
                        startDateLayout.setErrorEnabled(false);
                        endDateLayout.setError("Select start date");
                    }

                } else {
                    startDateLayout.setErrorEnabled(true);
                    endDateLayout.setErrorEnabled(false);
                    startDateLayout.setError("Select start date");
                }
            }
        });


        //obtain monthly textViews
        Jan = findViewById(R.id.Jan);
        Feb = findViewById(R.id.Feb);
        Mar = findViewById(R.id.Mar);
        Apr = findViewById(R.id.Apr);
        May = findViewById(R.id.May);
        Jun = findViewById(R.id.Jun);
        Jul = findViewById(R.id.Jul);
        Aug = findViewById(R.id.Aug);
        Sept = findViewById(R.id.Sept);
        Oct = findViewById(R.id.Oct);
        Nov = findViewById(R.id.Nov);
        Dec = findViewById(R.id.Dec);


        //obtain userId to be used in obtain user task stats
        spf = getSharedPreferences("user_details", MODE_PRIVATE);
        randUserId = Double.parseDouble(spf.getString("randomUserId", null));

        //obtain button for monthly and custom spending view
        btnCustomSpendingView = findViewById(R.id.btnCustomSpendingView);
        btnMonthlySpendingView = findViewById(R.id.btnMonthlySpendingView);

        //obtain custom view layout
        customViewLayout = findViewById(R.id.customViewLayout);

        //obtain monthly spending table layout
        monthlyTable = findViewById(R.id.monthlyTable);

        //set layouts to invisible on load
        monthlyTable.setVisibility(View.INVISIBLE);
        customViewLayout.setVisibility(View.INVISIBLE);

        setupPieChart();

        setupData(getPieData());

        initializeBarChart();
        setupBarChart();

        btnMonthlySpendingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               monthlyTable.setVisibility(View.VISIBLE);
                customViewLayout.setVisibility(View.INVISIBLE);
                getSumOfSpendingPerMonth(randUserId);

            }
        });

        btnCustomSpendingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDuration_choice.requestFocus();
                customViewLayout.setVisibility(View.VISIBLE);
                monthlyTable.setVisibility(View.INVISIBLE);
            }
        });

        //obtain views to populate monthly stats
        totalTasksPerMonth = findViewById(R.id.totalTasksPerMonth);
        totalCompletedTasksPerMonth = findViewById(R.id.totalTasksCompletedPerMonth);
        totalPendingTasksPerMonth = findViewById(R.id.totalTasksPendingPerMonth);

        totalTasksPerMonth.setText("" + populateTotalTasksView(randUserId));
        totalCompletedTasksPerMonth.setText("" + populateCompletedTasksView(randUserId));
        totalPendingTasksPerMonth.setText("" + populatePendingTasksView(randUserId));


        monthsSelectionDropDownOnDashBoard = findViewById(R.id.monthsSelectionDropDownOnDashBoard);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Months, android.R.layout.simple_dropdown_item_1line);
        monthsSelectionDropDownOnDashBoard.setAdapter(adapter);
        monthsSelectionDropDownOnDashBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = (String) adapterView.getItemAtPosition(i);
                monthIndex = i;
                //getSumOfTasksPerMonthForDashBoard
                totalTasksPerMonth.setText("" + populateTotalTasksPerMonthView(randUserId, monthIndex + 1));
                totalCompletedTasksPerMonth.setText("" + populateCompletedTasksPerMonthView(randUserId, monthIndex + 1));
                totalPendingTasksPerMonth.setText("" + populatePendingTasksPerMonthView(randUserId, monthIndex + 1));


            }
        });


    }


    private void spendingListViewPopulate(Cursor cursor) {
        if (simpleCursorAdapter == null) {
            simpleCursorAdapter = new SimpleCursorAdapter(Dashboard.this, R.layout.spending_listview, cursor, new String[]{"TaskTitle", "TaskAssociatedPrice"}, new int[]{R.id.spendingViewTitle_textView, R.id.spendingViewPrice_textView}, 0);
            showSpendingListView.setAdapter(simpleCursorAdapter);
        }
        simpleCursorAdapter.swapCursor(cursor);
    }

    public Cursor getSpendingSumCursor(Double randUserId, LocalDateTime startDate, LocalDateTime endDate) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getSumPerMonth(randUserId,startDate, endDate);
        } catch (Exception e) {
            System.out.println("Error getting sum " + e);
        }
        return cursor;
    }

    public Cursor getSpendingDetailsCursor(LocalDateTime startDate, LocalDateTime endDate) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getSpendingDetails(startDate, endDate);
        } catch (Exception e) {
            System.out.println("Error getting spending details " + e);
        }
        return cursor;
    }


    public void populateSpendingDetails(LocalDateTime startDate, LocalDateTime endDate) {
        Cursor cursor = getSpendingDetailsCursor(startDate, endDate);
        spendingListViewPopulate(cursor);
    }

    public int populateSpendingView(Double randUserId,LocalDateTime startDate, LocalDateTime endDate) {
        int spendingSum = 0;
        Cursor cursor = getSpendingSumCursor(randUserId, startDate, endDate);

        if (cursor.moveToFirst()) {
            try {
                spendingSum = cursor.getInt(cursor.getColumnIndexOrThrow("sumTotalSpendingPerMonth"));
            } catch (Exception e) {
                System.out.println("Error getting sum " + e);
            }
        } else {
            spendingSum = 0;
        }

        Toast.makeText(this, "" + spendingSum, Toast.LENGTH_SHORT).show();
        return spendingSum;
    }

    public LocalDateTime[] getMonthBasedStats(int indexOfMonth) {
        String newMonthIndex;
        LocalDateTime[] dateRanges = new LocalDateTime[2];

        //for proper date formatting, add zero before month
        if (indexOfMonth <= 9) {
            newMonthIndex = "0" + indexOfMonth;
        } else {
            newMonthIndex = "" + indexOfMonth;
        }
        //obtain the current year
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);

        //setup date ranges
        String rangeStart = "01-" + newMonthIndex + "-" + mYear + " 00:01";
        String rangeEnd = "31-" + newMonthIndex + "-" + mYear + " 00:01";
        System.out.println(rangeStart + " : " + rangeEnd);


        dateRanges[0] = stringToDate(rangeStart);
        dateRanges[1] = stringToDate(rangeEnd);
        System.out.println(dateRanges[0] + " :: " + dateRanges[1]);
        return dateRanges;

    }

    public Cursor getCompletedTasksPerMonth(Double randUserId, LocalDateTime startDate, LocalDateTime endDate) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getCountOfCompletedTasksPerMonth(randUserId, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getCompletedTasks(Double randUserId) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getCountOfCompletedTasks(randUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getTotalTasksCountPerMonth(Double randUserid, LocalDateTime startDate, LocalDateTime endDate) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getSumOfTasksPerMonthForDashBoard(randUserid, startDate, endDate);
            System.out.println("Success getting count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getTotalTasksCount(Double randUserid) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getSumOfTasksForDashBoard(randUserid);
            System.out.println("Success getting count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getTotalPendingTasksPerMonth(Double randUserId, LocalDateTime startDate, LocalDateTime endDate) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getCountOfPendingTasksPerMonth(randUserId, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public Cursor getTotalPendingTasksCount(Double randUserid) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getCountOfPendingTasks(randUserid);
            System.out.println("Success getting count");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public int populateTotalTasksView(Double randUserId) {
        Cursor cursor = getTotalTasksCount(randUserId);
        if (cursor.moveToFirst()) {
            totalTasks = cursor.getInt(cursor.getColumnIndexOrThrow("sumTotalTasks"));
        }
        return totalTasks;
    }

    public int populateTotalTasksPerMonthView(Double randUserId, int monthIndex) {
        LocalDateTime[] range = getMonthBasedStats(monthIndex);
        LocalDateTime startDate, endDate;
        startDate = range[0];
        endDate = range[1];
        Cursor cursor = getTotalTasksCountPerMonth(randUserId, startDate, endDate);
        if (cursor.moveToFirst()) {
            totalTasksPerMonth_int = cursor.getInt(cursor.getColumnIndexOrThrow("sumTotalTasksPerMonth"));
        }
        return totalTasksPerMonth_int;
    }

    public int populateCompletedTasksPerMonthView(Double randUserId, int monthIndex) {
        LocalDateTime[] range = getMonthBasedStats(monthIndex);
        LocalDateTime startDate, endDate;
        startDate = range[0];
        endDate = range[1];
        Cursor cursor = getCompletedTasksPerMonth(randUserId, startDate, endDate);
        if (cursor.moveToNext()) {
            totalCompletedTasksPerMonth_int = cursor.getInt(cursor.getColumnIndexOrThrow("completedTasksPerMonth"));
        }
        return totalCompletedTasksPerMonth_int;
    }

    public int populatePendingTasksPerMonthView(Double randUserId, int monthIndex) {
        LocalDateTime[] range = getMonthBasedStats(monthIndex);
        LocalDateTime startDate, endDate;
        startDate = range[0];
        endDate = range[1];
        Cursor cursor = getTotalPendingTasksPerMonth(randUserId, startDate, endDate);
        if (cursor.moveToNext()) {
            totalPendingTasksPerMonth_int = cursor.getInt(cursor.getColumnIndexOrThrow("pendingTasksPerMonth"));
        }
        return totalPendingTasksPerMonth_int;
    }

    public int populateCompletedTasksView(Double randUserId) {
        Cursor cursor = getCompletedTasks(randUserId);
        if (cursor.moveToNext()) {
            totalCompletedTasks_int = cursor.getInt(cursor.getColumnIndexOrThrow("completedTasks"));
        }
        return totalCompletedTasks_int;
    }

    public int populatePendingTasksView(Double randUserId) {
        Cursor cursor = getTotalPendingTasksCount(randUserId);
        if (cursor.moveToNext()) {
            totalPendingTasks_int = cursor.getInt(cursor.getColumnIndexOrThrow("totalPendingTasks"));
        }
        return totalPendingTasks_int;
    }

    public LocalDateTime stringToDate(String date) {
        LocalDateTime newDate = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            newDate = getDateFromString(date, dateTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public void getSumOfSpendingPerMonth(Double randUserId) {

        MaterialTextView[] monthlyTextViews = {Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sept, Oct, Nov, Dec};
        Cursor monthLySumCursor = null;

        for (int i = 1; i <= 12; i++) {
            //obtain monthly date ranges
            LocalDateTime[] monthlyRange = getMonthBasedStats(i);

            System.out.println(monthlyRange[0] + " ::: " + monthlyRange[1]);
            //obtain monthly sum
            try {
                monthLySumCursor = dbHelper.getSumPerMonth(randUserId, monthlyRange[0], monthlyRange[1]);

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (monthLySumCursor.moveToFirst()) {
                int monthlySpendingSum = monthLySumCursor.getInt(monthLySumCursor.getColumnIndexOrThrow("sumTotalSpendingPerMonth"));
                System.out.println("Total spending: " + monthlySpendingSum);
                //set text to view
                monthlyTextViews[i -1].setTextSize(12);
                monthlyTextViews[i - 1].setText("" +  numberFormat.format(monthlySpendingSum));
            }
        }
    }




    public long[] getSumOfSpendingPerMonthForBarChart(Double randUserId) {
        long spendingPerMonth[] = new long[12];
        Cursor monthLySumCursor = null;
        int monthlySpendingSum = 0;

        for (int i = 0; i < 12; i++) {
            //obtain monthly date ranges
            LocalDateTime[] monthlyRange = getMonthBasedStats(i + 1);

            //obtain monthly sum
            try {
                monthLySumCursor = dbHelper.getSumPerMonth(randUserId, monthlyRange[0], monthlyRange[1]);

                if (monthLySumCursor.moveToNext()) {
                    monthlySpendingSum = monthLySumCursor.getInt(monthLySumCursor.getColumnIndexOrThrow("sumTotalSpendingPerMonth"));
                    monthLySumCursor.close();

                    spendingPerMonth[i] = monthlySpendingSum;
                    System.out.println(i + ": " + spendingPerMonth[i]);
                }else{
                    spendingPerMonth[i] = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return spendingPerMonth;
    }
    public void initializeBarChart() {
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value];
            }
        });

        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#000000"));
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);

        barChart.getLegend().setEnabled(false);
        Description description = barChart.getDescription();
        description.setText("Spending per month");



    }


    public void setupBarChart(){
        long[] bills = getSumOfSpendingPerMonthForBarChart(randUserId);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

        ArrayList<BarEntry> barDatum = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            barDatum.add(new BarEntry(i, bills[i]));
        }
        BarDataSet barDataSet = new BarDataSet(barDatum,"Spending");
        barDataSet.setValueTextSize(8f);

        ArrayList<Integer> colors = new ArrayList<>();


        int startColor = rgb("#dd2c00");
        int endColor = rgb("#000000");

        List<GradientColor> gradientColors = new ArrayList<>();
        gradientColors.add(new GradientColor(startColor, endColor));
        barDataSet.setGradientColors(gradientColors);



        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
        barChart.animateY(1400, Easing.EaseInOutSine);



    }


    public long[] getSpendingTotalPerCategory(Double randUserId){
        Cursor cursor = null;
        long[] bills = new long[5];
        String[] categories = {"Home","Shopping", "Business","Work","School"};

        for (int i = 0; i < bills.length ; i++) {
            try {
                cursor = dbHelper.getSumPerCategory(randUserId, categories[i]);
                if (cursor.moveToNext()){
                    long sum = cursor.getLong(cursor.getColumnIndexOrThrow("sumTotalSpendingPerCategory"));
                    bills[i] = sum;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return bills;
    }


    public  ArrayList<PieEntry> getPieData(){
        long[] bills = getSpendingTotalPerCategory(randUserId);
        String[] categories = {"Home","Shopping", "Business","Work","School"};
        ArrayList<PieEntry> pieDatum = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            pieDatum.add(new PieEntry(bills[i],categories[i]));
            System.out.println(bills[i] + "" + categories[i]);
        }
        return pieDatum;
    }
    public void setupPieChart() {

        Description description = pieChart.getDescription();
        description.setText("Spending by category");
        description.setTextSize(10f);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(55f);
        pieChart.setTransparentCircleAlpha(10);
        pieChart.setCenterText("Bills");
        pieChart.setCenterTextSize(20f);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

    }
    public void setupData(ArrayList<PieEntry> pieDataArrayList) {

        ArrayList<Integer> colors = new ArrayList<>();





       for (int color : ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }
        PieDataSet pieDataSet = new PieDataSet(pieDataArrayList,"");
        pieDataSet.setSliceSpace(2);
        //add colors to dataset
        pieDataSet.setColors(colors);


        //create pieChart object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutSine);
        }

    }

