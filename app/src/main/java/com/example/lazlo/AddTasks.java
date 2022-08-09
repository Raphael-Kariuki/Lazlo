package com.example.lazlo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lazlo.Sql.DBHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(), PendingTasks.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);



        task_title =  findViewById(R.id.taskTitleAutoCompleteView);
        task_title.requestFocus();
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
                String randUserId = tasks_sharedPrefs.getString("randomUserId", null);
                String taskTitle_String = task_title.getText().toString().trim();
                String taskDescription_String = task_description.getText().toString().trim();
                String selectedDate_String = select_date.getText().toString().trim();
                String TaskAssociatedPrice =  priceAutocompleteView.getText().toString().trim();
                String selectedCategory_string = tasksCategories.getText().toString().trim();
                String selectedTime_String = selectTime_AutocompleteView.getText().toString().trim();
                String selectedDateTime = selectedDate_String + " " + selected_time;

                //process inputs
                if (!taskTitle_String.isEmpty()){
                    if (!taskDescription_String.isEmpty()){
                        if (!selectedCategory_string.isEmpty()){
                            if ( (selectedCategory_string.equals("Shopping") || selectedCategory_string.equals("Work") || selectedCategory_string.equals("School") || selectedCategory_string.equals("Business") || selectedCategory_string.equals("Home") )){
                                if (!TaskAssociatedPrice.isEmpty() && priceCheck(TaskAssociatedPrice)){
                                    willPriceFormat(TaskAssociatedPrice);
                                    if (!selectedDate_String.isEmpty() && selectedDate_String != null && dateCheck(selectedDate_String)){
                                        if (!selectedTime_String.isEmpty() && selectedTime_String != null && timeCheck(selectedTime_String)){
                                            if (willDateFormat(selectedDateTime)){
                                                date_now = LocalDateTime.now();
                                                if (selected_date.compareTo(date_now) > 0 || selected_date.compareTo(date_now) == 0) {
                                                    try {
                                                        //insert task to db if dates are cool
                                                        houseOfCommons commons = new houseOfCommons();
                                                        Double randomTaskId = commons.generateRandomId();
                                                        Integer defaultTaskState = 0;
                                                        b = dbHelper.insertTasks(randomTaskId,Double.parseDouble(randUserId),USERNAME, taskTitle_String, taskDescription_String, selected_category, Price, selected_date,getDateTimeNow(),defaultTaskState);

                                                    }catch(Exception e){
                                                        System.out.println("Db insertion error: " + e);
                                                    }
                                                    if (b){
                                                        Toast.makeText(getApplicationContext(), "Task inserted successfully", Toast.LENGTH_LONG).show();
                                                        Intent categoryStringToSendToPendingTasks = new Intent(getApplicationContext(), PendingTasks.class);
                                                        categoryStringToSendToPendingTasks.putExtra("tempCategory", selected_category);
                                                        startActivity(categoryStringToSendToPendingTasks);
                                                        //finish();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(), "Task insert failure", Toast.LENGTH_LONG).show();
                                                    }
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Choose another date", Toast.LENGTH_LONG).show();
                                                    select_date.setText("");
                                                }
                                            }else{
                                                selectedDate_TextInputLayout.setErrorEnabled(true);
                                                selectedDate_TextInputLayout.setError("select date");
                                                selectedTime_TextInputLayout.setErrorEnabled(true);
                                                selectedTime_TextInputLayout.setError("select time");
                                                taskTitle_TextLayout.setErrorEnabled(false);
                                                taskDescription_TextLayout.setErrorEnabled(false);
                                                price_TextLayout.setErrorEnabled(false);
                                                tasksCategoryTextLayout.setErrorEnabled(false);
                                            }

                                        }else{
                                            selectedDate_TextInputLayout.setErrorEnabled(false);
                                            selectedTime_TextInputLayout.setErrorEnabled(true);
                                            selectedTime_TextInputLayout.setError("select time");
                                            taskTitle_TextLayout.setErrorEnabled(false);
                                            taskDescription_TextLayout.setErrorEnabled(false);
                                            price_TextLayout.setErrorEnabled(false);
                                            tasksCategoryTextLayout.setErrorEnabled(false);
                                        }


                                    }else{
                                        selectedDate_TextInputLayout.setErrorEnabled(true);
                                        selectedDate_TextInputLayout.setError("Blank deadline");
                                        taskTitle_TextLayout.setErrorEnabled(false);
                                        taskDescription_TextLayout.setErrorEnabled(false);
                                        price_TextLayout.setErrorEnabled(false);
                                        tasksCategoryTextLayout.setErrorEnabled(false);
                                        selectedTime_TextInputLayout.setErrorEnabled(false);
                                    }
                                }else{
                                    price_TextLayout.setErrorEnabled(true);
                                    price_TextLayout.setError("Enter a money figure");
                                    taskTitle_TextLayout.setErrorEnabled(false);
                                    taskDescription_TextLayout.setErrorEnabled(false);
                                    tasksCategoryTextLayout.setErrorEnabled(false);
                                    selectedDate_TextInputLayout.setErrorEnabled(false);
                                    selectedTime_TextInputLayout.setErrorEnabled(false);
                                }

                            }  else{
                                tasksCategoryTextLayout.setErrorEnabled(true);
                                tasksCategoryTextLayout.setError("Choose a category from the dropdown");
                                taskTitle_TextLayout.setErrorEnabled(false);
                                taskDescription_TextLayout.setErrorEnabled(false);
                                price_TextLayout.setErrorEnabled(false);
                                selectedDate_TextInputLayout.setErrorEnabled(false);
                                selectedTime_TextInputLayout.setErrorEnabled(false);
                            }


                        }else{
                            tasksCategoryTextLayout.setErrorEnabled(true);
                            tasksCategoryTextLayout.setError("Blank category");
                            taskTitle_TextLayout.setErrorEnabled(false);
                            taskDescription_TextLayout.setErrorEnabled(false);
                            price_TextLayout.setErrorEnabled(false);
                            selectedDate_TextInputLayout.setErrorEnabled(false);
                            selectedTime_TextInputLayout.setErrorEnabled(false);
                        }
                    }else{
                        taskDescription_TextLayout.setErrorEnabled(true);
                        taskDescription_TextLayout.setError("Blank description");
                        taskTitle_TextLayout.setErrorEnabled(false);
                        tasksCategoryTextLayout.setErrorEnabled(false);
                        price_TextLayout.setErrorEnabled(false);
                        selectedDate_TextInputLayout.setErrorEnabled(false);
                        selectedTime_TextInputLayout.setErrorEnabled(false);
                    }

                }else{
                    taskTitle_TextLayout.setErrorEnabled(true);
                    taskTitle_TextLayout.setError("Blank title");
                    taskDescription_TextLayout.setErrorEnabled(false);
                    tasksCategoryTextLayout.setErrorEnabled(false);
                    price_TextLayout.setErrorEnabled(false);
                    selectedDate_TextInputLayout.setErrorEnabled(false);
                    selectedTime_TextInputLayout.setErrorEnabled(false);
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
    public boolean dateCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[-]).{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public boolean timeCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[:]).{3,9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public boolean priceCheck(String passphrase){
        String regex = "^(?=.*[0-9]).{1,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public boolean willDateFormat(String selectedDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            selected_date = getDateFromString(selectedDate, dateTimeFormatter);
            return true;
        }catch (IllegalArgumentException e){
            System.out.println("Date Exception" + e);
            return false;
        }
    }

    public LocalDateTime formatLocalDateTimePlusSeconds(String date_now){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm:ss");
        LocalDateTime formatted_dateNow = null;
        try {
           formatted_dateNow = getDateFromString(date_now, dateTimeFormatter);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return formatted_dateNow;
    }

    public LocalDateTime getDateTimeNow(){
        Calendar calendar = Calendar.getInstance(new Locale("en","KE"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String formattedHour = null, formattedMinute = null;        String formattedMonth = null,formattedDay = null,formattedSecond = null;

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

        if (month + 1 <= 9){
            formattedMonth = "0" + (month + 1) ;
        }else{
            formattedMonth = String.valueOf(month + 1);
        }
        if(day < 10){
            formattedDay = "0" + day;
        }else{
            formattedDay = String.valueOf(day);
        }
        if(second < 10){
            formattedSecond = "0" + second;
        }else{
            formattedSecond = String.valueOf(second);
        }
        String dateNow = formattedDay + "-" + formattedMonth + "-" + year + " " + formattedHour +":" + formattedMinute + ":" + formattedSecond;
        return formatLocalDateTimePlusSeconds(dateNow);

    }
    public boolean willPriceFormat(String priceToParse){
                if (!priceToParse.isEmpty()){
                    try {
                        Price = Double.parseDouble(priceToParse);
                        return true;
                    }catch(java.lang.NumberFormatException e){
                        System.out.println("Price Exception" + e);
                        return false;
                    }
                }else {
                    Price = 0.0;
                }
                return true;


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
