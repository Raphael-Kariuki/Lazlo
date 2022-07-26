package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AddTasks extends AppCompatActivity {
    TextInputEditText task_title,task_description,select_date,priceAutocompleteView,selectTime_AutocompleteView;
    DatePickerDialog datePickerDialog;
    AppCompatButton btn_saveTasks, btn_cancelTaskCreation;
    DBHelper dbHelper;
    SharedPreferences tasks_sharedPrefs;
    LocalDateTime selected_date,date_now;
    AutoCompleteTextView tasksCategories;
    String selected_category, selected_time;
    Double Price;
    TextInputLayout taskTitle_TextLayout,taskDescription_TextLayout,tasksCategoryTextLayout,
            price_TextLayout,selectedDate_TextInputLayout,selectedTime_TextInputLayout;
    boolean b,d;

//method to parse date input from adding task

    public static LocalDateTime getDateFromString(String string,DateTimeFormatter dateTimeFormatter){
        return LocalDateTime.parse(string, dateTimeFormatter);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);



        task_title =  findViewById(R.id.taskTitleAutoCompleteView);
        task_description = findViewById(R.id.taskDescriptionAutoCompleteView);
        select_date =  findViewById(R.id.selectDate_AutocompleteView);
        priceAutocompleteView = findViewById(R.id.priceAutoCompleteView);
        tasksCategories = findViewById(R.id.tasksAutoCompleteView);
        selectTime_AutocompleteView = findViewById(R.id.selectTime_AutocompleteView);

        taskTitle_TextLayout = findViewById(R.id.taskTitle_TextLayout);
        taskDescription_TextLayout = findViewById(R.id.taskDescription_TextLayout);
        tasksCategoryTextLayout = findViewById(R.id.tasksCategoryTextLayout);
        price_TextLayout = findViewById(R.id.price_TextLayout);
        selectedDate_TextInputLayout = findViewById(R.id.selectedDate_TextInputLayout);
        selectedTime_TextInputLayout = findViewById(R.id.selectedTime_TextInputLayout);

        btn_saveTasks = findViewById(R.id.btn_saveTask);
        btn_cancelTaskCreation = findViewById(R.id.cancelTaskCreation);

        dbHelper = new DBHelper(this);
        // get the string username broadcast from login to stand in as the
        //determiner of who enters tasks. Should be replaced by the username or userId
        tasks_sharedPrefs = getSharedPreferences("user_details",MODE_PRIVATE);





        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.categories, android.R.layout.simple_dropdown_item_1line);
        tasksCategories.setAdapter(adapter);
        tasksCategories.setOnItemClickListener((adapterView, view, i, l) -> selected_category = (String) adapterView.getItemAtPosition(i));





        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //date picker dialog
                datePickerDialog = new DatePickerDialog(AddTasks.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String formattedMonth = null,formattedDay = null;
                        if (monthOfYear + 1 <= 9){
                            formattedMonth = "0" + (monthOfYear + 1) ;
                        }else{
                            formattedMonth = String.valueOf(monthOfYear + 1);
                        }
                        if(dayOfMonth < 10){
                            formattedDay = "0" + dayOfMonth;
                        }else{
                            formattedDay = String.valueOf(dayOfMonth);
                        }
                        select_date.setText(formattedDay + "-" + formattedMonth + "-" + year);
                    }
                }, mYear,mMonth, mDay);
                datePickerDialog.show();
            }
        });



        selectTime_AutocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });




        btn_saveTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get inputs to insert to db
                String USERNAME = tasks_sharedPrefs.getString("username",null);
                String taskTitle_String = task_title.getText().toString().trim();
                String taskDescription_String = task_description.getText().toString().trim();
                String selectedDate_String = select_date.getText().toString().trim();
                String TaskAssociatedPrice =  priceAutocompleteView.getText().toString().trim();
                String selectedCategory_string = tasksCategories.getText().toString().trim();
                String selectedDateTime = selectedDate_String + " " + selected_time;

                //process inputs
                if (!taskTitle_String.isEmpty()){
                    if (!taskDescription_String.isEmpty()){
                        if (!selectedCategory_string.isEmpty()){
                            if (!selectedDate_String.isEmpty() && willDateFormat(selectedDateTime)){
                                if (willPriceFormat(TaskAssociatedPrice)){
                                    date_now = LocalDateTime.now();
                                    if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0) {
                                        try {
                                            //insert task to db if dates are cool
                                            b = dbHelper.insertTasks(USERNAME, taskTitle_String, taskDescription_String, selected_category, Price, selected_date);

                                        }catch(Exception e){
                                            System.out.println("Db insertion error: " + e);
                                        }
                                        if (b){
                                            Toast.makeText(getApplicationContext(), "Task inserted successfully", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Task insert failure", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Choose another date", Toast.LENGTH_LONG).show();
                                        select_date.setText("");
                                    }
                                }else{
                                    price_TextLayout.setErrorEnabled(true);
                                    price_TextLayout.setError("Wrong price");
                                    taskTitle_TextLayout.setErrorEnabled(false);
                                    taskDescription_TextLayout.setErrorEnabled(false);
                                    price_TextLayout.setErrorEnabled(false);
                                    tasksCategoryTextLayout.setErrorEnabled(false);
                                    selectedDate_TextInputLayout.setErrorEnabled(false);
                                }
                            }else{
                                selectedDate_TextInputLayout.setErrorEnabled(true);
                                selectedDate_TextInputLayout.setError("Blank deadline");
                                taskTitle_TextLayout.setErrorEnabled(false);
                                taskDescription_TextLayout.setErrorEnabled(false);
                                price_TextLayout.setErrorEnabled(false);
                                tasksCategoryTextLayout.setErrorEnabled(false);
                            }

                        }else{
                            tasksCategoryTextLayout.setErrorEnabled(true);
                            tasksCategoryTextLayout.setError("Blank category");
                            taskTitle_TextLayout.setErrorEnabled(false);
                            taskDescription_TextLayout.setErrorEnabled(false);
                            price_TextLayout.setErrorEnabled(false);
                            selectedDate_TextInputLayout.setErrorEnabled(false);
                        }
                    }else{
                        taskDescription_TextLayout.setErrorEnabled(true);
                        taskDescription_TextLayout.setError("Blank description");
                        taskTitle_TextLayout.setErrorEnabled(false);
                        tasksCategoryTextLayout.setErrorEnabled(false);
                        price_TextLayout.setErrorEnabled(false);
                        selectedDate_TextInputLayout.setErrorEnabled(false);
                    }

                }else{
                    taskTitle_TextLayout.setErrorEnabled(true);
                    taskTitle_TextLayout.setError("Blank title");
                    taskDescription_TextLayout.setErrorEnabled(false);
                    tasksCategoryTextLayout.setErrorEnabled(false);
                    price_TextLayout.setErrorEnabled(false);
                    selectedDate_TextInputLayout.setErrorEnabled(false);
                    }




            }
        });
        btn_cancelTaskCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        try {
                            String USERNAME = tasks_sharedPrefs.getString("username",null);
                            String taskTitle_String = task_title.getText().toString().trim();
                            String taskDescription_String = task_description.getText().toString().trim();
                            String selectedDate_String = select_date.getText().toString().trim();
                            String TaskAssociatedPrice =  priceAutocompleteView.getText().toString().trim();
                            String selectedCategory_string = tasksCategories.getText().toString().trim();
                            d = dbHelper.insertDraftTasks(USERNAME, taskTitle_String, taskDescription_String, selectedCategory_string, TaskAssociatedPrice, selectedDate_String);
                            if (d){
                                Toast.makeText(getApplicationContext(), "Draft saved successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                        }






            }
        });
    }
    private boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            selected_date = getDateFromString(selectedDate, dateTimeFormatter);
            System.out.print("get date from string " + selected_date);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
    }
    private boolean willPriceFormat(String priceToParse){
            try {
                if (!priceToParse.isEmpty()){
                    Price = Double.parseDouble(priceToParse);
                }else {
                    Price = 0.0;
                }
                return true;
            }catch(Exception e){
                System.out.println("Price Exception" + e);
                return false;
            }

    }
    //this method converts the time into 12hr format and assigns am or pm
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }
private void selectTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
    TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String formattedHour = null, formattedMinute = null;
            if (hour < 10){
                formattedHour = "0" + hour;
            }else{
                formattedHour = "" + hour;
            }
            if (minute < 10 ){
                formattedMinute = "0" + minute;
            }else{
                formattedMinute = "" + minute;
            }
            selected_time = formattedHour + ":" + formattedMinute;
            selectTime_AutocompleteView.setText(FormatTime(hour, minute));
        }
    },hour, minute,false);
    timePickerDialog.show();
}
    }
